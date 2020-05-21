package com.xiangshang360.middleware.redis;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 中间件SDK使用的Redis连接池配置,单例模式<br>
 * 注意:基础架构使用,其他业务不要使用
 * 
 * @author YuanZhiQiang
 *
 */
public class JedisPoolFactory {

	/**
	 * 中间件SDK配置文件名称
	 */
	private static final String configFile = "middleware-sdk-redis.properties";

	// 分布式锁和排队器使用的redis配置，这个在多套测试环境中需要根据业务走。各自连各自的。
	private static JedisPool jedisPool = null;

	// 序号生成功能使用的redis配置，因为多套测试环境中需要生成出不重复的序号，全部测试环境连一个。
	private static JedisPool serialJedisPool = null;

	public static JedisPool getJedisPool() throws ConfigurationException, IOException {
		if (jedisPool == null) {
			synchronized (JedisPoolFactory.class) {
				if (jedisPool == null) {
					initJedisPool();
				}
			}
		}
		return jedisPool;
	}

	public static JedisPool getSerialJedisPool() throws ConfigurationException, IOException {
		if (serialJedisPool == null) {
			synchronized (JedisPoolFactory.class) {
				if (serialJedisPool == null) {
					initSerialJedisPool();
				}
			}
		}
		return serialJedisPool;
	}

	// 初始化
	private static void initJedisPool() throws ConfigurationException, IOException {
		PropertiesConfiguration properties = new PropertiesConfiguration();
		properties.read(new InputStreamReader(JedisPoolFactory.class.getClassLoader().getResourceAsStream(configFile)));

		// Redis config
		String host = properties.getString("middleware.redis.host");
		int port = properties.getInt("middleware.redis.port");
		int timeout = properties.getInt("middleware.redis.timeout");
		String password = properties.getString("middleware.redis.password");
		int database = properties.getInt("middleware.redis.database");

		// Redis pool config
		int maxTotal = properties.getInt("middleware.redis.pool.maxTotal");
		int maxIdle = properties.getInt("middleware.redis.pool.maxIdle");
		long maxWaitMillis = properties.getLong("middleware.redis.pool.maxWaitMillis");
		boolean testOnBorrow = properties.getBoolean("middleware.redis.pool.testOnBorrow");
		boolean testWhileIdle = properties.getBoolean("middleware.redis.pool.testWhileIdle");

		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		config.setMaxWaitMillis(maxWaitMillis);
		config.setTestOnBorrow(testOnBorrow);
		config.setTestWhileIdle(testWhileIdle);

		jedisPool = new JedisPool(config, host, port, timeout, StringUtils.trimToNull(password), database);
	}
	
	private static void initSerialJedisPool() throws ConfigurationException, IOException {
		PropertiesConfiguration properties = new PropertiesConfiguration();
		properties.read(new InputStreamReader(JedisPoolFactory.class.getClassLoader().getResourceAsStream(configFile)));

		// Redis config
		String host = properties.getString("serial.redis.host");
		int port = properties.getInt("serial.redis.port");
		int timeout = properties.getInt("serial.redis.timeout");
		String password = properties.getString("serial.redis.password");
		int database = properties.getInt("serial.redis.database");

		// Redis pool config
		int maxTotal = properties.getInt("serial.redis.pool.maxTotal");
		int maxIdle = properties.getInt("serial.redis.pool.maxIdle");
		long maxWaitMillis = properties.getLong("serial.redis.pool.maxWaitMillis");
		boolean testOnBorrow = properties.getBoolean("serial.redis.pool.testOnBorrow");
		boolean testWhileIdle = properties.getBoolean("serial.redis.pool.testWhileIdle");

		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		config.setMaxWaitMillis(maxWaitMillis);
		config.setTestOnBorrow(testOnBorrow);
		config.setTestWhileIdle(testWhileIdle);

		serialJedisPool = new JedisPool(config, host, port, timeout, StringUtils.trimToNull(password), database);
	}

}
