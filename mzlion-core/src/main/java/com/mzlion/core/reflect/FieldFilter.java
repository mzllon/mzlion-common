package com.mzlion.core.reflect;

import java.lang.reflect.Field;

/**
 * <p>
 * 2016-06-07 06:52 field过滤器
 * </p>
 *
 * @author mzlion
 */
public interface FieldFilter {

    /**
     * 判断field是否需要过滤
     *
     * @param field 被检查的field
     * @return 如果为{@code true}则会过滤
     */
    boolean matches(Field field);

}
