package com.mzlion.core.json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mzlion.core.lang.StringUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 2016-04-14 {@code JsonUtil}工具类是基于<a href="http://">Google Gson</a>简单封装，提供了Java对象和Json字符串的相互转换。
 * </p>
 *
 * @author mzlion
 * @see Gson
 */
public abstract class JsonUtil {

    //gson对象
    private static Gson gson;

    /**
     * 实例化Gson
     */
    static {
        gson = new GsonBuilder().serializeNulls().create();
    }

    /**
     * 将对象转为Json字符串
     *
     * @param value 对象
     * @param <T>   泛型类
     * @return json字符串或{@code null}
     */
    public static <T> String toJson(T value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return gson.toJson(value);
    }

    /**
     * 将Json字符串转为对象
     *
     * @param json  Json字符串
     * @param clazz 泛型类类型
     * @param <T>   泛型类
     * @return Java对象
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        if (clazz == null) {
            return gson.fromJson(json, new TypeToken<Map<String, Object>>() {
            }.getType());
        }
        return gson.fromJson(json, clazz);
    }

    /**
     * 将Json字符串转为对象
     *
     * @param json    Json字符串
     * @param typeOfT Java的实际类型
     * @param <T>     泛型类型
     * @return 对象
     */
    public static <T> T toBean(String json, Type typeOfT) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return gson.fromJson(json, typeOfT);
    }

    /**
     * 将Json字符串转为集合对象
     *
     * @param json Json字符串
     * @param <T>  泛型类
     * @return 集合对象
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) return null;
        return gson.fromJson(json, new ListParameterizedType(clazz));
    }
}

