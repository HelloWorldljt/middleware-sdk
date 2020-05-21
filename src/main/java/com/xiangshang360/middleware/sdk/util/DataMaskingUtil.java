package com.xiangshang360.middleware.sdk.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: guochaohui
 * @Description: 数据脱敏工具类
 * @Since:
 */
public class DataMaskingUtil {

    /**
     * 隐藏信息
     * @param info
     * @param prefix
     * @param suffix
     * @param maskChar
     * @return
     */
    public static String hideInfo(String info, int prefix, int suffix, char maskChar) {
        if (StringUtils.isBlank(info)) {
            return "";
        }
        String result = StringUtils.left(info, prefix);
        result = StringUtils.rightPad(result, info.length() - suffix, maskChar);
        result += StringUtils.right(info, suffix);
        return result;
    }

    /**
     * @Description:  隐藏银行卡号中间的字符串（使用*号），显示前四后四
     * @Author: guochaohui
     * @param cardNo
     * @return: java.lang.String
     */
    public static String hideBankCardNo(String cardNo) {
        return hideInfo(cardNo, 4, 4, '*');
    }

    /**
     * @Description:  隐藏身份证号中间的字符串（使用*号），显示前三后三
     * @Author: guochaohui
     * @param idCardNo
     * @return: java.lang.String
     */
    public static String hideIdCardNo(String idCardNo) {
        return hideInfo(idCardNo, 3, 3, '*');
    }
    /**
     * @Description:  隐藏手机号中间位置字符，显示前三后四个字符
     * @Author: guochaohui
     * @param phoneNo
     * @return: java.lang.String
     */
    public static String hidePhoneNo(String phoneNo) {
        return hideInfo(phoneNo, 3, 4, '*');
    }

    /**
     * @description 隐藏邮箱账号中间的字符串（使用*号），显示前三后四
     * @author guochaohui
     * @date 2018/10/11 19:45
     * @param email
     * @return
     */
    public static String hideEmail(String email) {
        return hideInfo(email, 3, 4, '*');
    }

    /**
     * @Description: [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     * @Author: guochaohui
     * @Date: 2018/5/14 15:01
     * @param fullName
     * @return: java.lang.String
     */
    public static String chineseName(String fullName) {
        return hideInfo(fullName, 1, 0, '*');
    }
    /**
     * @Description: [地址] 只显示到地区，不显示详细地址；对个人信息增强保护<例子：北京市海淀区****>
     * @Author: lizhicheng
     * @Date: 2018/5/14 14:45
     * @param address 地址
     * @param sensitiveSize 敏感信息长度
     * @return: java.lang.String
     */
    public static String hideAddress(String address, int sensitiveSize) {

        if (StringUtils.isBlank(address)) {
            return "";
        }
        int length = StringUtils.length(address);

        return StringUtils.rightPad(StringUtils.left(address, length - sensitiveSize), length, "*");

    }
    public static String hideAddress(String address) {

        if (StringUtils.isBlank(address)) {
            return "";
        }
        int length = StringUtils.length(address);

        return StringUtils.rightPad(StringUtils.left(address, 3), length, "*");

    }

    public static void main(String[] args) {
        System.out.println(hideIdCardNo("6225881953502183"));
        System.out.println(hideBankCardNo("15222319871204739X"));
        System.out.println(hidePhoneNo("18504164663"));
        System.out.println(hideEmail("18504164663@qq.com"));
        System.out.println(chineseName("尔朱利龙"));
    }
}

