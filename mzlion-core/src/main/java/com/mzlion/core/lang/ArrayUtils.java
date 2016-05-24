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
     * 判断是否为空或者为{@code null}
     *
     * @param array 数组
     * @return 当数组为空或{@code null}时返回{@code true}
     */
    public static boolean isEmpty(final long[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断是否为空或者为{@code null}
     *
     * @param array 数组
     * @return 当数组为空或{@code null}时返回{@code true}
     */
    public static boolean isEmpty(final short[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断是否为空或者为{@code null}
     *
     * @param array 数组
     * @return 当数组为空或{@code null}时返回{@code true}
     */
    public static boolean isEmpty(final char[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断是否为空或者为{@code null}
     *
     * @param array 数组
     * @return 当数组为空或{@code null}时返回{@code true}
     */
    public static boolean isEmpty(final boolean[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断是否为空或者为{@code null}
     *
     * @param array 数组
     * @return 当数组为空或{@code null}时返回{@code true}
     */
    public static boolean isEmpty(final float[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断是否为空或者为{@code null}
     *
     * @param array 数组
     * @return 当数组为空或{@code null}时返回{@code true}
     */
    public static boolean isEmpty(final double[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断是否为空或者为{@code null}
     *
     * @param array 数组
     * @return 当数组为空或{@code null}时返回{@code true}
     */
    public static boolean isEmpty(final byte[] array) {
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

    /**
     * 将数组转为字符串，使用英文半角逗号连接
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(Object[] array) {
        return toString(array, ",");
    }

    /**
     * 将数组转为字符串，使用{@code delimiter}将元素连接起来
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(Object[] array, String delimiter) {
        if (isEmpty(array)) {
            return "";
        }
        if (delimiter == null) {
            delimiter = "";
        }
        StringBuilder builder = new StringBuilder();
        int length = array.length - 1;
        for (int i = 0; ; i++) {
            builder.append(String.valueOf(array[i]));
            if (i == length) {
                return builder.toString();
            }
            builder.append(delimiter);
        }
    }

    /**
     * 将数组转为字符串，使用英文半角逗号连接
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(int[] array) {
        return toString(array, ",");
    }

    /**
     * 将数组转为字符串，使用{@code delimiter}将元素连接起来
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(int[] array, String delimiter) {
        if (isEmpty(array)) {
            return "";
        }
        if (delimiter == null) {
            delimiter = "";
        }
        StringBuilder builder = new StringBuilder();
        int length = array.length - 1;
        for (int i = 0; ; i++) {
            builder.append(array[i]);
            if (i == length) {
                return builder.toString();
            }
            builder.append(delimiter);
        }
    }

    /**
     * 将数组转为字符串，使用英文半角逗号连接
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(long[] array) {
        return toString(array, ",");
    }

    /**
     * 将数组转为字符串，使用{@code delimiter}将元素连接起来
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(long[] array, String delimiter) {
        if (isEmpty(array)) {
            return "";
        }
        if (delimiter == null) {
            delimiter = "";
        }
        StringBuilder builder = new StringBuilder();
        int length = array.length - 1;
        for (int i = 0; ; i++) {
            builder.append(array[i]);
            if (i == length) {
                return builder.toString();
            }
            builder.append(delimiter);
        }
    }

    /**
     * 将数组转为字符串，使用英文半角逗号连接
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(short[] array, String delimiter) {
        if (isEmpty(array)) {
            return "";
        }
        if (delimiter == null) {
            delimiter = "";
        }
        StringBuilder builder = new StringBuilder();
        int length = array.length - 1;
        for (int i = 0; ; i++) {
            builder.append(array[i]);
            if (i == length) {
                return builder.toString();
            }
            builder.append(delimiter);
        }
    }

    /**
     * 将数组转为字符串，使用{@code delimiter}将元素连接起来
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(short[] array) {
        return toString(array, ",");
    }

    /**
     * 将数组转为字符串，使用英文半角逗号连接
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(char[] array, String delimiter) {
        if (isEmpty(array)) {
            return "";
        }
        if (delimiter == null) {
            delimiter = "";
        }
        StringBuilder builder = new StringBuilder();
        int length = array.length - 1;
        for (int i = 0; ; i++) {
            builder.append(array[i]);
            if (i == length) {
                return builder.toString();
            }
            builder.append(delimiter);
        }
    }

    /**
     * 将数组转为字符串，使用{@code delimiter}将元素连接起来
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(char[] array) {
        return toString(array, ",");
    }

    /**
     * 将数组转为字符串，使用英文半角逗号连接
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(byte[] array, String delimiter) {
        if (isEmpty(array)) {
            return "";
        }
        if (delimiter == null) {
            delimiter = "";
        }
        StringBuilder builder = new StringBuilder();
        int length = array.length - 1;
        for (int i = 0; ; i++) {
            builder.append(array[i]);
            if (i == length) {
                return builder.toString();
            }
            builder.append(delimiter);
        }
    }

    /**
     * 将数组转为字符串，使用{@code delimiter}将元素连接起来
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(byte[] array) {
        return toString(array, ",");
    }

    /**
     * 将数组转为字符串，使用英文半角逗号连接
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(boolean[] array, String delimiter) {
        if (isEmpty(array)) {
            return "";
        }
        if (delimiter == null) {
            delimiter = "";
        }
        StringBuilder builder = new StringBuilder();
        int length = array.length - 1;
        for (int i = 0; ; i++) {
            builder.append(array[i]);
            if (i == length) {
                return builder.toString();
            }
            builder.append(delimiter);
        }
    }

    /**
     * 将数组转为字符串，使用{@code delimiter}将元素连接起来
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(boolean[] array) {
        return toString(array, ",");
    }

    /**
     * 将数组转为字符串，使用英文半角逗号连接
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(float[] array, String delimiter) {
        if (isEmpty(array)) {
            return "";
        }
        if (delimiter == null) {
            delimiter = "";
        }
        StringBuilder builder = new StringBuilder();
        int length = array.length - 1;
        for (int i = 0; ; i++) {
            builder.append(array[i]);
            if (i == length) {
                return builder.toString();
            }
            builder.append(delimiter);
        }
    }

    /**
     * 将数组转为字符串，使用{@code delimiter}将元素连接起来
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(float[] array) {
        return toString(array, ",");
    }

    /**
     * 将数组转为字符串，使用英文半角逗号连接
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(double[] array, String delimiter) {
        if (isEmpty(array)) {
            return "";
        }
        if (delimiter == null) {
            delimiter = "";
        }
        StringBuilder builder = new StringBuilder();
        int length = array.length - 1;
        for (int i = 0; ; i++) {
            builder.append(array[i]);
            if (i == length) {
                return builder.toString();
            }
            builder.append(delimiter);
        }
    }

    /**
     * 将数组转为字符串，使用{@code delimiter}将元素连接起来
     *
     * @param array 数组
     * @return 如果数组为空则返回空字符串
     */
    public static String toString(double[] array) {
        return toString(array, ",");
    }

}
