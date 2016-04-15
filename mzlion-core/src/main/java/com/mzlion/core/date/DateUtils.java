/*
*/
package com.mzlion.core.date;

import com.mzlion.core.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 2016-04-14 日期工具类
 *
 * @author mzlion
 */
public abstract class DateUtils {
    //log
    private static Logger logger = LoggerFactory.getLogger(DateUtils.class);

    /**
     * 日期格式化规则：yyyyMMddHHmmss
     */
    public static final String PATTERN_FULL = "yyyyMMddHHmmss";

    /**
     * 日期格式化规则：yyyyMMdd
     */
    public static final String PATTERN = "yyyyMMdd";

    /**
     * 将日期转为字符串，格式化规则yyyyMMddHHmmss
     *
     * @param date 日期对象
     * @return 日期字符串
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return formatDate(date, PATTERN_FULL);
    }

    /**
     * 获取下N天（可为负值）转为字符串
     *
     * @param n       整数
     * @param date    参照日期
     * @param pattern 日期字符串样式
     * @return 日期字符串
     */
    public static String getNextDate(int n, Date date, String pattern) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, n);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime();

        return formatDate(date, pattern);
    }

    /**
     * @param n       正数当前日期后N月 负数当前日期前N月
     * @param date
     * @param pattern
     * @return String    返回类型
     * @throws
     * @Title: getNextMonth
     * @Description: 获取下N月后的日期（可为负值）转为字符串
     */
    public static String getNextMonth(int n, Date date, String pattern) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, n);//
        date = calendar.getTime();

        return formatDate(date, pattern);
    }

    /**
     * 将日期转为字符串
     *
     * @param date    日期对象
     * @param pattern 格式化规则
     * @return 日期字符串
     */
    public static String formatDate(Date date, String pattern) {
        if ((date == null) || !StringUtils.hasLength(pattern)) {
            return null;
        }
        return ThreadSafeDateParse.format(date, pattern);
    }

    /**
     * 将日期转为字符串
     *
     * @param date    日期
     * @param pattern 格式化规则
     * @return 日期字符串
     */
    public static String formatDate(long date, String pattern) {
        if ((date <= 0L) || !StringUtils.hasLength(pattern)) {
            return null;
        }
        return ThreadSafeDateParse.format(date, pattern);
    }

    /**
     * 将日期转为字符串，格式化规则yyyyMMddHHmmss
     *
     * @param date 日期对象
     * @return 日期字符串
     */
    public static String formatDate(long date) {
        return formatDate(date, PATTERN_FULL);
    }

    /**
     * 解析日期字符串
     *
     * @param strDate 日期字符串
     * @param pattern 解析规则
     * @return 日期对象
     */
    public static Date parseDate(String strDate, String pattern) {
        if (!StringUtils.hasText(strDate) || !StringUtils.hasText(pattern)) {
            return null;
        }
        try {
            return ThreadSafeDateParse.parse(strDate, pattern);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * 解析日期字符串,默认解析规则yyyyMMddHHmmss
     *
     * @param strDate
     * @return
     */
    public static Date parseDate(String strDate) {
        return parseDate(strDate, PATTERN_FULL);
    }

    /**
     * 解析日期字符串，日期格式应该是长整数的字符串表现形式。
     *
     * @param longStrDate 带解析的日期字符串
     * @return 返回日期
     */
    public static Date parseDateByLongStr(String longStrDate) {
        if (StringUtils.hasText(longStrDate)) {
            return new Date(Long.parseLong(longStrDate));
        }
        return null;
    }

    /**
     * 转换日期格式，比如将2013/01/01转为2013-01-01
     *
     * @param srcDate      日期字符串
     * @param sourceFormat 原始的格式化规则
     * @param destFotrmat  转换的日期格式化规则
     * @return 转换后的日期字符串
     */
    public static String swapDateStr(String srcDate, String sourceFormat, String destFotrmat) {
        Date date = parseDate(srcDate, sourceFormat);
        return formatDate(date, destFotrmat);
    }

}
