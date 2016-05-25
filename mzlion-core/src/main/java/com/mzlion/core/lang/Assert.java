package com.mzlion.core.lang;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 2016-05-25 参数校验工具类
 * </p>
 *
 * @author mzlion
 */
public class Assert {

    /**
     * 校验对象不能为null
     *
     * @param data    被检查的对象
     * @param message 校验失败时异常消息
     */
    public static void assertNotNull(Object data, String message) {
        if (data == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 校验字符串非空
     *
     * @param text    被检查的字符串
     * @param message 校验失败时异常消息
     */
    public static void assertHasLength(String text, String message) {
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 校验集合对象非空
     *
     * @param collection 被检查的集合对象
     * @param message    校验失败时异常消息
     */
    public static void assertNotEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 校验Map非空
     *
     * @param map     被检查的集合对象
     * @param message 校验失败时异常消息
     */
    public static void assertNotEmpty(Map<?, ?> map, String message) {
        if (CollectionUtils.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 校验数组非null及非空数组
     *
     * @param array   被检查的数组
     * @param message 校验失败时异常消息
     * @param <T>     泛型类
     */
    public static <T> void assertNotEmpty(T[] array, String message) {
        if (null == array || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 校验数组非null及非空数组
     *
     * @param array   被检查的数组
     * @param message 校验失败时异常消息
     */
    public static void assertNotEmpty(int[] array, String message) {
        if (null == array || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 校验数组非null及非空数组
     *
     * @param array   被检查的数组
     * @param message 校验失败时异常消息
     */
    public static void assertNotEmpty(long[] array, String message) {
        if (null == array || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 校验数组非null及非空数组
     *
     * @param array   被检查的数组
     * @param message 校验失败时异常消息
     */
    public static void assertNotEmpty(byte[] array, String message) {
        if (null == array || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 校验数组非null及非空数组
     *
     * @param array   被检查的数组
     * @param message 校验失败时异常消息
     */
    public static void assertNotEmpty(char[] array, String message) {
        if (null == array || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 校验数组非null及非空数组
     *
     * @param array   被检查的数组
     * @param message 校验失败时异常消息
     */
    public static void assertNotEmpty(short[] array, String message) {
        if (null == array || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 校验数组非null及非空数组
     *
     * @param array   被检查的数组
     * @param message 校验失败时异常消息
     */
    public static void assertNotEmpty(float[] array, String message) {
        if (null == array || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 校验数组非null及非空数组
     *
     * @param array   被检查的数组
     * @param message 校验失败时异常消息
     */
    public static void assertNotEmpty(double[] array, String message) {
        if (null == array || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 校验数组非null及非空数组
     *
     * @param array   被检查的数组
     * @param message 校验失败时异常消息
     */
    public static void assertNotEmpty(boolean[] array, String message) {
        if (null == array || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }


}
