package com.xiangshang360.middleware.sdk.queue;

import com.xiangshang360.middleware.sdk.queue.redis.BeforeCancelTransactionCommit;
import com.xiangshang360.middleware.sdk.queue.redis.BeforeEnqueueTransactionCommit;

/**
 * 优先级队列
 * @author 段子彧
 * @date 20180810
 */
public interface PriorityQueue {
    /**
     * 入队
     * @param unit 队列单元
     * @return
     */
    boolean enqueue(QueueUnit unit);

    /**
     * 出队但是不ack
     * @return
     */
    QueueUnit dequeue();

    /**
     * 取消
     * @param unitId
     * @return
     */
    boolean cancel(String unitId);

    /**
     * 查询队列位置
     * @param unitId id
     * @return 当前位置
     */
    Long queryPosition(String unitId);

    /**\
     * 查询进度
     * @param unitId
     * @return
     */
    String queryProgress(String unitId);


    /**
     * ack队头单元
     * @param
     * @return
     */
    boolean ack(String unitId);


    /**
     * 修改单元优先级
     * @param unitId 单元id
     * @param priorityNum 优先级数字
     * @return
     */
    boolean changePriority(String unitId,Integer priorityNum);


    /**
     * 获取队列长度
     * @return
     */
    Long getQueueSize();


    /**
     * 获取队列名称
     * @return
     */
    String queueName();

    void addBeforeEnqueueTransaction(BeforeEnqueueTransactionCommit beforeEnqueueTransactionCommit);

    void addBeforeCancelTransaction(BeforeCancelTransactionCommit beforeCancelTransactionCommit);

    QueueUnit getUnitByUnitId(String unitId);



}
