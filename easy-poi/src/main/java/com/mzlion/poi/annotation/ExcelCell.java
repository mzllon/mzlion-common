package com.mzlion.poi.annotation;

import com.mzlion.poi.constant.ExcelCellType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mzlion on 2016/6/7.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelCell {

    /**
     * Excel的标题
     */
    String title();

    /**
     * 是否是必要的
     */
    boolean required() default false;

    /**
     * 单元格类型
     */
    ExcelCellType type() default ExcelCellType.NORMAL;

    /**
     * 导出时单元格的宽度，单位为字符，一个汉字为2个字符
     */
    double width() default 10.0;

    /**
     * 导出时在excel中每个列的高度 单位为字符，一个汉字=2个字符
     */
    double height() default 10;

    /**
     * 单元格排序序号
     */
    int order() default 0;

    /**
     * 是否换行 即支持\n
     */
    boolean autoWrap() default true;

}
