package com.xiangshang360.middleware.config;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 中间件SDK使用的配置属性加载,单例模式<br>
 * 注意:基础架构使用,其他业务不要使用
 * 
 * @author YuanZhiQiang
 *
 */
public class ConfigFactory {

	private static final Logger LOG = LoggerFactory.getLogger(ConfigFactory.class);

	/**
	 * 中间件SDK配置文件名称
	 */
	private static final String configFile = "middleware-sdk-props.properties";

	/**
	 * 序号初始值，用来区分多环境，例如环境一从1开始，环境2从10000000开始（仅仅用在测试环境，正式环境应该一套，且用1开始）
	 */
	private static long serialIncrStartValue = 1;
	
	/**
	 * 加载配置
	 */
	private static PropertiesConfiguration properties;

	static {
		try {
			loadProps();
		} catch (ConfigurationException e) {
			LOG.error("", e);
		} catch (IOException e) {
			LOG.error("", e);
		}
	}

	// 初始化
	private static void loadProps() throws ConfigurationException, IOException {
		properties = new PropertiesConfiguration();
		properties.read(new InputStreamReader(ConfigFactory.class.getClassLoader().getResourceAsStream(configFile)));

		serialIncrStartValue = properties.getLong("serial.incr.start.value", 1l);

	}

	public static long getSerialIncrStartValue() {
		return serialIncrStartValue;
	}
	
	/**
	 * 通过key获取属性值，返回String类型属性值
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		return properties.getString(key);
	}
	
	/**
	 *  通过key获取属性值，返回int类型属性值
	 * @param key
	 * @return
	 */
	public static int getInt(String key) {
		return properties.getInt(key);
	}

}
