package com.xiangshang360.middleware.sdk.queue;

import com.xiangshang360.middleware.sdk.lock.DistributedLockEnum;
import com.xiangshang360.middleware.sdk.lock.redis.RedisDistributedLockSupport;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 出队业务逻辑
 * @author 段子彧
 * @date 20180903
 */
public class DequeueDealer {

    private DequeueHandler dequeueHandler;

    private PriorityQueue priorityQueue;

    private static final Logger LOG = LoggerFactory
            .getLogger(DequeueDealer.class);

    public DequeueDealer( PriorityQueue priorityQueue,DequeueHandler dequeueHandler) {
        this.dequeueHandler = dequeueHandler;
        this.priorityQueue = priorityQueue;
    }

    public void execute(){
        RedisDistributedLockSupport lock = new RedisDistributedLockSupport(3600);

        boolean isLock=false;
        try {
            isLock= lock.acquire(DistributedLockEnum.DEQUEUE_MANAGER_LOCK,priorityQueue.queueName());
            if(isLock){
                LOG.info("{} dequeueManager running",priorityQueue.queueName());
                StopWatch sw = new StopWatch();
                sw.start();
                QueueUnit queueUnit= priorityQueue.dequeue();
                if (queueUnit == null) {
                    LOG.info("{} has no unit exist dequeueManager finished,cost:{}ms",priorityQueue.queueName(),sw.getTime());
                    return;
                }
                while (dequeueHandler.condition(queueUnit)){

                    LOG.info("dequeue,queue:{},{}",priorityQueue.queueName(),queueUnit);
                    boolean success=  dequeueHandler.handler(queueUnit);

                    //消息被成功消费，出队
                    if(success){
                        priorityQueue.ack(queueUnit.getId());
                        LOG.info("ack:{}",queueUnit);
                        queueUnit=priorityQueue.dequeue();
                        if (queueUnit == null) {
                            LOG.info("{} has no unit exist dequeueManager finished,cost:{}ms",priorityQueue.queueName(),sw.getTime());
                            break;
                        }
                    }else {
                        LOG.error("dequeue fail ,canceled,queue:{},unit:{}",priorityQueue.queueName(),queueUnit);
                        break;
                    }
                }
                LOG.info("{} dequeueManager enqueue finish invoke finish()",priorityQueue.queueName());
                dequeueHandler.finish();
                LOG.info("{} dequeueManager enqueue finish invoke finish()，over",priorityQueue.queueName());

                sw.stop();
                LOG.info("{} dequeueManager finished,cost:{}ms",priorityQueue.queueName(),sw.getTime());


            }else{
                LOG.info("{} not acquire lock, canceled",priorityQueue.queueName());
            }
        }catch (Exception e){
            LOG.error("dequeue fail ,canceled,queue:"+priorityQueue.queueName(),e);
        }finally {
            if(isLock){
                lock.release(DistributedLockEnum.DEQUEUE_MANAGER_LOCK,priorityQueue.queueName());

            }
        }
    }
}
