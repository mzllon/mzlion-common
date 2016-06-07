package com.mzlion.core.date;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing for {@linkplain DateCalcUtils}
 * <p>
 * Created by mzlion on 2016/5/28.
 */
public class DateCalcUtilsTest {

    private Date time;

    @Before
    public void before() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 4);
        calendar.set(Calendar.DATE, 28);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 40);
        calendar.set(Calendar.SECOND, 40);
        calendar.set(Calendar.MILLISECOND, 0);
        time = calendar.getTime();
    }


    @Test
    public void addMilliSecond() throws Exception {
        Date date = DateCalcUtils.addMilliSecond(time, 1);
        long expect = time.getTime() + 1;
        assertEquals(expect, date.getTime());
    }

    @Test
    public void addSecond() throws Exception {
        Date date = DateCalcUtils.addSecond(time, 1);
        long expect = time.getTime() + 1 * 1000;
        assertEquals(expect, date.getTime());
    }

    @Test
    public void addMinute() throws Exception {
        Date date = DateCalcUtils.addMinute(time, 1);
        long expect = time.getTime() + 1 * 60 * 1000;
        assertEquals(expect, date.getTime());
    }

    @Test
    public void addHour() throws Exception {
        Date date = DateCalcUtils.addHour(time, 1);
        long expect = time.getTime() + 1 * 60 * 60 * 1000;
        assertEquals(expect, date.getTime());
    }

    @Test
    public void addDate() throws Exception {
        Date date = DateCalcUtils.addDate(time, 1);
        long expect = time.getTime() + 1 * 24 * 60 * 60 * 1000;
        assertEquals(expect, date.getTime());
    }

    @Test
    public void addMonth() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DATE, 28);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 40);
        calendar.set(Calendar.SECOND, 40);
        calendar.set(Calendar.MILLISECOND, 0);
        Date addMonth = DateCalcUtils.addMonth(time, 1);
        assertEquals(calendar.getTimeInMillis(), addMonth.getTime());
    }

    @Test
    public void addMonth1() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date addMonth = DateCalcUtils.addMonth(time, 1, true);
        assertEquals(calendar.getTime().getTime(), addMonth.getTime());
    }

    @Test
    public void addYear() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.MONTH, 4);
        calendar.set(Calendar.DATE, 28);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 40);
        calendar.set(Calendar.SECOND, 40);
        calendar.set(Calendar.MILLISECOND, 0);
        Date addYear = DateCalcUtils.addYear(time, -1);
        assertEquals(calendar.getTimeInMillis(), addYear.getTime());
    }

    @Test
    public void getYear() throws Exception {
        int year = DateCalcUtils.getYear(time);
        assertEquals(2016, year);
    }

    @Test
    public void getMonth() throws Exception {
        int month = DateCalcUtils.getMonth(time);
        assertEquals(5, month);
    }

    @Test
    public void getDay() throws Exception {
        int day = DateCalcUtils.getDay(time);
        assertEquals(28, day);
    }

    @Test
    public void get24Hour() throws Exception {
        int hour24 = DateCalcUtils.get24Hour(time);
        assertEquals(23, hour24);
    }

    @Test
    public void get12Hour() throws Exception {
        int hour12 = DateCalcUtils.get12Hour(this.time);
        assertEquals(11, hour12);
    }

    @Test
    public void getMinute() throws Exception {
        int minute = DateCalcUtils.getMinute(time);
        assertEquals(40, minute);
    }

    @Test
    public void getSecond() throws Exception {
        int second = DateCalcUtils.getSecond(this.time);
        assertEquals(40, second);
    }

    @Test
    public void getMillisecond() throws Exception {
        int millisecond = DateCalcUtils.getMillisecond(time);
        assertEquals(0, millisecond);
    }

    @Test
    public void getTimeMillis() throws Exception {
        long timeMillis = DateCalcUtils.getTimeMillis(time);
        assertEquals(time.getTime(), timeMillis);
    }

    @Test
    public void isLeapYear() throws Exception {
        assertTrue(DateCalcUtils.isLeapYear(time));
    }

    @Test
    public void getBeginDayInMonth() throws Exception {
        Date beginDayInMonth = DateCalcUtils.getBeginDayInMonth(this.time);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 4);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 40);
        calendar.set(Calendar.SECOND, 40);
        calendar.set(Calendar.MILLISECOND, 0);
        assertEquals(calendar.getTimeInMillis(), beginDayInMonth.getTime());
    }


    @Test
    public void getEndDayInMonth() throws Exception {
        Date endDayInMonth = DateCalcUtils.getEndDayInMonth(this.time);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 4);
        calendar.set(Calendar.DATE, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 40);
        calendar.set(Calendar.SECOND, 40);
        calendar.set(Calendar.MILLISECOND, 0);
        assertEquals(calendar.getTimeInMillis(), endDayInMonth.getTime());
    }

    @Test
    public void getDaysBetween() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 4);
        calendar.set(Calendar.DATE, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 40);
        calendar.set(Calendar.SECOND, 40);
        calendar.set(Calendar.MILLISECOND, 0);
        int daysBetween = DateCalcUtils.getDaysBetween(calendar.getTime(), this.time);
        assertEquals(3, daysBetween);
    }

    @Test
    public void getMonthsBetween() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 4);
        calendar.set(Calendar.DATE, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 40);
        calendar.set(Calendar.SECOND, 40);
        calendar.set(Calendar.MILLISECOND, 0);
        int monthsBetween = DateCalcUtils.getMonthsBetween(calendar.getTime(), this.time);
        assertEquals(0, monthsBetween);
    }

}