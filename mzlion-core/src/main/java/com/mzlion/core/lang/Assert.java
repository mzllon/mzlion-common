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
     * Assert a boolean expression,throwing {@code IllegalArgumentException} if the test result is {@code false}.
     * <pre class="code">Assert.isTrue(i &gt; 0, "The value must be greater than zero");</pre>
     *
     * @param expression a boolean expression
     * @param message    the detail message.
     * @throws IllegalArgumentException
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) throw new IllegalArgumentException(message);
    }

    /**
     * Assert an object value is not {@code null},throwing {@code IllegalArgumentException} if the test result is {@code false}.
     * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
     *
     * @param data    the object to check
     * @param message the detail message.
     * @throws IllegalArgumentException
     */
    public static void notNull(Object data, String message) {
        if (data == null) throw new IllegalArgumentException(message);
    }

    /**
     * assert the given string is not empty.
     * <pre class="code">Assert.hasLength(name, "Name must not be empty");</pre>
     *
     * @param text    the string to check
     * @param message the detail message.
     * @throws IllegalArgumentException
     */
    public static void hasLength(String text, String message) {
        if (StringUtils.isEmpty(text)) throw new IllegalArgumentException(message);
    }

    /**
     * Assert that the collection has one element at least.
     * <pre class="code">Assert.notEmpty(collection, "Collection must have elements");</pre>
     *
     * @param collection the collection to check
     * @param message    the detail message.
     * @throws IllegalArgumentException
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) throw new IllegalArgumentException(message);
    }

    /**
     * Assert that the map has one entry at least.
     * <pre class="code">Assert.notEmpty(map, "Map must have entries");</pre>
     *
     * @param map     the map to check
     * @param message the detail message.
     * @throws IllegalArgumentException
     */
    public static void notEmpty(Map<?, ?> map, String message) {
        if (CollectionUtils.isEmpty(map)) throw new IllegalArgumentException(message);
    }

    /**
     * Assert that the array has one element at least.
     * <pre class="code">Assert.notEmpty(array, "The array must have elements");</pre>
     *
     * @param array   the array to check
     * @param message the detail message.
     * @param <T>     generic type
     * @throws IllegalArgumentException
     */
    public static <T> void notEmpty(T[] array, String message) {
        if (null == array || array.length == 0) throw new IllegalArgumentException(message);
    }

    /**
     * Assert that the array has one char element at least.
     * <pre class="code">Assert.notEmpty(new char[]{}, "The array must have elements");</pre>
     *
     * @param array   the array to check
     * @param message the detail message.
     * @throws IllegalArgumentException
     */
    public static void notEmpty(char[] array, String message) {
        if (null == array || array.length == 0) throw new IllegalArgumentException(message);
    }

    /**
     * Assert that the array has one boolean element at least.
     * <pre class="code">Assert.notEmpty(new boolean[]{}, "The array must have elements");</pre>
     *
     * @param array   the array to check
     * @param message the detail message.
     * @throws IllegalArgumentException
     */
    public static void notEmpty(boolean[] array, String message) {
        if (null == array || array.length == 0) throw new IllegalArgumentException(message);
    }

    /**
     * Assert that the array has one byte element at least.
     * <pre class="code">Assert.notEmpty(new byte[]{}, "The array must have elements");</pre>
     *
     * @param array   the array to check
     * @param message the detail message.
     * @throws IllegalArgumentException
     */
    public static void notEmpty(byte[] array, String message) {
        if (null == array || array.length == 0) throw new IllegalArgumentException(message);
    }

    /**
     * Assert that the array has one short element at least.
     * <pre class="code">Assert.notEmpty(new short[]{}, "The array must have elements");</pre>
     *
     * @param array   the array to check
     * @param message the detail message.
     * @throws IllegalArgumentException
     */
    public static void notEmpty(short[] array, String message) {
        if (null == array || array.length == 0) throw new IllegalArgumentException(message);
    }

    /**
     * Assert that the array has one int element at least.
     * <pre class="code">Assert.notEmpty(new int[]{}, "The array must have elements");</pre>
     *
     * @param array   the array to check
     * @param message the detail message.
     * @throws IllegalArgumentException
     */
    public static void notEmpty(int[] array, String message) {
        if (null == array || array.length == 0) throw new IllegalArgumentException(message);
    }

    /**
     * Assert that the array has one long element at least.
     * <pre class="code">Assert.notEmpty(new long[]{}, "The array must have elements");</pre>
     *
     * @param array   the array to check
     * @param message the detail message.
     * @throws IllegalArgumentException
     */
    public static void notEmpty(long[] array, String message) {
        if (null == array || array.length == 0) throw new IllegalArgumentException(message);
    }

    /**
     * Assert that the array has one float element at least.
     * <pre class="code">Assert.notEmpty(new float[]{}, "The array must have elements");</pre>
     *
     * @param array   the array to check
     * @param message the detail message.
     * @throws IllegalArgumentException
     */
    public static void notEmpty(float[] array, String message) {
        if (null == array || array.length == 0) throw new IllegalArgumentException(message);
    }

    /**
     * Assert that the array has one doube element at least.
     * <pre class="code">Assert.notEmpty(new double[]{}, "The array must have elements");</pre>
     *
     * @param array   the array to check
     * @param message the detail message.
     * @throws IllegalArgumentException
     */
    public static void notEmpty(double[] array, String message) {
        if (null == array || array.length == 0) throw new IllegalArgumentException(message);
    }

}
