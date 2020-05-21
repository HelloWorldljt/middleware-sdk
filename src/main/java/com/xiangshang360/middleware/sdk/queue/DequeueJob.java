package com.xiangshang360.middleware.sdk.queue;

import com.xiangshang360.middleware.sdk.lock.DistributedLockEnum;
import com.xiangshang360.middleware.sdk.lock.redis.RedisDistributedLockSupport;
import org.apache.commons.lang3.time.StopWatch;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 出队JOB
 * 执行数据出队业务逻辑
 * @author 段子彧
 * @date 20180810
 */
public class DequeueJob implements Job {

    private static final Logger LOG = LoggerFactory
            .getLogger(DequeueJob.class);

    public DequeueHandler getDequeueHandler() {
        return dequeueHandler;
    }

    public void setDequeueHandler(DequeueHandler dequeueHandler) {
        this.dequeueHandler = dequeueHandler;
    }

    public PriorityQueue getPriorityQueue() {
        return priorityQueue;
    }

    public void setPriorityQueue(PriorityQueue priorityQueue) {
        this.priorityQueue = priorityQueue;
    }

    private DequeueHandler dequeueHandler;

    private PriorityQueue priorityQueue;





    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        new DequeueDealer(priorityQueue,dequeueHandler).execute();



    }
}
