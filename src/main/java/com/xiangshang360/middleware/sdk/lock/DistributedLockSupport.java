package com.xiangshang360.middleware.sdk.lock;

/**
 * 分布式锁服务
 * 
 * @author YuanZhiQiang
 *
 */
public interface DistributedLockSupport {

	/**
	 * 获取分布式锁
	 * 
	 * @param key
	 * @param owner
	 * @return
	 */
	public boolean acquire(DistributedLockEnum lockEnum, String... keyParts);

	/**
	 * 释放分布式锁
	 * 
	 * @param key
	 * @param owner
	 */
	public void release(DistributedLockEnum lockEnum, String... keyParts);

}