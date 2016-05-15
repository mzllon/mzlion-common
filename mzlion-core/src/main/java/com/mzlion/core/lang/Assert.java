package com.mzlion.core.lang;

import java.util.Collection;
import java.util.Map;

/**
 * Created by mzlion on 2016/5/6.
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

}
