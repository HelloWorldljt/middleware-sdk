package com.xiangshang360.middleware.sdk.queue.redis;

/**
 * @author 段子彧
 * @date 20180914
 */
public class RedisQueueUtil {

    /**
     * 获取原始ID
     *
     * @param redisId
     * @return
     */
    public static String  getUnitId(String redisId ,String redisQueueName) {


        return redisId.substring(redisId.indexOf(redisQueueName + "_") + (redisQueueName + "_").length());
    }

    public static   Double convertPriorityNum(Double priorityNum) {
        return -priorityNum;
    }
}
