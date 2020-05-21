package com.xiangshang360.middleware.sdk.queue.redis;

import com.xiangshang360.middleware.sdk.queue.QueueUnit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 基于 redis sortedSet 的 优先级队列
 * 使用 子队列完成实时进度查询
 * @author 段子彧
 * @date 20180810
 */
public class RedisProgressPriorityQueue extends BasePriorityQueue {

    private static final Logger LOG = LoggerFactory
            .getLogger(RedisProgressPriorityQueue.class);

    /**
     * 进度条队列名称
     */
    private String progressQueueName;


    /**
     * 进度条队列前缀
     */
    private static final String QUEUE_PROGRESS_PREFIX = "PRIORITY_QUEUE_PROGRESS_";



    /**
     * 金额分片，固定1000,如果修改分片大小，需要删除子队列，重新刷新
     */
    private static final int AMOUNT_PIECE = 1000;


    public RedisProgressPriorityQueue(String queueName) {
        super(queueName);
        this.progressQueueName = QUEUE_PROGRESS_PREFIX + queueName;


        initAdditionalMethods();
    }

    private void initAdditionalMethods() {
        this.addBeforeCancelTransaction(new BeforeCancelTransactionCommit() {
            @Override
            public void execute(QueueUnit unit, Transaction transaction) {
                String id = keyGenerator.getKey(unit.getId(), RedisPriorityQueueKeyEnum.ID_KEY);
                deleteProgress(id, unit.getAmount(), transaction);
            }
        });

        this.addBeforeEnqueueTransaction(new BeforeEnqueueTransactionCommit() {
            @Override
            public void execute(QueueUnit unit, Transaction transaction) {

                String id = keyGenerator.getKey(unit.getId(), RedisPriorityQueueKeyEnum.ID_KEY);

                insertProgressQueue(id, unit.getPriorityNum(), unit.getAmount(), transaction);
            }
        });
    }


    /**
     * 插入进度条队列
     * 数据与原始队列数据保持一致
     * 按照AMOUNT_PIECE分割
     *
     * @param id          id
     * @param prio        优先级
     * @param amount      总金额
     * @param transaction redis事务操作对象
     * @return
     */
    private void insertProgressQueue(String id, Integer prio, BigDecimal amount, Transaction transaction) {

        id = keyGenerator.getKeyById(id, RedisPriorityQueueKeyEnum.PROGRESS_ID_KEY);

        int pieces = amountSplit(amount);
        for (int i = 0; i < pieces; i++) {
            transaction.zadd(progressQueueName, RedisQueueUtil.convertPriorityNum(prio.doubleValue()), id + "_" + i);
        }

    }


    private void insertProgressQueue(String id, Double prio, BigDecimal amount, Jedis jedis) {

        id = keyGenerator.getKeyById(id, RedisPriorityQueueKeyEnum.PROGRESS_ID_KEY);

        int pieces = amountSplit(amount);
        for (int i = 0; i < pieces; i++) {
            jedis.zadd(progressQueueName, prio, id + "_" + i);
        }

    }

    /**
     * 金额分割算法，整除后四舍五入
     * @param amount
     * @return
     */
    private int amountSplit(BigDecimal amount){
        int pieces = amount.intValue() / AMOUNT_PIECE;
        if (pieces <= 0) {
            pieces = 1;
        }

        if(amount.intValue()>AMOUNT_PIECE&& (amount.intValue()%AMOUNT_PIECE >=(AMOUNT_PIECE/2))){
            pieces+=1;
        }
        return pieces;

    }

    private void deleteProgress(String id, BigDecimal amount, Transaction transaction) {
        id = keyGenerator.getKeyById(id, RedisPriorityQueueKeyEnum.PROGRESS_ID_KEY);

        int pieces = amountSplit(amount);
        for (int i = 0; i < pieces; i++) {
            transaction.zrem(progressQueueName, id + "_" + i);
        }

    }


    /**
     * 重写查询队列进度方法，通过进度条队列实时查询进度
     *
     * @param unitId
     * @return
     */
    @Override
    public String queryProgress(String unitId) {

        try (Jedis jedis = jedisPool.getResource()) {


            String oriProgress = jedis.get(keyGenerator.getKey(unitId, RedisPriorityQueueKeyEnum.PROGRESS_KEY));

            String id = keyGenerator.getKey(unitId, RedisPriorityQueueKeyEnum.ID_KEY);

            if(StringUtils.isEmpty(id)){
                return null;
            }

            String progressId = keyGenerator.getKeyById(id, RedisPriorityQueueKeyEnum.PROGRESS_ID_KEY) + "_0";

           //计算进度条
            BigDecimal position = new BigDecimal(jedis.zrank(progressQueueName, progressId));

            BigDecimal total = new BigDecimal(jedis.zcard(progressQueueName));
            BigDecimal progress = total.subtract(position).divide(total, 4, RoundingMode.HALF_EVEN);

            String progressKey = keyGenerator.getKeyById(id, RedisPriorityQueueKeyEnum.PROGRESS_KEY);
            if (progress.doubleValue() > Double.valueOf(oriProgress)) {
                if (progress.compareTo(BigDecimal.ONE) >= 0) {
                    jedis.set(progressKey, "0.9999");
                    return jedis.get(progressKey);
                }
                jedis.set(progressKey, progress.toString());

            }
            return jedis.get(progressKey);

        } catch (Exception e) {
            LOG.error("queryProgress error,queue:"+ oriName, e);
            return null;
        }


    }

