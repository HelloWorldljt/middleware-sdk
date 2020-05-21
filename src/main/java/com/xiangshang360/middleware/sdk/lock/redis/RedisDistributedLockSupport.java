package com.xiangshang360.middleware.sdk.lock.redis;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.xiangshang360.middleware.redis.JedisPoolFactory;
import com.xiangshang360.middleware.sdk.lock.DistributedLockEnum;
import com.xiangshang360.middleware.sdk.lock.DistributedLockSupport;

/**
 * 这里实现一个相对简单的分布式锁.基于Redis,客户端采用Jedis<BR>
 * 因为使用Jedis将来和SpringBoot整合相对简单,也可以使用https://github.com/redisson/redisson,
 * 开源Redis客户端实现
 * 
 * @author YuanZhiQiang
 *
 */
public class RedisDistributedLockSupport implements DistributedLockSupport {

	private static final Logger LOG = LoggerFactory
			.getLogger(RedisDistributedLockSupport.class);

	private JedisPool jedisPool;
	/**
	 * 分布式锁的占用超时时间,单位:秒 过时间后锁自动释放
	 */
	private long lockAcquireSeconds;

	public RedisDistributedLockSupport() {
		// 默认锁的占用时间5秒，过时间后锁自动释放
		this(5L);
	}

	public RedisDistributedLockSupport(long lockAcquireSeconds) {
		super();
		this.lockAcquireSeconds = lockAcquireSeconds;
		try {
			this.jedisPool = JedisPoolFactory.getJedisPool();
		} catch (ConfigurationException | IOException e) {
			LOG.error("", e);
		}
	}

	@Override
	public boolean acquire(DistributedLockEnum lockEnum, String... keyParts) {
		if (lockEnum == null) {
			throw new RuntimeException("lockEnum must not null.");
		}
		String uuid = UUID.randomUUID().toString();
		String key = DistributedLockEnum.generateDistributedKey(lockEnum,
				keyParts);
		LOG.info(
				"acquire key. uuid:[{}], key:[{}], timeout:[{}], timeUnit:[{}]",
				uuid, key, lockEnum.getTimeout(), lockEnum.getTimeUnit()
						.toString());
		boolean hasAcquire = tryAcquire(key, uuid, lockEnum.getTimeout(),
				lockEnum.getTimeUnit());
		LOG.info(
				"acquire key. uuid:[{}], key:[{}], timeout:[{}], timeUnit:[{}], hasAcquire:[{}]",
				uuid, key, lockEnum.getTimeout(), lockEnum.getTimeUnit()
						.toString(), hasAcquire);
		return hasAcquire;
	}

	@Override
	public void release(DistributedLockEnum lockEnum, String... keyParts) {
		if (lockEnum == null) {
			throw new RuntimeException("lockEnum must not null.");
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String key = DistributedLockEnum.generateDistributedKey(lockEnum,
					keyParts);
			unlock(jedis, key);
		} catch (Exception e) {
			LOG.error("", e);
		} finally {
			close(jedis);
		}

	}

	/**
	 * 尝试获取分布式锁,在一段时间内尝试获取分布式锁,每次尝试完后等待ACQUIRE_WAIT_MS设定的毫秒数,如果timeout==0
	 */
	private boolean tryAcquire(String key, String uuid, long timeout,
			TimeUnit timeUnit) {
		boolean acquired = false;
		// 尝试获取锁的毫秒数：在此时间内随机10-50毫秒重新尝试获取锁
		long totalWaitMs = timeUnit == TimeUnit.MILLISECONDS ? timeout
				: timeUnit.toMillis(timeout);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			while (totalWaitMs >= 0) {
				acquired = isMine(key, uuid, jedis) || locked(key, uuid, jedis);
				if (acquired) {
					// 已经拿到锁
					break;
				} else {
					// 未拿到锁，随机等待毫秒数后重新尝试
					int thisWaitMs = RandomUtils.nextInt(10, 50);
					totalWaitMs = totalWaitMs - thisWaitMs;
					if (totalWaitMs > 0) {
						try {
							TimeUnit.MILLISECONDS.sleep(thisWaitMs);
						} catch (InterruptedException e) {
							LOG.error("", e);
						}
					}
				}
			}
		} catch (Exception e) {
			LOG.error("", e);
		} finally {
			close(jedis);
		}
		return acquired;
	}

	private String value(String key, String uuid) {
		return new StringBuilder(key.length() + uuid.length() + 2).append(key)
				.append("::").append(uuid).toString();
	}

	private boolean isMine(String key, String uuid, Jedis jedis) {
		String value = jedis.get(key);
		return value != null && value.endsWith(uuid);
	}

	private boolean locked(String key, String uuid, Jedis jedis) {
		return setIfNotExists(key, uuid, jedis) != null;
	}

	private void unlock(Jedis jedis, String key) {
		jedis.del(key);
	}

	private String setIfNotExists(String key, String uuid, Jedis jedis) {
		return jedis.set(key, value(key, uuid), "nx", "ex", lockAcquireSeconds);
	}

	private void close(Jedis redis) {
		if (redis != null) {
			redis.close();
		}
	}

}