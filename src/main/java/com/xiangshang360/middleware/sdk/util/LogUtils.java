package com.xiangshang360.middleware.sdk.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志工具类
 * @ClassName: LogUtils
 * @date 2016年11月30日 下午1:14:45
 */
public class LogUtils {


	/**
	 * 记录日志
	 * @param businessType 业务类型
	 * @param businessId 业务ID
	 * @param logMessage
	 * @param params
	 * @param <T>
	 * @return
	 */
	public static <T> String template(String businessType, Integer businessId, String logMessage,T params) {
		return templateToObject(businessType, businessId != null ? String.valueOf(businessId) : null, logMessage, params);
	}

	/**
	 * 记录日志
	 * @param businessType 业务类型
	 * @param businessId 业务ID
	 * @param logMessage
	 * @param params
	 * @param <T>
	 * @return
	 */
	public static <T> String templateToObject(String businessType, String businessId, String logMessage,T params) {
		List<String> logContent = new ArrayList<String>();
		logContent.add("");
		logContent.add(businessType == null ? "" : businessType);
		logContent.add(String.valueOf(businessId));
		logContent.add(logMessage == null?"":logMessage);
		logContent.add(params == null?"": JSON.toJSONString(params));
		return StringUtils.join(logContent, "|");
	}

}
