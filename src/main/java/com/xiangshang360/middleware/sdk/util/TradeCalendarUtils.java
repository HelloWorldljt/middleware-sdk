package com.xiangshang360.middleware.sdk.util;

import com.alibaba.fastjson.JSONObject;
import com.xiangshang360.middleware.redis.JedisPoolFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


/**
 * 交易日（工作日）计算工具
 *
 * @author chenrg
 * @date 2019/3/6
 */
public final class TradeCalendarUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TradeCalendarUtils.class);

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final String YEAR_PATTERN = "yyyy";

    /**
     * 按年缓存当年的节假日的日期列表
     */
    private static final String HOLIDAYS_LIST = "SDK-TRADE-CALENDAR-HOLIDAYS-LIST-%1s";

    /**
     * 按年缓存当年的周六日是工作日的日期列表
     */
    private static final String WEEKEND_BUT_WORKDAYS_LIST = "SDK-TRADE-CALENDAR-WEEKEND-BUT-WORKDAYS-LIST-%1s";

    /**
     * 判断当日是否是交易日
     *
     * @return 返回当日是否是工作日
     */
    public static boolean isWorkday() {
        return isWorkday(CalendarUtil.getCurrentDate());
    }

    /**
     * 判断某日期是否是工作日
     *
     * @param date 指定日期
     * @return 返回指定日期是否是工作日
     */
    public static boolean isWorkday(Date date) {
        // 排除节假日、正常周六日。
        boolean isHoliday = isHoliday(date);
        if (isHoliday) {
            return false;
        }
        boolean isWeekend = isWeekend(date);
        if (!isWeekend) {
            return true;
        }
        // 因节假日调休将周六日调整为工作日的日期算正常工作日
        boolean isWeekendButWorkday = isWeekendButWorkday(date);
        if (isWeekendButWorkday) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前日期的下一个工作日
     *
     * @return 返回当日的下一个工作日
     */
    public static Date nextWorkday() {
        return nextWorkday(CalendarUtil.getCurrentDate());
    }

    /**
     * 根据给定日期计算下一个工作日
     *
     * @param date 指定日期
     * @return 返回指定日的下一个工作日
     */
    public static Date nextWorkday(Date date) {
        while (true) {
            date = CalendarUtil.getNextDay(date, 1);
            if (isWorkday(date)) {
                return date;
            }
        }
    }

    /**
     * 计算当日过几个工作日之后的日期
     *
     * @param afterWorkDays 过几个工作日
     * @return
     */
    public static Date theDayAfterWorkdays(int afterWorkDays) {
        return theDayAfterWorkdays(CalendarUtil.getCurrentDate(), afterWorkDays);
    }

    /**
     * 计算指定日期几个工作日之后的日期
     *
     * @param date          指定日期
     * @param afterWorkDays 过几个工作日
     * @return
     */
    public static Date theDayAfterWorkdays(Date date, int afterWorkDays) {
        for (int i = 0; i < afterWorkDays; i++) {
            date = nextWorkday(date);
        }
        return date;
    }

    /**
     * 根据抓取的网上日历节假日数据，检查是否节假日有变更，用于自动检测并预警。该方法只被micro-sky-eye调用，其它应用不要调
     *
     * @param holidays           节假日
     * @param weekendButWorkdays 周六日但为工作日
     * @return
     */
    public static HashMap<String, String> verifyChanges(List<String> holidays, List<String> weekendButWorkdays) {
        Date current = CalendarUtil.getCurrentDate();
        List<String> holidayDiffs = compareLists(holidays, getHolidays(current));
        List<String> weekendButWorkdayDiffs = compareLists(weekendButWorkdays, getWeekendButWorkday(current));
        if (isEmpty(holidayDiffs) && isEmpty(weekendButWorkdayDiffs)) {
            return null;
        }
        HashMap<String, String> diff = new HashMap<>(2);
        diff.put("holidayDiffs", JSONObject.toJSONString(holidayDiffs));
        diff.put("weekendButWorkdayDiffs", JSONObject.toJSONString(weekendButWorkdayDiffs));
        return diff;
    }

    /**
     * 判断某个日期是否是周六日
     *
     * @param date
     * @return
     */
    private static boolean isWeekend(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
                calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return true;
        }
        return false;
    }

    /**
     * 判断某个日期是否是节假日
     *
     * @param date
     * @return
     */
    private static boolean isHoliday(Date date) {
        List<String> holidays = getHolidays(date);
        String theDate = DateFormatUtils.format(date, DATE_PATTERN);
        for (String holiday : holidays) {
            if (StringUtils.equals(theDate, holiday)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断某个日期是否是周六日但是正常工作日（因节假日占用正常周一到周五的某天，而将工作日调整到周六日，这种算正常工作日）
     *
     * @param date
     * @return
     */
    private static boolean isWeekendButWorkday(Date date) {
        List<String> weekendButWorkDays = getWeekendButWorkday(date);
        if (isEmpty(weekendButWorkDays)) {
            return false;
        }
        String theDate = DateFormatUtils.format(date, DATE_PATTERN);
        for (String weekendButWorkDay : weekendButWorkDays) {
            if (StringUtils.equals(theDate, weekendButWorkDay)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取指定日期所在年份的周六日调整为工作日的日期
     *
     * @param date
     * @return
     */
    private static List<String> getWeekendButWorkday(Date date) {
        return getFromCache(WEEKEND_BUT_WORKDAYS_LIST, date);
    }

    /**
     * 获取指定日期所在年份的法定节假日
     *
     * @return
     */
    private static List<String> getHolidays(Date date) {
        List<String> list = getFromCache(HOLIDAYS_LIST, date);
        if (isEmpty(list)) {
            throw new RuntimeException("Holidays must not be empty.");
        }
        return list;
    }

    /**
     * 从缓存中获取节假日或工作日是周六日的日期列表
     *
     * @param keyPrefix
     * @param date
     * @return
     */
    private static List<String> getFromCache(String keyPrefix, Date date) {
        String current = DateFormatUtils.format(CalendarUtil.getCurrentDate(), DATE_PATTERN);
        String currentYear = StringUtils.substring(current, 0, 4);
        String currentMonth = StringUtils.substring(current, 5, 7);
        String currentDay = StringUtils.substring(current, 8, 10);

        String targetYear = DateFormatUtils.format(date, YEAR_PATTERN);

        int intCurrentYear = Integer.valueOf(currentYear);
        int intCurrentMonth = Integer.valueOf(currentMonth);
        int intCurrentDay = Integer.valueOf(currentDay);
        int intTargetYear = Integer.valueOf(targetYear);

        // 如果目标年份大于当前年份下一年，直接异常
        if (intTargetYear > intCurrentYear + 1) {
            throw new RuntimeException("The holidays for year " + targetYear + " is not published.");
        }
        //　目标年份是当前年份下一年，且当前日期在12月15之前，下一年法定假日还未公布，直接异常
        boolean isHolidayUnpublished = (intTargetYear == intCurrentYear + 1)
                && (intCurrentMonth < 12 || (intCurrentMonth == 12 && intCurrentDay < 15));
        if (isHolidayUnpublished) {
            throw new RuntimeException("The holidays for year " + targetYear + " is not published.");
        }
        // 不支持查询2015年之前的年份
        if (Integer.valueOf(targetYear) <= 2015) {
            throw new RuntimeException("The holidays for year " + targetYear + " is unknown.");
        }

        List<String> days = null;
        Jedis jedis = null;
        try {
            String key = String.format(keyPrefix, targetYear);
            jedis = JedisPoolFactory.getJedisPool().getResource();
            String val = jedis.get(key);
            if (StringUtils.isNotBlank(val)) {
                days = JSONObject.parseObject(val, ArrayList.class);
            }
        } catch (Exception e) {
            LOG.error("Can't get jedis from pool.", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        // 当前年份的节假日不允许为空
        boolean isCurrentYearEmpty = (days == null || days.size() == 0)
                && StringUtils.equals(HOLIDAYS_LIST, keyPrefix)
                && StringUtils.equals(currentYear, targetYear);
        if (isCurrentYearEmpty) {
            throw new RuntimeException("Can't found the holidays for " + currentYear + " year, holidays must not be empty.");
        }

        return days;
    }


    /**
     * 返回两个list差异元素集合，如果没有差异，返回空
     *
     * @param captureList
     * @param cachedList
     * @return
     */
    private static List<String> compareLists(List<String> captureList, List<String> cachedList) {
        if (isEmpty(captureList) && isEmpty(cachedList)) {
            return null;
        }
        if (isEmpty(captureList) && !isEmpty(cachedList)) {
            return cachedList;
        }
        if (!isEmpty(captureList) && isEmpty(cachedList)) {
            return captureList;
        }

        List<String> diff = new ArrayList<>();
        List<String> tmp = new ArrayList<>();

        tmp.addAll(captureList);
        tmp.removeAll(cachedList);
        diff.addAll(tmp);

        tmp.clear();
        tmp.addAll(cachedList);
        tmp.removeAll(captureList);
        diff.addAll(tmp);

        return diff;
    }

    private static boolean isEmpty(Collection collection) {
        if (collection == null || collection.size() <= 0) {
            return true;
        }
        return false;
    }

    private static boolean isEmpty(Map map) {
        if (map == null || map.size() <= 0) {
            return true;
        }
        return false;
    }

}
