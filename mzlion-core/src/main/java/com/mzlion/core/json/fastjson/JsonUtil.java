/*
*/
package com.mzlion.core.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mzlion.core.lang.ArrayUtils;
import com.mzlion.core.lang.Assert;

import java.util.List;
import java.util.Map;

/**
 * 2016-04-14 {@code JsonUtil}类是基于<a href="https://github.com/alibaba/fastjson" style="color:red;font-size:14px;">fastjson</a>进行一层封装，提供Java对象和json字符串之间的相互转换。
 * 由于fastjson较Jackson，gson等解析JSON的jar出现较晚，吸收了实际应用中的一些特点，
 * 所以可以认为fastjson相对效率较高。
 * <p>请注意：本工具类提供的方法默认禁用了循环引用的问题，就不会出现$ref等字符。所谓循环引用一般出现在引用同一个对象（对象重复），对象自引用（父子关系都是一个类）
 * 没有禁用循环引用时的json字符串为：[{"group":{"groupName":"Test组","id":1},"hobbies":["游泳","上网"],"id":1,"password":"123456"},{"group":{"groupName":"Test组","id":2},"hobbies":["电影","book"],"id":2,"password":"123456"},{"birthDay":"1992-02-12 15:32:09","group":{"$ref":"$[1].group"},"hobbies":["电影","book"],"id":3,"password":"xxxxxx"}]
 * 禁用了循环引用后的json字符串为：[{"group":{"groupName":"Test组","id":1},"hobbies":["游泳","上网"],"id":1,"password":"123456"},{"group":{"groupName":"Test组","id":2},"hobbies":["电影","book"],"id":2,"password":"123456"},{"birthDay":"1992-02-12 15:31:28","group":{"groupName":"Test组","id":2},"hobbies":["电影","book"],"id":3,"password":"xxxxxx"}]
 * </p>
 * <p>本工具类仅封装常用的相互转换方法，包括属性过滤等方法。fastjson提供很多高级方法，这个需要自己阅读官网文档。</p>
 *
 * @author mzlion
 * @see <a href="https://github.com/alibaba/fastjson">fastjson</a>
 */
public abstract class JsonUtil {

    //---------------------------------------------------------------------
    // convert Java instance to JSON String
    // ---------------------------------------------------------------------


    /**
     * 将Java对象转换JSON字符串
     * <pre>
     *    User user = new User("admin","123456");
     *    JsonUtil.toJson(user);
     * </pre>
     *
     * @param value Java对象
     * @return json字符串
     */
    public static String toJson(Object value) {
        return JSON.toJSONString(value, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.BrowserCompatible);
    }

    /**
     * 将Java对象转换JSON字符串。请注意这里的属性过滤会过滤掉所有类中的属性名。
     * 也就是说假设类User中有name和group，而类Group也有name属性。那么如果过滤属性name，那么User和Group中的name都会过滤掉。
     * 如果想要针对某个类的属性进行过滤请使用{@link #toJson(Object, Map)}
     * <pre>
     *    User user = new User("admin","123456");
     *    JsonUtil.toJson(user,"username");// ---> {"password":"123456"}
     * </pre>
     *
     * @param value         Java对象
     * @param propertyNames 需要过滤的属性列表
     * @return json字符串
     */
    public static String toJson(Object value, final String... propertyNames) {
        PropertyFilter filter = new PropertyFilter() {
            @Override
            public boolean apply(Object object, String name, Object value) {
                return !ArrayUtils.containsElement(propertyNames, name);
            }
        };
        return JSON.toJSONString(value, filter, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.BrowserCompatible);
    }

    /**
     * 将Java对象转换为JSON字符串，会过滤掉指定类中的属性，不会污染到其它类中的同名属性。
     * <pre>
     * Map<Class<?>, List<String>> map = new HashMap<Class<?>, List<String>>(2);
     * map.put(Person.class, Arrays.asList("username"));
     * map.put(Group.class, Arrays.asList("id"));
     * System.out.println(toJson(list, map));
     * </pre>
     *
     * @param value        Java对象
     * @param classOfProps 需要过滤的属性列表
     * @return json字符串
     */
    public static String toJson(Object value, final Map<Class<?>, List<String>> classOfProps) {
        return JSON.toJSONString(value, new PropertyFilter() {
            @Override
            public boolean apply(Object object, String name, Object value) {
                List<String> props = classOfProps.get(object.getClass());
                return !props.contains(name);
            }
        }, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.BrowserCompatible);
    }

    /**
     * 将Java对象转换为JSON字符串，该方法提供了开放的转换规则，比如出现了死循环而导致了结果不正确。<br/>
     * {@link com.alibaba.fastjson.serializer.SerializerFeature}对象枚举
     * <ul>
     * <li>{@link com.alibaba.fastjson.serializer.SerializerFeature#DisableCircularReferenceDetect} 禁用循环引用</li>
     * <li>{@link com.alibaba.fastjson.serializer.SerializerFeature#WriteClassName} json序列化时将类信息保存以便反序列化时可以自动获取类信息</li>
     * <li>{@link com.alibaba.fastjson.serializer.SerializerFeature#BeanToArray} 将Java对象转换为数组，这可能已经不是json序列化的范围之内</li>
     * <li>{@link com.alibaba.fastjson.serializer.SerializerFeature#PrettyFormat} 用于显示格式化过的json字符串，一般用于输出控制台效果较好。</li>
     * <li>{@link com.alibaba.fastjson.serializer.SerializerFeature#UseSingleQuotes} 使用单引号，默认使用双引号</li>
     * <li>{@link com.alibaba.fastjson.serializer.SerializerFeature#WriteDateUseDateFormat} 日期格式化输出，日期格式为：yyyy-MM-dd HH:mm:ss</li>
     * <li>{@link com.alibaba.fastjson.serializer.SerializerFeature#BrowserCompatible} 解决中文乱码，会转成Unicode码</li>
     * </ul>
     * 该对象枚举值非常多，这里仅仅提供一些常用的枚举值作为介绍。
     *
     * @param value    Java对象
     * @param features 序列化规则
     * @return json字符串
     */
    public static String toJson(Object value, SerializerFeature... features) {
        Assert.assertNotNull(features, "Array SerializerFeature is null or empty.");
        return JSON.toJSONString(value, features);
    }

    /**
     * 将Java对象转换为json字符串，可以自定义日期格式
     *
     * @param value       Java对象
     * @param datePattern 输出的日期格式
     * @return json字符串
     */
    public static String toJsonWithDateFormat(Object value, String datePattern) {
        return JSON.toJSONStringWithDateFormat(value, datePattern,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.BrowserCompatible);
    }


    //---------------------------------------------------------------------
    // convert JSON String to Java instance
    // ---------------------------------------------------------------------

    /**
     * 将json字符串转为Java对象
     *
     * @param json  json字符串
     * @param clazz 转换的类型
     * @param <T>   泛型类型
     * @return Java对象
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * 将复杂的json字符串转为Java对象
     *
     * @param json 复杂的json字符串
     * @param type 转换的类型的实例
     * @param <T>  泛型类型
     * @return Java对象
     */
    public static <T> T toBean(String json, TypeReference<T> type) {
        return JSON.parseObject(json, type);
    }

    /**
     * 向控制台打印格式化过的json字符串
     *
     * @param value Java对象
     */
    public static void println(Object value) {
        System.out.println(toJson(value, SerializerFeature.PrettyFormat, SerializerFeature.BrowserCompatible));
    }
}
