/**
 * Copyright(c) 2010-2013 by XiangShang Inc.
 * All Rights Reserved
 */
package com.xiangshang360.middleware.sdk.util;

import com.xiangshang360.middleware.sdk.timechanger.RedisTimeChanger;
import com.xiangshang360.middleware.sdk.timechanger.TimeChanger;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 */
public class CalendarUtil {

	private final static Logger LOG = LoggerFactory.getLogger(CalendarUtil.class);

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_SINGLE_FORMAT = "yyyy-M-d";
	public static final String DATE_SLASH_FORMAT = "yyyy/MM/dd";
	public static final String DATE_SINGLE_SLASH_FORMAT = "yyyy/M/d";
	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_NOT_SLASH_FORMAT = "yyyyMMddHHmmss";
	public static final String DATE_NOT_SLASH_DATE_FORMAT = "yyyyMMdd";
	public static final String DATE_TWO_PALCE_YEAR_DAY_FORMAT = "yyMMdd";
	public static final String DATE_YEAR_MONTH_FORMAT = "yyyyMM";
	
	public static final String DATE_PATTERN_DAY_CHINNESS_DEFAULT = "yyyy年MM月dd日";
	public static final String DATE_PATTERN_DAY_CHINNESS = "【yyyy】年【MM】月【dd】";

	public static final String DATE_PATTERN_JUEST_DAY = "dd";
	public static final String[] ALL_FORMAT = {DATE_FORMAT, DATE_SINGLE_FORMAT, DATE_SLASH_FORMAT, DATE_SINGLE_SLASH_FORMAT, DATETIME_FORMAT, DATE_PATTERN_DAY_CHINNESS_DEFAULT, DATE_PATTERN_DAY_CHINNESS};

	private static  TimeChanger timeChanger;
	static {
		timeChanger = new RedisTimeChanger();
	}
	public static String formatDateByStr(Date input, String str) {
		String returnValue = null;
		if (input != null) {
			returnValue = DateFormatUtils.format(input, str);
		}
		return returnValue;
	}

	public static String formatDate(Date input) {
		String returnValue = null;
		if (input != null) {
			returnValue = DateFormatUtils.format(input, DATE_FORMAT);
		}
		return returnValue;
	}
	
	
	public static String getDateNotSlashFormat(Date input){
    	String returnValue = null;
		if (input != null) {
			returnValue = DateFormatUtils.format(input, DATE_NOT_SLASH_FORMAT);
		}
		return returnValue;
    }

	public static String getDateNotSlashDateFormat(Date input){
		String returnValue = null;
		if (input != null) {
			returnValue = DateFormatUtils.format(input, DATE_NOT_SLASH_DATE_FORMAT);
		}
		return returnValue;
	}
	
	/**
     * 获取某个日期的天
     * @param date
     * @return
     */
    public static int getDay(Date date){
        return getField(date,Calendar.DAY_OF_MONTH);
    }
    
    /**
     * 获取某个时间的某个字段值
     * @param date
     * @param field
     * @return
     */
    public static int getField(Date date,int field){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(field);
    }
	
	/**
	 * 按指定样式格式化时间
	 * @param input
	 * @param format
	 * @return
	 */
	public static String formatDate(Date input,String format) {
		String returnValue = null;
		if (input != null) {
			returnValue = DateFormatUtils.format(input, format);
		}
		return returnValue;
	}

	public static String formatDateTime(Date input) {
		String returnValue = null;
		if (input != null) {
			returnValue = DateFormatUtils.format(input, DATETIME_FORMAT);
		}
		return returnValue;
	}

