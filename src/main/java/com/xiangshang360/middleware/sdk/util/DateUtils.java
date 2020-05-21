/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.xiangshang360.middleware.sdk.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	/**日期格式化为yyyyMMddHHmmssSSS**/
	public static final String DATE_PATTERN_SHORT = "yyyyMMdd";
	public static final String DATE_PATTERN_DATE = "yyyy-MM-dd";
	public static final String DATE_PATTERN_SEC_NUM = "yyyyMMddHHmmss";
	public static final String DATE_PATTERN_MSEC_NUM = "yyyyMMddHHmmssSSS";
    public static final String DATE_PATTERN_MILLISECOND = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PATTERN_DAY_CHINNESS_DEFAULT = "yyyy年MM月dd日";
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM",
		"yyyyMMddHHmmss"};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(CalendarUtil.getCurrentDate(), pattern);
	}
	
	/**
     * 将字符串转化为日期。 字符串格式("YYYY-MM-DD")。
     * 例如："2012-07-01"或者"2012-7-1"或者"2012-7-01"或者"2012-07-1"是等价的。
     */
    public static Date stringToDate(String str, String pattern) {
        Date dateTime = null;
        try {
            SimpleDateFormat formater = new SimpleDateFormat(pattern);
            dateTime = formater.parse(str);
        } catch (Exception e) {
            return null;
        }
        return dateTime;
    }
    
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(CalendarUtil.getCurrentDate(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(CalendarUtil.getCurrentDate(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(CalendarUtil.getCurrentDate(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(CalendarUtil.getCurrentDate(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(CalendarUtil.getCurrentDate(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(CalendarUtil.getCurrentDate(), "E");
	}

	/**
	 * 获取指定时间的小时
	 * @param date
	 * @return
	 */
	public static int getHour(Date date){
		Calendar time = Calendar.getInstance();
		time.setTime(date);
		return time.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
	
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	/**
	 * 获取传入日期的开始时间 00:00:00
	 */
	public static final Date getBeginTimeForThisTime(Date date) {
		Calendar calst = Calendar.getInstance();
		calst.setTime(date);
		calst.set(Calendar.HOUR_OF_DAY, 0);
		calst.set(Calendar.MINUTE, 0);
		calst.set(Calendar.SECOND, 0);
		calst.set(Calendar.MILLISECOND, 0);
		return calst.getTime();
	}

	/**
	 * 获取传入日期的结束时间 (23:59:59)
	 */
	public static final Date getEndTimeForThisTime(Date date) {
		Calendar calst = Calendar.getInstance();
		calst.setTime(date);
		calst.set(Calendar.HOUR_OF_DAY, 23);
		calst.set(Calendar.MINUTE, 59);
		calst.set(Calendar.SECOND, 59);
		calst.set(Calendar.MILLISECOND, 0);
		return calst.getTime();
	}
	/**
	 * 获取传入年月的增加月份值
	 * @param inputYear
	 * @param inputMonth
	 * @param addMonth
	 * @return
	 */
	public static final String getYearAndMonth(String inputYear,String inputMonth,int addMonth){
		Calendar now = Calendar.getInstance();
		now.set(Calendar.YEAR, Integer.parseInt(inputYear));
		now.set(Calendar.MONTH, Integer.parseInt(inputMonth)-1);
		now.add(Calendar.MONTH, addMonth);
		String year = String.valueOf(now.get(Calendar.YEAR));
		int _month = now.get(Calendar.MONTH) + 1;
		String month = new DecimalFormat("00").format(_month);
		return year+","+month;
	}

	/**
	 * 取得一月中的最早一天。
	 */
	public static Date getMinDateOfMonth(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));

		return calendar.getTime();
	}

	/**
	 * 取得一月中的最后一天
	 */
	public static Date getMaxDateOfMonth(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));

		return calendar.getTime();
	}

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = CalendarUtil.getCurrentDate().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
//		System.out.println(formatDate(getBeginTimeForThisTime(CalendarUtil.getCurrentDate()), DATE_PATTERN_MILLISECOND));
//		System.out.println(formatDate(getEndTimeForThisTime(CalendarUtil.getCurrentDate()), DATE_PATTERN_MILLISECOND));
	}

	/***
	 * 将日期转化为字符串。 字符串格式("yyyy年MM月dd日")。
	 */
	public static String dateToChineseString(Date date) {
		return formatDate(date, DATE_PATTERN_DAY_CHINNESS_DEFAULT);
	}

	public static Date dateToDate(Date date, String datePatternDay) {
		return stringToDate(formatDate(date, datePatternDay), datePatternDay);
	}

}
