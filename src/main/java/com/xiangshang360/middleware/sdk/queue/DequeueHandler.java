package com.xiangshang360.middleware.sdk.queue;

/**
 * 出队处理器
 * @author 段子彧
 * @date 20180810
 */
public interface DequeueHandler {

    /**
     * 出队处理器
     * @param unit
     * @return 如果返回true，ack出队数据
     */
    boolean handler(QueueUnit unit);

    /**
     * 出队条件。如果为true，执行handler
     * @return
     */
    @Deprecated
    boolean condition();

    /**
     * 出队条件。如果为true，执行handler
     * @param queueUnit
     * @return
     */
    boolean condition(QueueUnit queueUnit);

    /**
     * 一次task执行完后调用
     */
    void finish();
}
