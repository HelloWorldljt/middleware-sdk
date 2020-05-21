package com.xiangshang360.middleware.sdk.queue.redis;

import com.xiangshang360.middleware.sdk.queue.QueueUnit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 基于 redis sortedSet 的 优先级队列
 * 进度条更新依赖刷新操作
 * @author 段子彧
 * @date 20180810
 */
public class RedisPriorityQueue extends BasePriorityQueue implements RefreshableProgress {

    private static final Logger LOG = LoggerFactory
            .getLogger(RedisPriorityQueue.class);

    public RedisPriorityQueue(String queueName) {
        super(queueName);

    }





    /**
     * 修改进度条，仅供老数据初始化使用
     *
     * @param unitId
     * @param progress
     */
    public void changeProgress(String unitId, double progress) {
        try (Jedis jedis = jedisPool.getResource()) {


            String progressKey = keyGenerator.getKey(unitId, RedisPriorityQueueKeyEnum.PROGRESS_KEY);

            String progressStr = jedis.get(progressKey);


            if (StringUtils.isEmpty(progressStr)) {
                jedis.set(progressKey, String.valueOf(progress));
                return;
            }

            Double oriProgress = Double.valueOf(progressStr);

            if (progress > oriProgress) {

                jedis.set(progressKey, BigDecimal.valueOf(progress).setScale(4, RoundingMode.HALF_EVEN).toString());
            }

        } catch (Exception e) {
            LOG.error("change progress fail! unitId:{} ", unitId, e);

        }
    }


    /**
     * 刷新队列进度条
     */
    @Override
    public void refreshProgress() {
        try (Jedis jedis = jedisPool.getResource()) {


            Set<String> idSet = jedis.zrange(queueName, 0, jedis.zcard(queueName));
            List<QueueUnit> unitList = new ArrayList<>();


            BigDecimal beforeAmount = BigDecimal.ZERO;

            for (String id : idSet) {
                QueueUnit unit = new QueueUnit();
                unit.setId(id);
                BigDecimal amount = new BigDecimal(jedis.get(keyGenerator.getKeyById(id, RedisPriorityQueueKeyEnum.AMOUNT_KEY)));
                unit.setAmount(amount);
                unit.setBeforeAmount(beforeAmount.add(BigDecimal.ZERO));
                beforeAmount = beforeAmount.add(amount);
                unitList.add(unit);
            }
            for (QueueUnit queueUnit : unitList) {
                setProgress(queueUnit, beforeAmount);
            }

        } catch (Exception e) {
            LOG.error("refreshProgress error", e);
        }
    }


    /**
     * 设置进度条
     *
     * @param queueUnit
     * @param totalAmount
     */
    private void setProgress(QueueUnit queueUnit, BigDecimal totalAmount) {
        String progressStr = "0.01";
        if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal progress = totalAmount.subtract(queueUnit.getBeforeAmount()).divide(totalAmount, 4, RoundingMode.HALF_EVEN);

            progressStr = progress.toString();

            if (progress.compareTo(BigDecimal.ONE) >= 0) {
                progressStr = "0.9999";
            }
            String progressKey = keyGenerator.getKeyById(queueUnit.getId(), RedisPriorityQueueKeyEnum.PROGRESS_KEY);
            try (Jedis jedis = jedisPool.getResource()) {

                Double oriProgress = Double.valueOf(jedis.get(progressKey));

                if (progress.doubleValue() > oriProgress) {
                    jedis.set(progressKey, progressStr);
                }

            } catch (Exception e) {

                LOG.error("setProgress error!", e);
            }

        }
    }

    @Deprecated
    public void setLimitAmount(String amount) {

    }

    @Deprecated
    public String getLimitAmount() {
        return "not supported";
    }


}
