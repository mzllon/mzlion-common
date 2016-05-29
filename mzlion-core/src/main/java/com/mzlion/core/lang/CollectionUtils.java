/*
*/
package com.mzlion.core.lang;

import java.util.*;

/**
 * <p>
 * 2016-05-05 22:05 集合框架的工具类
 * </p>
 *
 * @author mzlion
 */
public abstract class CollectionUtils {

    /**
     * 判断集合是否为空
     * <pre class="code">CollectionUtils.isEmpty(list);</pre>
     *
     * @param collection 集合
     * @return 如果集合为{@code null}或为空是则返回{@code true}，否则返回{@code false}
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 判断map是否为空
     * <pre class="code">CollectionUtils.isEmpty(hashmap);</pre>
     *
     * @param map map集合
     * @return 如果map为{@code null}或为空是则返回{@code true}，否则返回{@code false}
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 判断集合是否为不为空
     * <pre class="code">CollectionUtils.isNotEmpty(list);</pre>
     *
     * @param collection 集合
     * @return 如果集合不为{@code null}且不为空是则返回{@code true}，否则返回{@code false}
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断map是否为不为空
     * <pre class="code">CollectionUtils.isNotEmpty(hashmap);</pre>
     *
     * @param map map集合
     * @return 如果map不为{@code null}且不为空是则返回{@code true}，否则返回{@code false}
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 将数组的内容转换为集合对象，注意该集合对象的可以修改的，
     * 注意区别JDK API{@linkplain Arrays#asList(Object[])}不同。
     * 如果数组为{@code null}或空，则会返回一个空的集合对象
     * <p>
     * 该方法相当于提供了一种创建集合对象并且集合里面已经包含了一定的数据。
     * </p>
     * <pre>
     *     List&lt;String&gt; stooges = CollectionUtils.asList("Larry", "Moe", "Curly");
     * </pre>
     *
     * @param values 数组
     * @param <T>    泛型类型
     * @return 集合对象
     */
    public static <T> List<T> asList(T... values) {
        if (ArrayUtils.isEmpty(values)) {
            return new ArrayList<>(0);
        }
        List<T> list = new ArrayList<>(values.length);
        for (T element : values) {
            list.add(element);
        }
        return list;
    }

    /**
     * 将数组一一映射转换为Map对象，思路来自于Python中的字典，如果两个数组的长度不一致，则取较短的那个数组。
     * <p>
     * 所以一一映射，根据举例能够说明的更清楚。
     * <pre>
     *         keys =    [a,b,c]
     * 		   values = [1,2,3,4]
     * 		   则达到的Map值应该为{a=1,b=2,c=3}
     *     </pre>
     * </p>
     *
     * @param keys   键列表
     * @param values 值列表
     * @param <K>    泛型类型
     * @param <V>    泛型类型
     * @return 返回键值对应的Map对象
     */
    public static <K, V> Map<K, V> asMap(K[] keys, V[] values) {
        if (ArrayUtils.isEmpty(keys) || ArrayUtils.isEmpty(values)) {
            return new HashMap<>(0);
        }

        //两个数组只能取长度较短的那个至
        final int size = Math.min(keys.length, values.length);
        Map<K, V> map = new HashMap<>(size);

        for (int index = 0; index < size; index++) {
            map.put(keys[index], values[index]);
        }
        return map;
    }

    /**
     * 键值映射，将{@code keyStr}和{@code valueStr}按照{@code delimiter}分隔符分割，然后一一映射为Map对象。
     * <p>
     * 该方法在Web应用程序下比较适合使用，如果两个分割后的长度不一致，则取较短的部分。
     * 以下是示例说明：
     * <pre>
     *   keys =    a,b,c
     * 	 values = 1,2,3,4
     * 	 分割符合为逗号","
     * 	 则达到的Map值应该为{a=1,b=2,c=3}
     *  </pre>
     * </p>
     *
     * @param keyStr    键列表
     * @param valueStr  值列表
     * @param delimiter 分隔符
     * @return 返回键值对应的Map对象
     */
    public static Map<String, String> asMap(String keyStr, String valueStr, String delimiter) {
        if (StringUtils.isEmpty(keyStr) || StringUtils.isEmpty(valueStr)) {
            return new HashMap<>(0);
        }
        if (StringUtils.isEmpty(delimiter)) {
            throw new IllegalArgumentException("The delimiter is invalid!");
        }

        String[] keys = keyStr.split(delimiter);
        String[] values = valueStr.split(delimiter);

        return asMap(keys, values);
    }

    /**
     * 将url中的请求参数转为Map对象
     * <pre>
     *     String urlParams = "username=admin&password=123456";
     *     Map<String,String> urlMap = </>CollectionUtils.urlParam2Map(urlParams);
     *     //那么得到的结果为：{"username":"admin","password":"123456"}
     * </pre>
     *
     * @param urlParam url请求参数
     * @return 返回Map对象
     */
    public static Map<String, String> urlParam2Map(String urlParam) {
        if (StringUtils.isEmpty(urlParam)) {
            return Collections.emptyMap();
        }
        String[] arr = StringUtils.split(urlParam, "&");
        if (ArrayUtils.isEmpty(arr)) {
            return Collections.emptyMap();
        }

        Map<String, String> params = new HashMap<>(arr.length);
        for (String param : arr) {
            if (StringUtils.hasText(param)) {
                String[] kv = StringUtils.split(param, "=");
                if (StringUtils.hasLength(kv[0])) {
                    params.put(kv[0], kv[1]);
                }
            }
        }
        return params;
    }

}
