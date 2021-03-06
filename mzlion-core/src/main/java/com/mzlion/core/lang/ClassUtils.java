package com.mzlion.core.lang;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by mzlion on 2016/5/23.
 */
public class ClassUtils {
    /**
     * Map with primitive wrapper type as key and corresponding primitive
     * type as value, for example: Integer.class -> int.class.
     */
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<>(8);

    /**
     * Map with primitive type as key and corresponding wrapper
     * type as value, for example: int.class -> Integer.class.
     */
    private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new IdentityHashMap<>(8);

    static {
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);

        for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperTypeMap.entrySet()) {
            primitiveTypeToWrapperMap.put(entry.getValue(), entry.getKey());
        }
    }

    /**
     * 判断sourceType是否是targetType父类或接口，其类的本身
     *
     * @param sourceType 被检查的类型
     * @param targetType 被检查的类型
     * @return 如果是则返回{@code true}，否则返回{@code false}
     * @see Class#isAssignableFrom(Class)
     */
    public static boolean isAssignable(Class<?> sourceType, Class<?> targetType) {
        Assert.notNull(sourceType, "Source type must not be null");
        Assert.notNull(targetType, "Target side type must not be null");
        if (sourceType.isAssignableFrom(targetType)) {
            return true;
        }
        if (sourceType.isPrimitive()) {
            Class<?> resolvedPrimitive = primitiveWrapperTypeMap.get(targetType);
            if (sourceType == resolvedPrimitive) {
                return true;
            }
        } else {
            Class<?> resolvedWrapper = primitiveTypeToWrapperMap.get(targetType);
            if (resolvedWrapper != null && sourceType.isAssignableFrom(resolvedWrapper)) {
                return true;
            }
        }
        return false;
    }
}
