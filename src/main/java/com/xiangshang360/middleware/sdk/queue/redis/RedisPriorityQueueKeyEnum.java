package com.xiangshang360.middleware.sdk.queue.redis;

/**
 * redis 队列 key枚举
 * @author 段子彧
 * @date 20180914
 */
public enum RedisPriorityQueueKeyEnum {
    /**
     * 队列id
     */
    ID_KEY,

    ADD_ID_KEY,
    /**
     * 进度条队列id
     */
    PROGRESS_ID_KEY,

    /**
     * 金额key
     */
    AMOUNT_KEY,

    /**
     * 原始位置key
     */
    ORI_POSITION_KEY,

    /**
     *进度条key
     */
    PROGRESS_KEY;





}
