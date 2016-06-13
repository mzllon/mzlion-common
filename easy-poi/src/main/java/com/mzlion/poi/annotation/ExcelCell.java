package com.mzlion.poi.annotation;

import com.mzlion.poi.constant.ExcelCellType;

import java.lang.annotation.*;

/**
 * Excel的单元格注解，标记java属性和cell的关系
 *
 * @author mzlion
 * @date 2016-06-11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelCell {

    /**
     * cell的标题
     */
    String value();

    /**
     * 该cell 列是否是必须的
     */
    boolean required() default false;

    /**
     * cell填充或解析的数据类型
     */
    ExcelCellType type() default ExcelCellType.AUTO;

    /**
     * 导出时cell的宽度，单位为字符，一个汉字为2个字符
     */
    float width() default 8.38f;

    /**
     * 对应Excel的顺序
     */
    int order() default 0;

    /**
     * 是否换行 即支持\n
     */
    boolean autoWrap() default false;

    /**
     * 在Excel文件日期的格式化风格,默认为空则表示忽略
     */
    String excelDateFormat() default "";

    /**
     * 在JavaBean日期的格式化风格，默认为空则表示忽略
     */
    String javaDateFormat() default "";

}
