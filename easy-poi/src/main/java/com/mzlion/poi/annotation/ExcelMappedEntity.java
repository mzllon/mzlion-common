package com.mzlion.poi.annotation;

import java.lang.annotation.*;

/**
 * Created by mzlion on 2016/6/11.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelMappedEntity {

    String[] value();



}
