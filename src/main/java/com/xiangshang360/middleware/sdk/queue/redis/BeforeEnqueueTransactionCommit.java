package com.xiangshang360.middleware.sdk.queue.redis;

import com.xiangshang360.middleware.sdk.queue.QueueUnit;
import redis.clients.jedis.Transaction;

/**
 * @author 段子彧
 * @date 20180914
 */
public interface BeforeEnqueueTransactionCommit {
    /**
     * 入队事务提交前执行
     * @param transaction reids 事务操作对象
     * @param unit 队列单元
     */
    void execute(QueueUnit unit, Transaction transaction);
}
