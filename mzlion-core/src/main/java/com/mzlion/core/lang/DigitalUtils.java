package com.mzlion.core.lang;

import java.math.BigDecimal;

/**
 * Created by mzlion on 2016/6/8.
 */
public class DigitalUtils {

    /**
     * 将科学计数法转为字符串
     *
     * @param val 以科学计数法形式的数字
     * @return 字符串
     */
    public static String avoidScientificNotation(double val) {
        return avoidScientificNotation(String.valueOf(val));
    }


    /**
     * 将科学计数法转为字符串
     *
     * @param val 以科学计数法形式的数字
     * @return 字符串
     */
    public static String avoidScientificNotation(String val) {
        return StringUtils.isEmpty(val) ? StringUtils.EMPTY_STRING : (val.matches("^\\d(.\\d+)?[eE](\\d+)$") ? new BigDecimal(val).toPlainString() : val);
    }


}