    /**
     * 重写修改优先级方法
     * 同步修改进度条队列的优先级
     * @param unitId
     * @param priorityNum
     * @return
     */
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

            BigDecimal amount = new BigDecimal(jedis.get(keyGenerator.getKeyById(id, RedisPriorityQueueKeyEnum.AMOUNT_KEY)));
            Transaction transaction=jedis.multi();
            transaction.zadd(queueName, prio, id);
            //修改优先级，子队列也得修改
            insertProgressQueue(id,priorityNum,amount,transaction);
            transaction.exec();
            return  true;
        } catch (Exception e) {
            LOG.error("unitId:{} changePriority error, queue:{}", unitId, oriName,e);
            return false;
        }

    }



    /**
     * 允许误差
     */
    private static final BigDecimal MIN_VAL=BigDecimal.valueOf(0.01);
    /**
     * 保留两位
     */
    private static final Integer COMPARE_SCALE=2;

    /**
     * 队列单元部分取消金额
     * 如果队列单元金额为0，则在队列种删除
     * 允许一分钱误差
     * @param unitId 队列单元id
     * @param amount 取消金额
     */
    public boolean cancel(String unitId, BigDecimal amount){
        try (Jedis jedis = jedisPool.getResource()) {
            QueueUnit unit = getUnitByUnitId(unitId);
            String id = keyGenerator.getKey(unitId, RedisPriorityQueueKeyEnum.ID_KEY);
            String amountKey = keyGenerator.getKeyById(id, RedisPriorityQueueKeyEnum.AMOUNT_KEY);

            BigDecimal currentAmount = new BigDecimal(jedis.get(amountKey)) ;

            /*
             * 如果金额相等，调用取消方法
             */
            BigDecimal remainAmount = currentAmount.subtract(amount);
            if(currentAmount.compareTo(amount) ==0
                    || remainAmount.setScale(COMPARE_SCALE,RoundingMode.DOWN).abs().compareTo(MIN_VAL)==0){
                return  cancel(unitId);
            }

            if(currentAmount.compareTo(amount) <0){
                LOG.error("cancel with amount  fail! unit amount is " +
                        "less than given unitId:{},unit amount:{},given amount:{} ",unitId,currentAmount.toString(),amount.toString());
                return false;
            }

            Transaction transaction = jedis.multi();

            transaction.set(amountKey,remainAmount.setScale(COMPARE_SCALE,RoundingMode.DOWN).toString());
            deleteProgress(id,currentAmount,transaction);
            insertProgressQueue(id,unit.getPriorityNum(),remainAmount,transaction);
            transaction.exec();

            return true;



        }catch (Exception e){
            LOG.error("cancel with amount  fail! unitId: "+ unitId, e);
            return false;
        }

    }





    /**
     * 初始化进度条队列
     */
    public void initProgressQueue() {
        try (Jedis jedis = jedisPool.getResource()) {

            Set<Tuple> idSet = jedis.zrangeWithScores(queueName, 0, jedis.zcard(queueName));
            List<QueueUnit> unitList = new ArrayList<>();

            BigDecimal beforeAmount = BigDecimal.ZERO;

            for (Tuple tuple : idSet) {
                QueueUnit unit = new QueueUnit();
                unit.setId(tuple.getElement());
                BigDecimal amount = new BigDecimal(jedis.get(keyGenerator.getKeyById(tuple.getElement(), RedisPriorityQueueKeyEnum.AMOUNT_KEY)));
                unit.setAmount(amount);
                unit.setBeforeAmount(beforeAmount.add(BigDecimal.ZERO));
                unit.setPriorityNum(Double.valueOf(tuple.getScore()).intValue());
                beforeAmount = beforeAmount.add(amount);
                unitList.add(unit);
            }
            int index = 1;
            int total = unitList.size();
            for (QueueUnit queueUnit : unitList) {
                LOG.info("refreshProgress unit:{} progress:{}/{}", RedisQueueUtil.getUnitId(queueUnit.getId(), queueName), index, total);
                insertProgressQueue(queueUnit.getId(), Double.valueOf(queueUnit.getPriorityNum()), queueUnit.getAmount(), jedis);
                index++;
            }


        } catch (Exception e) {
            LOG.error("refreshProgress error", e);
        }
    }




}
