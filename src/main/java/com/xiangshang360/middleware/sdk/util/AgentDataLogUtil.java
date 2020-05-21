package com.xiangshang360.middleware.sdk.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 埋点日志工具类
 * @author typ
 */
public class AgentDataLogUtil {

    private static final Logger LOG = LoggerFactory.getLogger("agentData");

    /**
     * 记录日志
     *
     * @param businessType 业务类型
     * @param businessId   业务ID
     * @param logMessage
     * @param params
     * @param <T>
     * @return
     */
    public static <T> void template(String businessType, Integer businessId, String logMessage, T params) {
        List<String> logContent = new ArrayList<String>();
        logContent.add("");
        logContent.add(businessType == null ? "" : businessType);
        logContent.add(String.valueOf(businessId));
        logContent.add(logMessage == null ? "" : logMessage);
        logContent.add(params == null ? "" : ToStringBuilder.reflectionToString(params));
        LOG.info(StringUtils.join(logContent, "|"));
    }

    /**
     * 记录日志
     *
     * @param businessType 业务类型
     * @param businessId   业务ID
     * @param logMessage
     * @param params
     * @return
     */
    public static void templateToString(String businessType, String businessId, String logMessage, String params) {
        List<String> logContent = new ArrayList<String>();
        logContent.add("");
        logContent.add(businessType == null ? "" : businessType);
        logContent.add(String.valueOf(businessId));
        logContent.add(logMessage == null ? "" : logMessage);
        logContent.add(params == null ? "" : params);
        LOG.info(StringUtils.join(logContent, "|"));
    }
}