	public static Date parseDate(String input) {
		Date result = null;
		try {
			result = DateUtils.parseDate(input, DATE_FORMAT);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Date parseDateTime(String input) {
		Date result = null;
		try {
			result = DateUtils.parseDate(input, DATETIME_FORMAT);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Date compatibleParseDate(String input) {
		for (String format : ALL_FORMAT) {
			try {
				Date result = DateUtils.parseDate(input, format);
				return result;
			} catch (ParseException e) {
			}
		}
		return null;
	}
	
	/**
	 * 日期之前
	 * 
	 * @param date
	 * @param date2
	 * @return
	 */
	public static boolean isBefore(Date date, Date date2) {
		return null != date && null != date2 && date.before(date2);
	}

	/**
	 * 日期之后
	 * 
	 * @param date
	 * @param date2
	 * @return
	 */
	public static boolean isAfter(Date date, Date date2) {
		return null != date && null != date2 && date.after(date2);
	}

	/**
	 * 日期之中
	 * 
	 * @param date
	 * @param date2
	 * @param date3
	 * @return
	 */
	public static boolean isBetween(Date date, Date date2, Date date3) {
		return null != date && isAfter(date, date2) && isBefore(date, date3);
	}

	/**
	 * 某一个日期n月相对应某一天 n 为负值表示向前 n 为正值表示向后
	 */
	public static Date calDateForMonth(Date date, int n) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, n);
		return c.getTime();
	}

	/**
	 * 某一个日期n天相对应某一天 n 为负值表示向前 n 为正值表示向后
	 */
	public static Date calDateForDay(Date date, int n) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, n);
		return c.getTime();
	}

