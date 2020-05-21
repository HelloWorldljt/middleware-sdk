package com.xiangshang360.middleware.sdk.util;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;

/**
 * From http://www.oschina.net/code/snippet_933828_24567
 */
public class HolidayUtils {
    // 阳历
    private static final String SOLAR[] = {"0101", "0404", "0501", "1001", "1002", "1003"};
    // 阴历
    private static final String LUNAR[] = {"0101", "0102", "0103", "0505", "0815", "1231"};


    private final static NumberFormat NumFmt = NumberFormat.getInstance();

    static {
        NumFmt.setMaximumFractionDigits(0);
        NumFmt.setMinimumIntegerDigits(2);
    }

    public static Boolean isHoliday(Calendar cal) {
        long[] ds = LunarCalendar.get(cal);
        String nongli = NumFmt.format(ds[1]) + NumFmt.format(ds[2]);
        String yangli = NumFmt.format(cal.get(Calendar.MONTH) + 1) +
                NumFmt.format(cal.get(Calendar.DATE));
        Boolean holiday = false;

        //阳历
        if (isInThoseDays(SOLAR, yangli)) {
            holiday = true;
        }

        //阴历
        if (isInThoseDays(LUNAR, nongli)) {
            holiday = true;
        }

        //双休
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            holiday = true;
        }

        return holiday;
    }

    private static boolean isInThoseDays(String[] holidays, String theDay) {
        boolean flag = false;
        for (String holiday : holidays) {
            if (holiday.equals(theDay)) {
                flag = true;
                return flag;
            }
        }
        return flag;
    }

    public static int numberOfHolidays(Calendar startDate, Calendar endDate) {
        int numberOfWorkdays = 0;
        for (; startDate.before(endDate); startDate.add(Calendar.DAY_OF_YEAR, 1)){
            if (isHoliday(startDate))
                numberOfWorkdays++;
        }
        return numberOfWorkdays;
    }

    public static void main(String[] args) throws IOException {
        Calendar date = Calendar.getInstance();
        System.out.println(date + ";" + isHoliday(date));
        date.add(Calendar.DAY_OF_YEAR, 1);
        System.out.println(date + ";" + isHoliday(date));
        date.add(Calendar.DAY_OF_YEAR, 1);
        System.out.println(date + ";" + isHoliday(date));
        date.add(Calendar.DAY_OF_YEAR, 1);
        System.out.println(date + ";" + isHoliday(date));
        date.add(Calendar.DAY_OF_YEAR, 1);
        System.out.println(date + ";" + isHoliday(date));
    }

}
