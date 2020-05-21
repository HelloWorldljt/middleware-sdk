package com.xiangshang360.middleware.sdk.util;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * @author chenrg
 * @date 2019/3/6
 */
public class TradeCalendarUtilTest extends TestCase {

    @Test
    public void testIsWorkday() {
        boolean isWorkday = TradeCalendarUtils.isWorkday();
        Assert.assertTrue(isWorkday);
    }

    @Test
    public void testIsWorkaySignedDate() {
        Date date = DateUtils.stringToDate("2016-08-21", "yyyy-MM-dd");
        boolean isWorkday = TradeCalendarUtils.isWorkday(date);
        Assert.assertFalse(isWorkday);
    }

    @Test
    public void testNextWorkday() {
        Date nextWorkday = TradeCalendarUtils.nextWorkday();
        String nextWorkdayStr = DateUtils.formatDate(nextWorkday, "yyyy-MM-dd");
        Assert.assertEquals(nextWorkdayStr, "2019-03-08");
    }

    @Test
    public void testNextWorkdaySignedDate() {
        Date signedDate = DateUtils.stringToDate("2018-04-28", "yyyy-MM-dd");
        Date nextWorkday = TradeCalendarUtils.nextWorkday(signedDate);
        String nextWorkdayStr = DateUtils.formatDate(nextWorkday, "yyyy-MM-dd");
        Assert.assertEquals(nextWorkdayStr, "2018-05-02");
    }

    @Test
    public void testTheDayAfterWorkdays() {
        Date afterWorkdays = TradeCalendarUtils.theDayAfterWorkdays(7);
        String afterWorkdaysStr = DateUtils.formatDate(afterWorkdays, "yyyy-MM-dd");
        Assert.assertEquals(afterWorkdaysStr, "2019-03-18");
    }

    @Test
    public void testTheDayAfterWorkdaysSignedDate() {
        Date signedDate = DateUtils.stringToDate("2019-09-30", "yyyy-MM-dd");
        Date afterWorkdays = TradeCalendarUtils.theDayAfterWorkdays(signedDate, 3);
        String afterWorkdaysStr = DateUtils.formatDate(afterWorkdays, "yyyy-MM-dd");
        Assert.assertEquals(afterWorkdaysStr, "2019-10-10");
    }
}
