/*
*/
package com.mzlion.core.json.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 该Jackson提供的JSON解析不推荐使用了，因为其速度与Gson和Fastjson不相上下甚至略逊，其次相关Jar包太大。
 * 推挤使用谷歌提供的Gson工具。
 * </p>
 * 2016-04-14 JSON的工具类：提供了将Java类转为json字符串和json字符串解析为Java对象。
 * 底层采用的是性能较高的Jackson转换作为支撑，并且对字符串转换为Unicode码后再输出。
 * 使用该工具需要导入jackson-core-asl.jar,jackson-mapper-asl.jar两个包
 * <p>
 * 以下是该工具类的简单使用方法示例：
 * <pre class="code">
 * User{username,password,nickname}; //--->User类的属性
 * User u = new User("admin","123456","管理员");
 * String json = JsonUtil.toJson(u);
 * //print:-->'{"username":"admin","password":"123456","nickname":"\u7BA1\u7406\u5458"}'
 * json = JsonUtil.toJson(u,User.class,"nickname");
 * //print:-->'{"username":"admin","password":"123456"}'
 * </pre>
 *
 * @author mzlion
 * @see <a href="http://jackson.codehaus.org/">jackson</a>
 * @deprecated
 */
public abstract class JsonUtil {

    //日志记录
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    //Jackson解析和转换类
    private static ObjectMapper objectMapper;
    private static ObjectMapper unicodeObjectMapper;

    static {
        objectMapper = new ObjectMapper();
        unicodeObjectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(String.class, new StringUnicodeSerializer());
        unicodeObjectMapper.registerModule(simpleModule);
    }

    /**
     * 将json字符串转换为对象
     * <pre>
     *    JsonUtil.fromJson("{\"username\":\"username",\"password\":\"123456\"}",User.class);
     * </pre>
     *
     * @param jsonStr json字符串
     * @param clazz   对象的类型，默认返回Map类型
     * @param <T>     泛型类型
     * @return 返回对象，如果解析失败时则返回<code>null</code>
     */
    public static <T> T toBean(String jsonStr, Class<T> clazz) {
        if (jsonStr == null) {
            return null;
        }
        try {
            if (clazz == null) {
                return objectMapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {
                });
            } else {
                return objectMapper.readValue(jsonStr, clazz);
            }
        } catch (IOException e) {
            logger.error(String.format("parse json string error:%s", jsonStr), e);
            return null;
        }
    }

    /**
     * 将json字符串转换为集合对象
     * <p/>
     * <pre>
     *     JsonUtil.fromJson("[{\"username\":\"uname1",\"password\":\"123456\"},{\"username\":\"uname2",\"password\":\"123456\"}]",new TypeReference<List<User>>(){});
     * </pre>
     *
     * @param jsonStr       json字符串
     * @param typeReference 引用类型对象
     * @param <T>           泛型类型
     * @return 返回集合对象
     */
    public static <T> T toBean(String jsonStr, TypeReference<T> typeReference) {
        if (jsonStr == null) {
            return null;
        }
        if (typeReference == null) {
            logger.error(" ===> typeReference参数不能为空.");
            return null;
        }
        try {
            return objectMapper.readValue(jsonStr, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(String.format("parse json string error:%s", jsonStr), e);
            return null;
        }
    }

    /**
     * 将对象转为json字符串
     * <p/>
     * <pre>
     *     User user = new User("admin","123456");
     *    JsonUtil.toJson(user);
     * </pre>
     *
     * @param value java对象
     * @param <T>   泛型类型
     * @return 返回json字符串
     */
    public static <T> String toJson(T value) {
        return toJson(value, false);
    }

    /**
     * 将对象转为json字符串，非ASCII码均被转为Unicode码。
     * <p/>
     * <p>
     * 此方法会导致字符串的长度变长，但是在各个系统传输时不会出现乱码。
     * </p>
     * <pre>
     *     User user = new User("admin","123456");
     *    JsonUtil.toJson(user);
     * </pre>
     *
     * @param value java对象
     * @param <T>   泛型类型
     * @return 返回json字符串
     */
    public static <T> String toJsonWithUnicode(T value) {
        return toJson(value, true);
    }

    /**
     * 对象转JSON字符串
     *
     * @param value         对象
     * @param enableUnicode 如果为{@code true}则表示输出的字符串可能含有Unicode码
     * @param <T>           泛型类
     * @return 字符串
     * @see StringUnicodeSerializer
     */
    private static <T> String toJson(T value, boolean enableUnicode) {
        if (value == null) {
            return "";
        }
        if (value instanceof String) {
            return (String) value;
        }

        try {
            if (enableUnicode) {
                return unicodeObjectMapper.writeValueAsString(value);
            } else {
                return objectMapper.writeValueAsString(value);
            }
        } catch (JsonProcessingException e) {
            logger.error(String.format("written to string error:%s", value.toString()), e);
            return null;
        }
    }

}
