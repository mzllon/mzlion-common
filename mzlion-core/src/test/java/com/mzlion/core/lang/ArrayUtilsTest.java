package com.mzlion.core.lang;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by mzlion on 2016/5/23.
 */
public class ArrayUtilsTest {
    @Test
    public void isArray() throws Exception {

    }

    @Test
    public void isEmpty() throws Exception {

    }

    @Test
    public void isEmpty1() throws Exception {

    }

    @Test
    public void isNotEmpty() throws Exception {

    }

    @Test
    public void isNotEmpty1() throws Exception {

    }

    @Test
    public void isEmptyElement() throws Exception {

    }

    @Test
    public void containsElement() throws Exception {

    }

    @Test
    public void toString2() throws Exception {
        String[] array = {"I", "admin"};
        String expectStr = "I,admin";
        assertEquals(expectStr, ArrayUtils.toString(array));
    }

    @Test
    public void toString1() throws Exception {
        int[] array = {1, 2, 3, 4};
        assertEquals("1,2,3,4", ArrayUtils.toString(array));
    }

}