	/**
	 * 某一个日期n天相对应某一天 n 为负值表示向前 n 为正值表示向后
	 */
	public static Date calDateForYear(Date date, int n) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, n);
		return c.getTime();
	}

	/**
	 * 取某一个时间相对某一时间n小时向前或向后的时间
	 */
	public static Date calDateForHour(Date date, int n) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, n);
		return c.getTime();
	}

	/**
	 * 取某一时间相对某一时间n分钟向前或向后的时间
	 */
	public static Date calDateForMin(Date date, int n) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, n);
		return c.getTime();
	}

	/**
	 * 当前月份最大天数
	 *
	 * @param date
	 */
	public static int currentMonthMaxDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return day;
	}

	/**
	 * 计算日期天数差值
	 *
	 * @param early
	 * @param late
	 * @return
	 */
	public static final int daysBetween(Date early, Date late) {
		Calendar calst = Calendar.getInstance();
		Calendar caled = Calendar.getInstance();
		calst.setTime(early);
		caled.setTime(late);
		// 设置时间为0时
		calst.set(Calendar.HOUR_OF_DAY, 0);
		calst.set(Calendar.MINUTE, 0);
		calst.set(Calendar.SECOND, 0);
		caled.set(Calendar.HOUR_OF_DAY, 0);
		caled.set(Calendar.MINUTE, 0);
		caled.set(Calendar.SECOND, 0);
		// 得到两个日期相差的天数
		int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600 / 24;
		return days;
	}

	/**
	 * 获取传入日期的开始时间 00:00:00
	 */
	public static final Date calBeginTimeForThisTime(Date date) {
		Calendar calst = Calendar.getInstance();
		calst.setTime(date);
		calst.set(Calendar.HOUR_OF_DAY, 0);
		calst.set(Calendar.MINUTE, 0);
		calst.set(Calendar.SECOND, 0);
		calst.set(Calendar.MILLISECOND, 0);
		return calst.getTime();
	}

	/**
	 * 获取传入日期的结束时间 00:00:00
	 */
	public static final Date calEndTimeForThisTime(Date date) {
		Calendar calst = Calendar.getInstance();
		calst.setTime(date);
		calst.set(Calendar.HOUR_OF_DAY, 23);
		calst.set(Calendar.MINUTE, 59);
		calst.set(Calendar.SECOND, 59);
		calst.set(Calendar.MILLISECOND, 0);
		return calst.getTime();
	}

	/*
	 * 
	 * 判断传入的时间是否在早上9点到晚上10点之间
	 */
	public static int compare_date(Date DATE1) throws ParseException {
		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now_date_9 = ss.format(DATE1) + " 09:00:00";
		String now_date_22 = ss.format(DATE1) + " 22:00:00";
		Date time_9 = df.parse(now_date_9);
		Date time_22 = df.parse(now_date_22);
		try {
			if (DATE1.before(time_9)) {
				return -1;
			} else if (DATE1.after(time_22)) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	public static boolean compareDateHour(Date date, int beginHour, int endHour){

		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String ds1 = ss.format(date) + " " + String.valueOf(beginHour) + ":00:00";
		String ds2 = ss.format(date) + " " + String.valueOf(endHour) + ":59:59";

		try {
			Date beginDate = df.parse(ds1);
			Date endDate = df.parse(ds2);

			if (date.getTime() >= beginDate.getTime() && date.getTime() <= endDate.getTime()){
				return true;
			}
		} catch (Exception e) {

		}
		return false;
	}

	public static int truncatedCompareTo(Date date1, Date date2, int field) {
		return DateUtils.truncatedCompareTo(date1, date2, field);
	}

	/*
	 * 
	 * 根据标识返回时间
	 */
	public static Date obtain_date(Date DATE1, int flage, String timeFlage) throws ParseException {
		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now_date_9 = ss.format(DATE1) + " " + timeFlage;
		String now_date_22 = ss.format(DATE1) + " " + timeFlage;
		if (flage == -1) {// 时间早与9点
			DATE1 = df.parse(now_date_9);
		} else if (flage == 1) {// 时间晚于22点
			DATE1 = df.parse(now_date_22);
		}
		return DATE1;
	}

	public static int getBetweenSecondNumberNotAbsolute(Date startDate, Date endDate) {

		if (startDate == null || endDate == null) {
			return -1;
		}
		long timeNumber = -1L;
		long TIME = 1000L;
		try {
			timeNumber = (endDate.getTime() - startDate.getTime()) / TIME;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return (int) timeNumber;
	}

	public static void main(String[] args) throws ParseException {
//		Date date = new Date();
//		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String test = f.format(date) + " 01:59:59";
//		Date date_ = df.parse(test);
//
//		System.out.println(compare_date(date_));
//		System.out.println(obtain_date(date_, compare_date(date_), " 9:00:00"));

		//System.out.println(getPreTime(new Date(), 15));
//		System.out.println(formatDate(calBeginTimeForThisTime(new Date()), "yyyy-MM-dd HH:mm:ss.SSS"));
//		System.out.println(formatDate(calEndTimeForThisTime(new Date()), "yyyy-MM-dd HH:mm:ss.SSS"));
// 		System.out.println(getMinDateOfDay(new Date()));
	}

	/**
	 * 忽略时间来判断两个日期是否相同天
	 * 
	 * @param dateOne
	 * @param dateTwo
	 * @return
	 */
	public static boolean isSameDay(Date dateOne, Date dateTwo) {
		return DateUtils.isSameDay(dateOne, dateTwo);
	}

	/**
	 * 根据系统还款日配置，查看某天是否是还款日
	 * 
	 * @param configLoanRepayDays
	 * @param date
	 * @return
	 */
	public static boolean isLoanRepayDay(String configLoanRepayDays, Date date) {
		String[] repayDays = StringUtils.split(configLoanRepayDays, ",;:");
		String day = DateFormatUtils.format(date, "d");
		if (ArrayUtils.contains(repayDays, day)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取2个时间相隔几分钟,非绝对值
	 */
	public static int getMinutesNotAbsolute(Date startDate, Date endDate) {
		if (startDate == null || endDate == null)
			return -1;

		long timeNumber = -1l;
		long TIME = 60L * 1000L;
		try {
			timeNumber = (endDate.getTime() - startDate.getTime()) / TIME;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return (int) timeNumber;
	}

	/**
	 * 获取时间+N分钟的值
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static Date getNextTime(Date date, int num) {
		Calendar ca = Calendar.getInstance();
		ca.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		ca.setTime(date);

		int minute = ca.get(Calendar.MINUTE);
		minute = minute + num;
		ca.set(Calendar.MINUTE, minute);
		return ca.getTime();
	}

	/**
	 * 获取时间-N分钟的值
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static Date getPreTime(Date date, int num) {
		Calendar ca = Calendar.getInstance();
		ca.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		ca.setTime(date);

		int minute = ca.get(Calendar.MINUTE);
		minute = minute - num;
		ca.set(Calendar.MINUTE, minute);
		return ca.getTime();
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
        	e.printStackTrace();
        }
        return dateTime;
    }
    /**
     * 根据指定格式获取日期数据
     * 
     * @author tangzhi,2012-11-28
     * @param date
     *            ：指定日期
     * @param pattern
     *            ：日期格式
     * @return
     */
    private static String getFormatDateTime(Date date, String pattern) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return format.format(date);
    }
	/**
     * 给定一个日期和天数，得到这个日期+天数的日期
     * 
     * @author
     * @param date
     *            指定日期
     * @param num
     *            天数
     * @return
     */
    public static String getNextDay(String date, int num,String format) {
        Date d = stringToDate(date, format);
        Calendar ca = Calendar.getInstance();
        ca.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        ca.setTime(d);

        int day = ca.get(Calendar.DATE);
        day = day + num;
        ca.set(Calendar.DATE, day);
        return getFormatDateTime(ca.getTime(), format);
    }

	public static Date getNextDay(Date date, int num) {
		Calendar ca = Calendar.getInstance();
		ca.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		ca.setTime(date);
		int day = ca.get(Calendar.DATE);
		day = day + num;
		ca.set(Calendar.DATE, day);
		return ca.getTime();
	}


	/**
	 * 给定一个日期和月数，得到这个日期+月份的日期
	 *
	 * @author xienian, 2017-3-2 11:00:06
	 * @param date 指定日期
	 * @param num 月数
	 * @return
	 */
	public static Date getNextMonth(Date date, int num) {
		Calendar ca = Calendar.getInstance();
		ca.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		ca.setTime(date);

		int month = ca.get(Calendar.MONTH);
		month = month + num;
		ca.set(Calendar.MONTH, month);
		return ca.getTime();
	}


	 /***
     * 将日期转化为字符串
     */
    public static String dateToString(Date date, String pattern) {
        String str = "";
        try {
            SimpleDateFormat formater = new SimpleDateFormat(pattern);
            str = formater.format(date);
        } catch (Throwable e) {
        	e.printStackTrace();
        }
        return str;
    }

    /**
     * 将字符串转化为日期
     */
    public static Date stringToDateTime(String str)  {
        return parseDateTime(str);
    }
    /**
     * @Description 获取当前时间的numDate后的日期
     * 
     * @param startDate
     * @param numDate
     * @return
     */
    public static String getSomeDate(Date startDate,int numDate){
		String open_time = formatDateTime(startDate);//开始招标时间
		return getNextDay(open_time,numDate,DATETIME_FORMAT);
    }
    
	 /**
     * 获取2个时间相隔几天
     * <br>
     * 一天中每个时间段都算做同一时间点来计算
     */
    public static int getBetweenDayNumber(Date startDate, Date endDate) {
        if (startDate == null || endDate == null)
            return -1;

        if (startDate.after(endDate)) {
            Date tmp = endDate;
            endDate = startDate;
            startDate = tmp;
        }

        long dayNumber = -1L;
        try {
            long ms = DateUtils.MILLIS_PER_DAY;//一天的毫秒数
            endDate = DateUtils.truncate(endDate, Calendar.DAY_OF_MONTH);//将时间转化为yyyy-MM-dd 00:00:00
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);//将时间转化为yyyy-MM-dd 00:00:00
			dayNumber = (endDate.getTime() - startDate.getTime()) / ms;//两个时间相差天数
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return (int) dayNumber;
    }

	/**
	 * 获取2个时间相隔几小时
	 */
	public static int getBetweenHourNumber(Date startDate, Date endDate) {
		if (startDate == null || endDate == null)
			return -1;

		if (startDate.after(endDate)) {
			Date tmp = endDate;
			endDate = startDate;
			startDate = tmp;
		}

		long timeNumber = -1l;
		long TIME = 60L * 60L * 1000L;
		try {
			timeNumber = (endDate.getTime() - startDate.getTime()) / TIME;
		} catch (Exception e) {
			LOG.error(LogUtils.template("getBetweenHourNumber", null," failed, startDate = " + startDate + ", endDate = " + endDate,null), e);
		}
		return (int) timeNumber;
	}


	/**
	 * 格式化时期 默认返回"yyyy-MM-dd
	 * @param input
	 * @return
	 */
	public static  Date parseDate(Date input){
		return CalendarUtil.parseDate(input,DATE_FORMAT);
	}

	/**
	 * 指定返回日期格式
	 * @param input
	 * @param partFormat
	 * @return
	 */
	public static Date parseDate(Date input, String partFormat){
		String returnValue = DateFormatUtils.format(input, partFormat);
		Date result = null;
		try {
			result = DateUtils.parseDate(returnValue, partFormat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
    /**
     * 根据开始时间和结束时间返回时间段内的时间集合 
     * @author Zhang Meijia 
     * @param beginDate
     * @param endDate
     * @return List<Date>  
     * @throws
     */
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {  
        List<Date> lDate = new ArrayList<Date>();  
        lDate.add(beginDate);// 把开始时间加入集合  
        Calendar cal = Calendar.getInstance();  
        // 使用给定的 Date 设置此 Calendar 的时间  
        cal.setTime(beginDate);  
        boolean bContinue = true;  
        while (bContinue) {  
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量  
            cal.add(Calendar.DAY_OF_MONTH, 1);  
            // 测试此日期是否在指定日期之后  
            if (endDate.after(cal.getTime())) {  
                lDate.add(cal.getTime());  
            } else {  
                break;  
            }  
        }  
        lDate.add(endDate);// 把结束时间加入集合  
        return lDate;  
    }

	/**
	 * 输入日期是否为同一天.
	 */
	public static boolean isTheSameDay(Date startDate, Date endDate) {
		String startDateStr = dateToString(startDate, DATE_FORMAT);
		String endDateStr = dateToString(endDate, DATE_FORMAT);
		return startDateStr.equals(endDateStr);
	}

	/**
	 * 在测试环境通过启动时加载探针将方法名修改为 getCurrentDateTe，达到动态修改时间的目的；
	 * 使线上不存在特殊代码，防止出现问题导致核心业务出错
	 * 探针代码：http://wiki.xs.jf/pages/viewpage.action?pageId=14223334
	 * @return Date
	 * @author zhangchao
	 * @date 2019年8月30日
	 */
	public static Date getCurrentDate(){
		return new Date();
	}

	/**
	 * 在测试环境通过启动时加载探针将方法getCurrentDate 修改为 getCurrentDateTe
	 * @return Date
	 * @author zhangchao
	 * @date 2019年8月30日
	 */
	public static Date getCurrentDateTe(){
		LOG.info("探针起效");
		return timeChanger.getCurrentTime();
	}

	/**
	 * 获取两个日期相差的月数
	 * @param date1  较大的日期
	 * @param date2  较小的日期
	 * @return 如果d1>d2返回 月数差 否则返回0
	 */
	public static int getMonthDiff(Date date1, Date date2) {

		if (date2.getTime() >= date1.getTime()) {
			Integer day1 = Integer.valueOf(dateToString(date1, "dd"));
			Integer day2 = Integer.valueOf(dateToString(date2, "dd"));

			int monthC = getBetweenMonthNumber(date1, date2);
			if (day1 > day2) {
				monthC--;
			}
			return monthC;
		} else {
			return 0;
		}
	}

	/**
	 * 获取2个时间相隔几月
	 */
	public static int getBetweenMonthNumber(Date startDate, Date endDate) {
		int result = 0;
		try {
			if (startDate == null || endDate == null){
				return -1;
			}

			// swap start and end date
			if (startDate.after(endDate)) {
				Date tmp = endDate;
				endDate = startDate;
				startDate = tmp;
			}

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);

			int monthS = calendar.get(Calendar.MONTH);
			int yearS = calendar.get(Calendar.YEAR);

			calendar.setTime(endDate);
			int monthE = calendar.get(Calendar.MONTH);
			int yearE = calendar.get(Calendar.YEAR);

			if (yearE - yearS == 0) {
				result = monthE - monthS;
			} else {
				result = (yearE - yearS - 1) * 12 + (12 - monthS) + monthE;
			}

		} catch (Exception e) {
			LOG.error(LogUtils.template("getBetweenMonthNumber", null," failed, startDate = " + startDate + ", endDate = " + endDate,null), e);
		}
		return result;
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
	 * 取某个日期所在月份的第一天
	 * @param date
	 * @return
	 */
	public static Date getMinDayOfMonth(Date date){
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
	 * 获取传入日期的当年第一天，例：1990-01-01 00:00:00
	 * @param date
	 * @return
	 */
	public static Date calFirstDayYearForThisTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_YEAR,1);
		c.set(java.util.Calendar.HOUR_OF_DAY, 0);
		c.set(java.util.Calendar.MINUTE, 0);
		c.set(java.util.Calendar.SECOND, 0);
		return c.getTime();
	}

	/**
	 * 获取2个时间相隔几年
	 */
	public static int getBetweenYearNumber(Date startDate, Date endDate) {
		int result = 0;
		try {
			if (startDate == null || endDate == null)
				return -1;

			// swap start and end date
			if (startDate.after(endDate)) {
				Date tmp = endDate;
				endDate = startDate;
				startDate = tmp;
			}

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			int yearS = calendar.get(Calendar.YEAR);

			calendar.setTime(endDate);
			int yearE = calendar.get(Calendar.YEAR);

			result = yearE - yearS;

		} catch (Exception e) {
			LOG.error(LogUtils.template("getBetweenYearNumber", null, " failed, startDate = " + startDate + ", endDate = " + endDate, null), e);
		}
		return result;
	}


	/**
	 * 取得一个date对象对应的日期的0点0分0秒时刻的Date对象。
	 */
	public static Date getMinDateOfDay(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
		return calendar.getTime();
	}
	/**
	 * 得到给定时间的年份
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 取得一年中的最早一天。
	 */
	public static Date getMinDateOfYear(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));

		return calendar.getTime();
	}

	/**
	 * 取得一年中的最后一天
	 */
	public static Date getMaxDateOfYear(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));

		return calendar.getTime();
	}

	/**
	 * @description 生成中文的时间展示
	 * ①今天只展示时间如[11:11]，②昨天展示如[昨天 11:11]，③当年的其他时间展示如[11-11 11:11]，④之前的年份时间展示如[2018-11-11 11:11]
	 * @author Yangfengshuai
	 * @date 2019/8/28
	 */
	public static String getShowTimeZh(Date date) {
		if (date == null) {
			return "";
		}
		Date todayMin = getMinDateOfDay(CalendarUtil.getCurrentDate());
		if (date.after(todayMin)) {
			return dateToString(date, "HH:mm");
		}
		Date yesterdayMin = DateUtils.addDays(todayMin, -1);
		if (date.after(yesterdayMin)) {
			return dateToString(date, "昨天 HH:mm");
		}
		Date thisYearMin = getMinDateOfYear(todayMin);
		if (date.after(thisYearMin)) {
			return dateToString(date, "MM-dd HH:mm");
		}
		return dateToString(date, "yyyy-MM-dd HH:mm");
	}
}
