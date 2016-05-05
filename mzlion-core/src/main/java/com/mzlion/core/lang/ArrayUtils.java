package com.mzlion.core.lang;


/**
 * <p>
 * 2016-05-05 22:15 数组工具类
 * </p>
 *
 * @author mzlion
 */
public class ArrayUtils {


    /**
     * 判断是否是数组类型
     * <p/>
     * <pre class="code">
     * ObjectUtils.isArray(null); //---> false;
     * ObjectUtils.isArray(new String[]{"aa","bb"}); //---> true
     * </pre>
     *
     * @param obj 对象
     * @return 如果是数组类型则返回{@code true},否则返回{@code false}
     */
    public static boolean isArray(Object obj) {
        return (obj != null && obj.getClass().isArray());
    }

    /**
     * 判断是否为空或者为{@code null}
     *
     * @param array 数组
     * @return 当数组为空或{@code null}时返回{@code true}
     */
    public static boolean isEmpty(final int[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断是否为空或为{@code null}
     *
     * @param array 数组
     * @param <T>   泛型类
     * @return 当数组为空或{@code null}时返回{@code true}
     */
    public static <T> boolean isEmpty(final T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断是否不为空或者不为{@code null}
     *
     * @param array 数组
     * @return 当数组不为空且不是{@code null}时返回{@code true}
     */
    public static boolean isNotEmpty(final int[] array) {
        return array != null && array.length != 0;
    }

    /**
     * 判断是否不为空或不为{@code null}
     *
     * @param array 数组
     * @param <T>   泛型类
     * @return 当数组不为空且不是{@code null}时返回{@code true}
     */
    public static <T> boolean isNotEmpty(final T[] array) {
        return array != null && array.length != 0;
    }

    /**
     * 判断数组里的元素是否为空
     *
     * @param array 数组
     * @param <T>   泛型类
     * @return 如果数组的元素中存在{@code null}或空则返回{@code true}
     */
    public static <T> boolean isEmptyElement(final T[] array) {
        boolean empty = isEmpty(array);
        if (!empty) {
            empty = false;
            for (T element : array) {
                if (element == null) {
                    empty = true;
                } else {
                    if (element instanceof String) {
                        empty = StringUtils.isEmpty((String) element);
                    }
                }
                if (empty) {
                    return true;
                }
            }
        }
        return empty;
    }

    /**
     * 判断数组中是否包含了指定的元素
     * <p/>
     * <pre class="code">
     * ObjectUtils.containsElement(new String[]{"aaaa","bbb","cc",null},null); //---> true
     * ObjectUtils.containsElement(new String[]{"aaaa","bbb","cc"},"cc"); //---> true
     * ObjectUtils.containsElement(new String[]{"aaaa","bbb","cc",null},"xx"); //---> false
     * </pre>
     *
     * @param array   数组
     * @param element 检查的元素对象
     * @return 如果数组中存在则返回{@code true},否则返回{@code false}
     * @see ObjectUtils#nullSafeEquals(Object, Object)
     */
    public static <T> boolean containsElement(T[] array, T element) {
        if (isEmpty(array)) {
            return false;
        }
        for (Object arrayEle : array) {
            if (ObjectUtils.nullSafeEquals(arrayEle, element)) {
                return true;
            }
        }
        return false;
    }

}
