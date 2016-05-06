package com.mzlion.core.lang;

/**
 * Created by mzlion on 2016/5/6.
 */
public class Assert {

    /**
     * 校验对象不能为null
     *
     * @param object  被检查的对象
     * @param message 校验失败时异常消息
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
