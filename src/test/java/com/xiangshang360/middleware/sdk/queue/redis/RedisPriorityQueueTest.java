package com.xiangshang360.middleware.sdk.queue.redis;

import com.xiangshang360.middleware.sdk.queue.DequeueDealer;
import com.xiangshang360.middleware.sdk.queue.DequeueHandler;
import com.xiangshang360.middleware.sdk.queue.QueueUnit;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 段子彧
 * @date 20180810
 */
public class RedisPriorityQueueTest {

    private AtomicInteger atomicInteger=new AtomicInteger(0);
    @Test
    public void test1(){
        RedisProgressPriorityQueue queue = new RedisProgressPriorityQueue("TRANSFER_WAITING");
        queue.changePriority("20180630091759687M7C",10);



    }
    @Test
    public  void test2(){
        RedisPriorityQueue queue = new RedisPriorityQueue("test");
        StopWatch stopWatch=new StopWatch();
        stopWatch.start();

        for(int i=0;i<1;i++){

            QueueUnit unit=new QueueUnit("unit"+atomicInteger.getAndAdd(1),"xxx", 0,new BigDecimal(RandomUtils.nextInt(1000,1000000)));
            System.out.println("enqueue:"+unit);
            queue.enqueue(unit);


            System.out.println("progress:"+queue.queryProgress(unit.getId()));

        }
        stopWatch.stop();
        System.out.println(stopWatch.getTime()+" ms");
    }

    @Test
    public void test3(){
        RedisPriorityQueue queue = new RedisPriorityQueue("test");
        System.out.println(queue.queryProgress("unit1"));
        queue.refreshProgress();
        System.out.println(queue.queryProgress("unit1"));
        System.out.println(queue.queryProgress("unit10"));
        System.out.println(queue.getUnitByUnitId("unit1"));
        System.out.println(queue.getUnitByUnitId("unit10"));
    }

    @Test
    public void testCron(){
        final RedisProgressPriorityQueue queue = new RedisProgressPriorityQueue("test");
        DequeueDealer dequeueDealer=new DequeueDealer(queue, new DequeueHandler() {
            @Override
            public boolean handler(QueueUnit unit) {
                if(unit.getId().equals("unit0")){

                    return true;
                }
                return false;
            }

            @Override
            public boolean condition() {
                return true;
            }

            @Override
            public boolean condition(QueueUnit queueUnit) {
                return true;
            }

            @Override
            public void finish() {

            }
        });
        dequeueDealer.execute();


    }

    @Test
    public void testRefresh(){
        RedisProgressPriorityQueue queue = new RedisProgressPriorityQueue("test");
        StopWatch stopWatch=new StopWatch();
        stopWatch.start();

        stopWatch.stop();
        System.out.println(stopWatch.getTime()+"ms");
    }

    @Test
    public void testInit(){
        RedisProgressPriorityQueue queue = new RedisProgressPriorityQueue("TRANSFER_WAITING");
        StopWatch stopWatch=new StopWatch();
        stopWatch.start();
        queue.initProgressQueue();
        stopWatch.stop();
        System.out.println(stopWatch.getTime()+"ms");
    }

    @Test
    public void testCompare(){
        RedisProgressPriorityQueue queue = new RedisProgressPriorityQueue("TRANSFER_WAITING");

    }

    @Test
    public void testCancelAmount(){
        RedisProgressPriorityQueue queue = new RedisProgressPriorityQueue("test");
        for(int i=0;i<10;i++){


            queue.cancel("unit8",new BigDecimal("194883.01"));


        }
    }

    @Test
    public void testQueryProgress(){
        RedisProgressPriorityQueue queue = new RedisProgressPriorityQueue("test");

        System.out.println(queue.queryProgress("unit6"));
    }

    @Test
    public void testQueue1(){
        RedisPriorityQueue queue = new RedisPriorityQueue("TRANSFER_WAITING");
        System.out.println(queue.getQueueSize());
//        QueueUnit unit=new QueueUnit("unit"+atomicInteger.getAndAdd(2),"xxx", 0,new BigDecimal(RandomUtils.nextInt(1000,1000000)));
//
//        queue.enqueue(unit);
//        queue.refreshProgress();

        queue.changePriority("unit0",30);
        queue.refreshProgress();
        System.out.println(queue.queryProgress("unit0"));

    }

    @Test
    public void  testQueue2(){
        RedisPriorityQueue queue = new RedisPriorityQueue("test2");
        QueueUnit unit=new QueueUnit("unit"+atomicInteger.getAndAdd(2),"xxx", 0,new BigDecimal(RandomUtils.nextInt(1000,1000000)));
        System.out.println("enqueue:"+unit);
        queue.enqueue(unit);
    }






    @Test
    public void testParse(){
        System.out.println(Integer.parseInt("11.1"));
    }
}