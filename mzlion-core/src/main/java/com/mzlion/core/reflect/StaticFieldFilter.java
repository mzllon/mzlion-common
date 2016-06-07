package com.mzlion.core.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * <p>
 * 2016-06-07 06:54 静态的field过滤器实现
 * </p>
 *
 * @author mzlion
 */
public class StaticFieldFilter implements FieldFilter {

    /**
     * 判断field是否需要过滤
     *
     * @param field 被检查的field
     * @return 如果为{@code true}则会过滤
     */
    @Override
    public boolean matches(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }
}
