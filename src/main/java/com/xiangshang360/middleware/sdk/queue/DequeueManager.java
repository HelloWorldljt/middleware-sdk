package com.xiangshang360.middleware.sdk.queue;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 这种方式不好管理定时任务，弃用，建议使用DequeueDealer
 * 出队管理器
 * @author 段子彧
 * @date 20180810
 */
@Deprecated
public class DequeueManager  {
    private static final Logger LOG = LoggerFactory
            .getLogger(DequeueManager.class);
    /**
     * 队列操作
     */
    private PriorityQueue priorityQueue;

    /**
     * 定时表达式
     */
    private String cron;

    /**
     * 出队操作
     */
    private DequeueHandler dequeueHandler;



    public DequeueManager(PriorityQueue priorityQueue, String cron, DequeueHandler dequeueHandler) {
        this.priorityQueue = priorityQueue;
        this.cron = cron;
        this.dequeueHandler = dequeueHandler;


    }

    public void register(){
        JobDataMap jobDataMap= new JobDataMap();
        jobDataMap.put("priorityQueue",priorityQueue);
        jobDataMap.put("dequeueHandler",dequeueHandler);
        JobDetail job = newJob(DequeueJob.class).withIdentity(priorityQueue.queueName()+"_job","queue")
                .setJobData(jobDataMap).storeDurably(true).build();
         Trigger  trigger = newTrigger()
                .withIdentity(priorityQueue.queueName(), "queue")
                .withSchedule(cronSchedule(cron))
                .forJob(priorityQueue.queueName()+"_job", "queue")
                .build();

        Scheduler sched = null;
        try {
            sched = StdSchedulerFactory.getDefaultScheduler();
            sched.scheduleJob(job, trigger);
            sched.start();
            LOG.info("dequeueManager init start,queue:{}",priorityQueue.queueName());
        } catch (SchedulerException e) {
           LOG.error("dequeueManager init error");
        }

    }
}
