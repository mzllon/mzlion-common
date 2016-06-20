package com.mzlion.poi.annotation;

import java.lang.annotation.*;

/**
 * Excel的惟一键的属性标记
 *
 * @author mzlion
 * @date 2016-06-16
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelId {

}
