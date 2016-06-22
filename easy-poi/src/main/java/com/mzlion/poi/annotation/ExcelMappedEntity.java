package com.mzlion.poi.annotation;

import java.lang.annotation.*;

/**
 * 针对JavaBean的属性类型也是JavaBean时注解
 *
 * @author mzlion
 * @date 2016-06-11.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelMappedEntity {

    /**
     * 需要解析属性列表，不可为空
     */
    String[] propertyNames() default {"*"};

    /**
     * 对应的目标类型
     */
    Class<?> targetClass();

}
