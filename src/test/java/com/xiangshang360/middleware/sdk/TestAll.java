package com.xiangshang360.middleware.sdk;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.xiangshang360.middleware.sdk.lock.redis.RedisDistributedLockSupportTest;
import com.xiangshang360.middleware.sdk.serial.redis.RedisSerialNumberSupportTest;

@RunWith(Suite.class)
@SuiteClasses({ RedisSerialNumberSupportTest.class,
		RedisDistributedLockSupportTest.class })
public class TestAll {

}
