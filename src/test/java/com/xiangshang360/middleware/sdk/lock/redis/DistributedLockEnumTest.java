package com.xiangshang360.middleware.sdk.lock.redis;

import org.junit.Assert;
import org.junit.Test;

import com.xiangshang360.middleware.sdk.lock.DistributedLockEnum;

public class DistributedLockEnumTest {

	@Test
	public void testGenerateDistributedKey() {
		String key = DistributedLockEnum
				.generateDistributedKey(DistributedLockEnum.FOR_TEST_NO_TRY);
		Assert.assertEquals("SDK-LOCK-FOR_TEST_NO_TRY", key);

		key = DistributedLockEnum.generateDistributedKey(
				DistributedLockEnum.FOR_TEST_NO_TRY, "part1");
		Assert.assertEquals("SDK-LOCK-FOR_TEST_NO_TRY-part1", key);

		key = DistributedLockEnum.generateDistributedKey(
				DistributedLockEnum.FOR_TEST_NO_TRY, "part1", "part2", "part3");
		Assert.assertEquals("SDK-LOCK-FOR_TEST_NO_TRY-part1-part2-part3", key);
	}
}
