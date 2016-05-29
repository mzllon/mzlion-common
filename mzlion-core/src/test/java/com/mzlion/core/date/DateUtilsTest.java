package com.mzlion.core.date;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * 测试类
 * <p>
 * Created by mzlion on 2016/5/28.
 */
public class DateUtilsTest {

    private Date time;
    private String expected;

    @Before
    public void before() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 4);
        calendar.set(Calendar.DATE, 28);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 11);
        calendar.set(Calendar.SECOND, 11);
        calendar.set(Calendar.MILLISECOND, 0);
        time = calendar.getTime();
        expected = "20160528231111";
    }

    @Test
    public void formatDate() throws Exception {
        String formatDate = DateUtils.formatDate(time);
        assertEquals(expected, formatDate);
    }

    @Test
    public void getNextDate() throws Exception {
        String nextDate = DateUtils.getNextDate(time, 3, DateUtils.PATTERN_FULL);
        assertEquals("20160531231111", nextDate);
    }

    @Test
    public void getNextMonth() throws Exception {
        String nextMonth = DateUtils.getNextMonth(time, 1, DateUtils.PATTERN_FULL);
        assertEquals("20160628231111", nextMonth);
    }

    @Test
    public void formatDate1() throws Exception {
        String formatDate = DateUtils.formatDate(time, "yyyy-MM-dd HH:mm:ss");
        assertEquals("2016-05-28 23:11:11", formatDate);
    }

    @Test
    public void formatDate2() throws Exception {
        long timeLong = this.time.getTime();
        String formatDate = DateUtils.formatDate(timeLong);
        assertEquals(expected, formatDate);
    }

    @Test
    public void formatDate3() throws Exception {
        long timeLong = this.time.getTime();
        String formatDate = DateUtils.formatDate(timeLong, "yyyy-MM-dd HH:mm:ss");
        assertEquals("2016-05-28 23:11:11", formatDate);
    }

    @Test
    public void parseDate() throws Exception {
        Date parseDate = DateUtils.parseDate(expected);
        assertEquals(time.getTime(), parseDate.getTime());
    }

    @Test
    public void parseDate1() throws Exception {
        Date parseDate = DateUtils.parseDate("20160528", DateUtils.PATTERN);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 4);
        calendar.set(Calendar.DATE, 28);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        assertEquals(calendar.getTimeInMillis(), parseDate.getTime());
    }

    @Test
    public void parseDateByLongStr() throws Exception {
        long l = System.currentTimeMillis();
        String currentTimeMillis = String.valueOf(l);
        Date date = DateUtils.parseDateByLongStr(currentTimeMillis);
        assertEquals(l, date.getTime());
    }

    @Test
    public void swapDateStr() throws Exception {
        String dateStr = DateUtils.swapDateStr(expected, DateUtils.PATTERN_FULL, "yyyy-MM-dd HH:mm:ss");
        assertEquals("2016-05-28 23:11:11", dateStr);
    }

}