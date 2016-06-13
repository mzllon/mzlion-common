package com.mzlion.poi.annotation;

import com.mzlion.poi.constant.ExcelHyperLinkType;

import java.lang.annotation.*;

/**
 * Created by mzlion on 2016/6/13.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelHyperLink {

    /**
     * EExcel的Cell链接类型
     */
    ExcelHyperLinkType value() default ExcelHyperLinkType.URL;

    /**
     * 链接名称
     */
    String linkName() default "";
}
