package com.xiangshang360.middleware.sdk.serial.redis;

import com.xiangshang360.middleware.config.ConfigFactory;
import com.xiangshang360.middleware.redis.JedisPoolFactory;
import com.xiangshang360.middleware.sdk.serial.SerialNumberEnum;
import com.xiangshang360.middleware.sdk.serial.SerialNumberSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.List;

/**
 * 分布式主键生成服务,用Redis实现主键自增服务,使用必须连接同一个相同的Redis源.
 * 
 * @author YuanZhiQiang
 *
 */
public class RedisSerialNumberSupport implements SerialNumberSupport {

	private static final Logger LOG = LoggerFactory.getLogger(RedisSerialNumberSupport.class);

	/**
	 * ID日期域的格式
	 */
	private static final String DATE_REGION_PATTERN = "yyyyMMdd";

	/**
	 * 每日自增序列KEY的有效期（小时）
	 */
	private static final int INCRKEY_EXPIRE_HOURS = 30;

	/**
	 * 每日自增序列的统一前缀
	 */
	private static final String UNIFIED_PREFIX = "SDK-SN-";
	/**
	 * 最小长度
	 */
	static final Integer MIN_NUMBER_LEN = 6;

	private static JedisPool jedisPool;

	public RedisSerialNumberSupport() {
		super();
		try {
			jedisPool = JedisPoolFactory.getSerialJedisPool();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public long generate(SerialNumberEnum enumType) {
		return Long.parseLong(this.generate(enumType, enumType.getLen()));
	}

	@Override
	public String generate(SerialNumberEnum enumType, int length) {

		if (length < MIN_NUMBER_LEN) {
			length = MIN_NUMBER_LEN;
		}
		int prefix = enumType.getPrefix();
		checkPrefix(prefix);
		String redisServerDate = getRedisServerDate();
		StringBuilder sb = new StringBuilder();
		sb.append(prefix);// 101
		sb.append(redisServerDate);// 获取Redis当前日期.20170425
		sb.append(generateByIncrement(redisServerDate, enumType.name(), length));// 000000001
		return sb.toString();
	}

	@Override
	public long generateDateBegin(SerialNumberEnum enumType) {
		return Long.parseLong(this.generateDateBegin(enumType, enumType.getLen()));
	}

	@Override
	public String generateDateBegin(SerialNumberEnum enumType, int length) {

		if (length < MIN_NUMBER_LEN) {
			length = MIN_NUMBER_LEN;
		}
		int prefix = enumType.getPrefix();
		checkPrefix(prefix);
		String redisServerDate = getRedisServerDate();
		StringBuilder sb = new StringBuilder();
		// 获取Redis当前日期.20170425
		sb.append(redisServerDate);
		// 101
		sb.append(prefix);
		// 000000001
		sb.append(generateByIncrement(redisServerDate, enumType.name(), length));
		return sb.toString();
	}

	/**
	 * 检查前前缀设置是否正常,大于等于100因为数字前面不能为0,小于等于921是因为long的长度范围
	 * 
	 * @param prefix
	 */
	void checkPrefix(int prefix) {
		if (prefix < 100 || prefix > 921) {
			throw new RuntimeException("enum prefix rang is error. must in rang 100 to 921");
		}
	}

	// 生成自增域ID,并根据最终返回长度左侧拼0
	String generateByIncrement(String redisServerDate, String enumName, int length) {
		long id = generateIncrement(redisServerDate, enumName);
		if (id <= 0) {
			throw new RuntimeException("generate increment id error. id must large than 0. id:[" + id + "]");
		}
		String idString = String.valueOf(id);
		final int needPads = length - idString.length();
		if (needPads >= 0) {
			return StringUtils.leftPad(idString, length, '0');
		} else {
			throw new RuntimeException("id total length is too large. id:[" + id + "],length:[" + length + "]");
		}
	}

	// 生成每日自增ID
	private long generateIncrement(String redisServerDate, String enumName) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String incrKey = getIncrKey(redisServerDate, enumName);
			long id = -1l;// 默认-1代表没有取到
			if (!jedis.exists(incrKey)) {
				// id = jedis.incr(incrKey);
				long startValue = ConfigFactory.getSerialIncrStartValue();
				LOG.info("incryKey:[{}], incr start value:[{}]", incrKey, startValue);
				id = jedis.incrBy(incrKey, startValue);
				jedis.expire(incrKey, INCRKEY_EXPIRE_HOURS * 3600);// 30个小时
			} else {
				id = jedis.incr(incrKey);
			}
			return id;
		} finally {
			close(jedis);
		}
	}

	/**
	 * 获取Redis服务器当前日期，格式见DATE_REGION_PATTERN
	 * 
	 * @return
	 */
	String getRedisServerDate() {
		long current = getRedisServerMilliseconds();
		Date date = new Date(current);
		String result = DateFormatUtils.format(date, DATE_REGION_PATTERN);
		return result;
	}

	/**
	 * 获取每日自增序列的KEY
	 * 
	 * @param redisServerDate
	 * @param enumName
	 * @return
	 */
	private String getIncrKey(String redisServerDate, String enumName) {
		StringBuilder sb = new StringBuilder(UNIFIED_PREFIX);
		sb.append(redisServerDate);
		sb.append("-");
		sb.append(enumName);
		return sb.toString();
	}

	/**
	 * 获取Redis服务器当前毫秒数
	 * 
	 * @return
	 */
	private long getRedisServerMilliseconds() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<String> times = jedis.time();
			long milliseconds = Long.parseLong(new StringBuilder().append(times.get(0)).append(StringUtils.leftPad(times.get(1), 6, '0').substring(0, 3)).toString());
			return milliseconds;
		} finally {
			close(jedis);
		}
	}

	// 关闭Redis连接,如果用到连接池,则返回连接给连接池
	private void close(Jedis redis) {
		if (redis != null) {
			redis.close();
		}
	}

	@Override
	public String getIncrKey(SerialNumberEnum type, int incrLength) {
		String redisServerDate = getRedisServerDate();
		return generateByIncrement(redisServerDate, type.name(), incrLength);
	}

	
}
