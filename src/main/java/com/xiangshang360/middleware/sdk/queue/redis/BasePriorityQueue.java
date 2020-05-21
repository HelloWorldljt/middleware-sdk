package com.xiangshang360.middleware.sdk.queue.redis;

import com.xiangshang360.middleware.redis.JedisPoolFactory;
import com.xiangshang360.middleware.sdk.queue.PriorityQueue;
import com.xiangshang360.middleware.sdk.queue.QueueUnit;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 基于 redis sortedSet 的 优先级队列
 * 实现了基本的优先级队列功能，但是不能刷新进度条
 *
 * @author 段子彧
 * @date 20180810
 */
public class BasePriorityQueue implements PriorityQueue {

    private static final Logger LOG = LoggerFactory
            .getLogger(BasePriorityQueue.class);


    protected JedisPool jedisPool;

    /**
     * redis 中的队列名称
     */
    String queueName;

    /**
     * 原始队列名称
     */
    String oriName;

    KeyGenerator keyGenerator;

    private static final String QUEUE_PREFIX = "PRIORITY_QUEUE_";

    private static final String TIMESTAMP_SUFFIX = "_TIMESTAMP";

    private List<BeforeCancelTransactionCommit> beforeCancelTransactionCommitList = new ArrayList<>();

    private List<BeforeEnqueueTransactionCommit> beforeEnqueueTransactionCommitList = new ArrayList<>();


    BasePriorityQueue(String queueName) {
        this.queueName = QUEUE_PREFIX + queueName;
        this.oriName = queueName;
        try {
            this.jedisPool = JedisPoolFactory.getJedisPool();
            this.keyGenerator = new KeyGenerator(jedisPool, this.queueName);
        } catch (ConfigurationException | IOException e) {
            LOG.error(" init error, queue:"+queueName(), e);
        }
    }

    @Override
    public boolean enqueue(QueueUnit unit) {

        try (Jedis jedis = jedisPool.getResource()) {
            if (StringUtils.isEmpty(unit.getId())) {
                LOG.error("unit.id is null!");
                return false;
            }
            if (StringUtils.isEmpty(unit.getData())) {
                LOG.error("unit.data is null!");
                return false;
            }
            if (unit.getPriorityNum() == null) {
                LOG.error("unit.priorityNum is null!");
                return false;
            }

            if (unit.getAmount() == null) {
                LOG.error("unit.amount is null!");
                return false;
            }


            String id = keyGenerator.getKey(unit.getId(), RedisPriorityQueueKeyEnum.ID_KEY);

            if(StringUtils.isNotEmpty(id)){
                cancel(unit.getId());
            }

           id = keyGenerator.getKey(unit.getId(),RedisPriorityQueueKeyEnum.ADD_ID_KEY);

            Double prio = RedisQueueUtil.convertPriorityNum(unit.getPriorityNum().doubleValue());

            Transaction transaction = jedis.multi();

            //保存排队数据
            transaction.set(id, unit.getData());
            // 获取队列长度
            Long size = getQueueSize() + 1;

            //设置位置信息
            transaction.set(keyGenerator.getKeyById(id, RedisPriorityQueueKeyEnum.ORI_POSITION_KEY), size.toString());
            //设置进度条，初始0.01
            transaction.set(keyGenerator.getKeyById(id, RedisPriorityQueueKeyEnum.PROGRESS_KEY), "0.01");

            transaction.set(keyGenerator.getKeyById(id, RedisPriorityQueueKeyEnum.AMOUNT_KEY), unit.getAmount().setScale(2, RoundingMode.DOWN).toString());
            //插入队列
            transaction.zadd(queueName, prio, id);

            for (BeforeEnqueueTransactionCommit enqueueTransactionCommit : beforeEnqueueTransactionCommitList) {
                enqueueTransactionCommit.execute(unit, transaction);
            }

            transaction.exec();

            return true;
        } catch (Exception e) {
            LOG.error("queue:{},enqueue error,{} ", queueName, unit, e);
            return false;
        }

    }


    @Override
    public QueueUnit dequeue() {

        try (Jedis jedis = jedisPool.getResource()) {
            Long size = getQueueSize();
            if (size == null) {
                LOG.info("queue:{} size is null", queueName);
                return null;
            }
            Set<Tuple> idSet = jedis.zrangeWithScores(queueName, 0, 0);
            String id = "";
            int priorityNum = 0;
            BigDecimal amount = BigDecimal.ZERO;
            if (idSet.size() == 0) {
                return null;
            } else {
                for (Tuple tuple : idSet) {
                    id = tuple.getElement();
                    priorityNum = RedisQueueUtil.convertPriorityNum(tuple.getScore()).intValue();
                    String amtStr = jedis.get(keyGenerator.getKeyById(id, RedisPriorityQueueKeyEnum.AMOUNT_KEY));
                    if (!StringUtils.isEmpty(amtStr)) {
                        amount = new BigDecimal(amtStr);
                    }
                }

            }
            String data = jedis.get(id);
            return new QueueUnit(RedisQueueUtil.getUnitId(id, queueName), data, priorityNum, amount);
        } catch (Exception e) {
            LOG.error(" dequeue error,queue:"+ queueName(), e);
            return null;
        }

    }

