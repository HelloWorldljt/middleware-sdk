package com.xiangshang360.middleware.sdk.serial.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

import com.xiangshang360.middleware.sdk.serial.SerialNumberEnum;

public class RedisSerialNumberSupportTest {

	private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 10, 3, TimeUnit.SECONDS,
			new ArrayBlockingQueue<Runnable>(100), new ThreadPoolExecutor.CallerRunsPolicy());

	@Test
	public void generateForTest() throws InterruptedException, ExecutionException {
		final RedisSerialNumberSupport jis = new RedisSerialNumberSupport();
		String serialNumber = jis.generate(SerialNumberEnum.FOR_TEST, 30);
		System.out.println("FOR_TEST:" + serialNumber);
	}

	@Test
	public void generateLFOrderId() throws InterruptedException, ExecutionException {
		final RedisSerialNumberSupport jis = new RedisSerialNumberSupport();
		long serialNumber = jis.generate(SerialNumberEnum.BANK_DESPOSITORY_ORDER_ID);
		System.out.println("BANK_DESPOSITORY_ORDER_ID:" + serialNumber);
	}

	@Test
	public void generateEverydayIncrThreadTest() throws InterruptedException, ExecutionException {
		final RedisSerialNumberSupport jis = new RedisSerialNumberSupport();
		StopWatch sw = new StopWatch();
		sw.start();
		List<Future<Long>> futures = new ArrayList<Future<Long>>();
		for (int i = 0; i < 1000; i++) {
			Future<Long> submit = threadPool.submit(new Callable<Long>() {

				@Override
				public Long call() throws Exception {
					return jis.generate(SerialNumberEnum.FOR_TEST);
				}
			});
			futures.add(submit);
		}
		for (Future<Long> future : futures) {
			Object object = future.get();
			System.out.println(object);
		}
		sw.stop();
		System.out.println("cost time:" + sw.getTime() + " milliseconds.");
	}

	@Test
	public void generateStartValue() throws InterruptedException, ExecutionException {
		// 初始值从xx开始，后续步长为1自增
		final RedisSerialNumberSupport jis = new RedisSerialNumberSupport();
		long generateDateBegin = jis.generateDateBegin(SerialNumberEnum.FOR_TEST);
		long generateDateBegin2 = jis.generateDateBegin(SerialNumberEnum.FOR_TEST);
		System.out.println("generateDateBegin:" + generateDateBegin);
		System.out.println("generateDateBegin2:" + generateDateBegin2);
	}

	@Test
	public void testGetIncrKey() {
		final RedisSerialNumberSupport jis = new RedisSerialNumberSupport();
		String id = jis.getIncrKey(SerialNumberEnum.FOR_TEST, 6);
		System.out.println("id1:"+id);
		Assert.assertTrue("id length must 6", StringUtils.length(id) == 6);

		id = jis.getIncrKey(SerialNumberEnum.FOR_TEST, 4);
		System.out.println("id2:"+id);
		Assert.assertTrue("id length must 4", StringUtils.length(id) == 4);
	}
}
