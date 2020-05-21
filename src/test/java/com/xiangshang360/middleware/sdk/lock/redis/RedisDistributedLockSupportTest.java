package com.xiangshang360.middleware.sdk.lock.redis;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.xiangshang360.middleware.sdk.lock.DistributedLockEnum;

public class RedisDistributedLockSupportTest {

	private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 10, 3, TimeUnit.SECONDS,
			new ArrayBlockingQueue<Runnable>(6), new ThreadPoolExecutor.CallerRunsPolicy());

	@Test
	public void testLock() {
		String key = "lock1";
		RedisDistributedLockSupport lockSupport = new RedisDistributedLockSupport(10);
		boolean acquire = false;
		try {
			acquire = lockSupport.acquire(DistributedLockEnum.FOR_TEST_NO_TRY, key);
			Assert.assertTrue("testLock not acquire lock.", acquire);
			System.out.println("do business begin");
			TimeUnit.SECONDS.sleep(RandomUtils.nextInt(1, 3));
			System.out.println("do business end");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lockSupport.release(DistributedLockEnum.FOR_TEST_NO_TRY, key);
		}
	}

	@Test
	public void testMultithreadAcquire() throws InterruptedException {
		// owner不包含线程名称
		final String key = "lock2";
		for (int i = 0; i < 10; i++) {
			threadPool.execute(new AcquireTask(key, "owner1", i));
		}
		for (int i = 0; i < 10; i++) {
			threadPool.execute(new AcquireTask(key, "owner2", i));
		}
		for (int i = 0; i < 10; i++) {
			threadPool.execute(new AcquireTask(key, "owner3", i));
		}
	}

	@Test
	public void testMultithreadAcquire2() throws InterruptedException {
		// owner包含线程名称
		final String key = "lock3";
		for (int i = 0; i < 10; i++) {
			threadPool.execute(new AcquireTask(key, "owner1", i, true));
		}
		for (int i = 0; i < 10; i++) {
			threadPool.execute(new AcquireTask(key, "owner2", i, true));
		}
		for (int i = 0; i < 10; i++) {
			threadPool.execute(new AcquireTask(key, "owner3", i, true));
		}
	}

	@Test
	public void testMultithreadTryAcquire() throws InterruptedException {
		// owner不包含线程名称
		final String key = "lock4";
		for (int i = 0; i < 10; i++) {
			threadPool.execute(new TryAcquireTask(key, "owner1", i));
		}
		for (int i = 0; i < 10; i++) {
			threadPool.execute(new TryAcquireTask(key, "owner2", i));
		}
		for (int i = 0; i < 10; i++) {
			threadPool.execute(new TryAcquireTask(key, "owner3", i));
		}
	}

	@Test
	public void testMultithreadTryAcquire2() throws InterruptedException {
		// owner包含线程名称
		final String key = "lock5";
		for (int i = 0; i < 10; i++) {
			threadPool.execute(new TryAcquireTask(key, "owner1", i, true));
		}
		for (int i = 0; i < 10; i++) {
			threadPool.execute(new TryAcquireTask(key, "owner2", i, true));
		}
		for (int i = 0; i < 10; i++) {
			threadPool.execute(new TryAcquireTask(key, "owner3", i, true));
		}
	}

	class AcquireTask implements Runnable {

		private String key;

		public AcquireTask(String key, String owner, int i) {
			super();
			this.key = key;
		}

		public AcquireTask(String key, String owner, int i, boolean ownerHasthreadName) {
			super();
			this.key = key;
		}

		@Override
		public void run() {
			RedisDistributedLockSupport lockSupport = new RedisDistributedLockSupport(10);
			boolean acquire = false;
			try {
				acquire = lockSupport.acquire(DistributedLockEnum.FOR_TEST_NO_TRY, key);
				String keyy = DistributedLockEnum.generateDistributedKey(DistributedLockEnum.FOR_TEST_NO_TRY, key);
				if (acquire) {
					System.out.println("begin key:[" + keyy + "]");
					TimeUnit.MICROSECONDS.sleep(RandomUtils.nextInt(10, 20));
					System.out.println("end key:[" + keyy + "]");
				} else {
					System.out.println("not acquire key:[" + keyy + "]");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lockSupport.release(DistributedLockEnum.FOR_TEST_NO_TRY, key);
			}
		}

	}

	class TryAcquireTask implements Runnable {

		private String key;

		private String owner;

		private int i;

		private boolean ownerHasthreadName;

		public TryAcquireTask(String key, String owner, int i) {
			super();
			this.key = key;
			this.owner = owner;
			this.i = i;
		}

		public TryAcquireTask(String key, String owner, int i, boolean ownerHasthreadName) {
			super();
			this.key = key;
			this.owner = owner;
			this.i = i;
			this.ownerHasthreadName = ownerHasthreadName;
		}

		@Override
		public void run() {
			RedisDistributedLockSupport lockSupport = new RedisDistributedLockSupport(10);
			boolean acquire = false;
			try {
				acquire = lockSupport.acquire(DistributedLockEnum.FOR_TEST_WITH_TIMEOUT, key);
				if (acquire) {
					System.out.println("tryAcquire key:[" + key + "],owner:[" + owner + "],i:[" + i
							+ "],ownerHasthreadName:[" + ownerHasthreadName + "],thread:["
							+ StringUtils.rightPad(Thread.currentThread().getName(), 15) + "] begin.");
					try {
						TimeUnit.MICROSECONDS.sleep(RandomUtils.nextInt(10, 20));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("tryAcquire key:[" + key + "],owner:[" + owner + "],i:[" + i
							+ "],ownerHasthreadName:[" + ownerHasthreadName + "],thread:["
							+ StringUtils.rightPad(Thread.currentThread().getName(), 15) + "] end.");
				} else {
					System.out.println("tryAcquire key:[" + key + "],owner:[" + owner + "],i:[" + i
							+ "],ownerHasthreadName:[" + ownerHasthreadName + "],thread:["
							+ StringUtils.rightPad(Thread.currentThread().getName(), 15) + "] not acquire.");
				}
			} finally {
				lockSupport.release(DistributedLockEnum.FOR_TEST_WITH_TIMEOUT, key);
			}
		}

	}
}