    @Override
    public boolean cancel(String unitId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String id = keyGenerator.getKey(unitId, RedisPriorityQueueKeyEnum.ID_KEY);

            if(StringUtils.isEmpty(id)){
                LOG.info("unit:{} not exit,cancel abort");
                return false;
            }

            String amountKey = keyGenerator.getKeyById(id, RedisPriorityQueueKeyEnum.AMOUNT_KEY);

            Transaction transaction = jedis.multi();
            //sorted set 中删除
            transaction.zrem(queueName, id);
            // 删除unit.data
            transaction.del(id);
            // 删除原始位置
            transaction.del(keyGenerator.getKeyById(id, RedisPriorityQueueKeyEnum.ORI_POSITION_KEY));
            // 删除进度
            transaction.del(keyGenerator.getKeyById(id, RedisPriorityQueueKeyEnum.PROGRESS_KEY));

            //删除金额
            transaction.del(amountKey);

            // 删除时间戳
            String redisId = queueName + "_" + unitId;
            String timestampKey = redisId + TIMESTAMP_SUFFIX;
            transaction.del(timestampKey);

            QueueUnit unit = getUnitByUnitId(unitId);
            for (BeforeCancelTransactionCommit cancelTransactionCommit : beforeCancelTransactionCommitList) {
                cancelTransactionCommit.execute(unit, transaction);

            }
            transaction.exec();
            return true;
        } catch (Exception e) {

            LOG.error("cancel error,queue:"+oriName, e);
            return false;
        }
    }

    @Override
    public Long queryPosition(String unitId) {

        try (Jedis jedis = jedisPool.getResource()) {
            String id =keyGenerator.getKey(unitId, RedisPriorityQueueKeyEnum.ID_KEY);
            if(StringUtils.isEmpty(id)){
                return null;
            }
            return jedis.zrank(queueName,id );
        } catch (Exception e) {
            LOG.error("queryPosition error,queue:"+oriName, e);
            return null;
        }


    }

    @Override
    public String queryProgress(String unitId) {

        try (Jedis jedis = jedisPool.getResource()) {
            String progressId =keyGenerator.getKey(unitId, RedisPriorityQueueKeyEnum.PROGRESS_KEY);
            if(StringUtils.isEmpty(progressId)){
                return null;
            }
            return jedis.get(progressId);
        } catch (Exception e) {
            LOG.error("queryProgress error,queue:"+ oriName, e);
            return null;
        }

    }


    @Override
    public boolean ack(String unitId) {
        return cancel(unitId);
    }

    @Override
    public boolean changePriority(String unitId, Integer priorityNum) {
        try (Jedis jedis = jedisPool.getResource()) {
            String id = keyGenerator.getKey(unitId, RedisPriorityQueueKeyEnum.ID_KEY);
            Double score = jedis.zscore(queueName, id);
            if (score == null) {
                LOG.error("unitId:{} changePriority error,not exist in queue:{}", unitId, oriName);
                return false;
            }

            Double prio = RedisQueueUtil.convertPriorityNum(priorityNum.doubleValue());

            Long count = jedis.zadd(queueName, prio, id);
            return count != 1;
        } catch (Exception e) {
            LOG.error("unitId:{} changePriority error, queue:{}", unitId, oriName,e);
            return false;
        }

    }

    @Override
    public Long getQueueSize() {

        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zcard(queueName);
        } catch (Exception e) {
            LOG.error("queue:{} getQueueSize error", oriName);
            return null;
        }


    }

    @Override
    public String queueName() {
        return oriName;
    }

    @Override
    public void addBeforeEnqueueTransaction(BeforeEnqueueTransactionCommit beforeEnqueueTransactionCommit) {
        this.beforeEnqueueTransactionCommitList.add(beforeEnqueueTransactionCommit);
    }

    @Override
    public void addBeforeCancelTransaction(BeforeCancelTransactionCommit beforeCancelTransactionCommit) {

        this.beforeCancelTransactionCommitList.add(beforeCancelTransactionCommit);
    }

    @Override
    public QueueUnit getUnitByUnitId(String unitId) {
        try (Jedis jedis = jedisPool.getResource()) {
            Long size = getQueueSize();
            if (size == null) {
                LOG.info("queue:{} size is null", queueName);
                return null;
            }
            String id = keyGenerator.getKey(unitId, RedisPriorityQueueKeyEnum.ID_KEY);

            if(StringUtils.isEmpty(id)){
                return null;
            }
            Double prio = jedis.zscore(queueName, id);
            int priorityNum = 0;
            BigDecimal amount = BigDecimal.ZERO;
            if (prio == null) {
                return null;
            }
            priorityNum = RedisQueueUtil.convertPriorityNum(prio).intValue();
            String amtStr = jedis.get(keyGenerator.getKeyById(id, RedisPriorityQueueKeyEnum.AMOUNT_KEY));
            if (!StringUtils.isEmpty(amtStr)) {
                amount = new BigDecimal(amtStr);
            }

            String data = jedis.get(id);
            return new QueueUnit(unitId, data, priorityNum, amount);
        } catch (Exception e) {
            LOG.error("getUnitByUnitId error，queue:"+queueName() , e);
            return null;
        }
    }


}
