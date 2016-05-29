package com.mzlion.core.lang;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 测试类
 * <p>
 * Created by mzlion on 2016/5/23.
 */
public class ArrayUtilsTest {

    @Test
    public void isArray() throws Exception {
        assertFalse(ArrayUtils.isArray(null));
        assertFalse(ArrayUtils.isArray(new Object()));
        assertTrue(ArrayUtils.isArray(new Object[]{}));
    }

    @Test
    public void isEmpty() throws Exception {
        assertTrue(ArrayUtils.isEmpty(new char[]{}));
    }

    @Test
    public void isEmpty1() throws Exception {
        assertTrue(ArrayUtils.isEmpty(new boolean[]{}));
    }

    @Test
    public void isEmpty2() throws Exception {
        assertTrue(ArrayUtils.isEmpty(new byte[]{}));
    }

    @Test
    public void isEmpty3() throws Exception {
        assertTrue(ArrayUtils.isEmpty(new short[]{}));
    }

    @Test
    public void isEmpty4() throws Exception {
        assertTrue(ArrayUtils.isEmpty(new int[]{}));
    }

    @Test
    public void isEmpty5() throws Exception {
        assertTrue(ArrayUtils.isEmpty(new long[]{}));
    }

    @Test
    public void isEmpty6() throws Exception {
        assertTrue(ArrayUtils.isEmpty(new float[]{}));
    }

    @Test
    public void isEmpty7() throws Exception {
        assertTrue(ArrayUtils.isEmpty(new double[]{}));
    }

    @Test
    public void isEmpty8() throws Exception {
        assertTrue(ArrayUtils.isEmpty(new String[]{}));
    }

    @Test
    public void isNotEmpty() throws Exception {
        assertFalse(ArrayUtils.isNotEmpty(new String[]{}));
        assertTrue(ArrayUtils.isNotEmpty(new String[]{"mzlion"}));
    }

    @Test
    public void isEmptyElement() throws Exception {
        assertTrue(ArrayUtils.isEmptyElement(null));
        assertTrue(ArrayUtils.isEmptyElement(new String[]{}));
        assertFalse(ArrayUtils.isEmptyElement(new String[]{"mzlion"}));
        assertTrue(ArrayUtils.isEmptyElement(new String[]{"mzlion", ""}));
    }

    @Test
    public void containsElement() throws Exception {
        assertTrue(ArrayUtils.containsElement(new String[]{"I", "am", "a", "coder"}, "am"));
    }

    @Test
    public void toString1() throws Exception {
        assertEquals("andy,mzlion", ArrayUtils.toString(new String[]{"andy", "mzlion"}));
    }

    @Test
    public void toString2() throws Exception {
        assertEquals("andy|mzlion", ArrayUtils.toString(new String[]{"andy", "mzlion"}, "|"));
    }

    @Test
    public void toString3() throws Exception {
        assertEquals("男,女", ArrayUtils.toString(new char[]{'男', '女'}));
    }

    @Test
    public void toString4() throws Exception {
        assertEquals("男|女", ArrayUtils.toString(new char[]{'男', '女'}, "|"));
    }

    @Test
    public void toString5() throws Exception {
        assertEquals("true,false", ArrayUtils.toString(new boolean[]{true, false}));
    }

    @Test
    public void toString6() throws Exception {
        assertEquals("true|false", ArrayUtils.toString(new boolean[]{true, false}, "|"));
    }

    @Test
    public void toString7() throws Exception {
        assertEquals("122,110", ArrayUtils.toString(new byte[]{122, 110}));
    }

    @Test
    public void toString8() throws Exception {
        assertEquals("122|110", ArrayUtils.toString(new byte[]{122, 110}, "|"));
    }

    @Test
    public void toString9() throws Exception {
        assertEquals("1330,1440", ArrayUtils.toString(new short[]{1330, 1440}));
    }

    @Test
    public void toString10() throws Exception {
        assertEquals("1330|1440", ArrayUtils.toString(new short[]{1330, 1440}, "|"));
    }

    @Test
    public void toString11() throws Exception {
        assertEquals("1330,1440", ArrayUtils.toString(new int[]{1330, 1440}));
    }

    @Test
    public void toString12() throws Exception {
        assertEquals("1330|1440", ArrayUtils.toString(new int[]{1330, 1440}, "|"));
    }

    @Test
    public void toString13() throws Exception {
        assertEquals("1330,1440", ArrayUtils.toString(new long[]{1330, 1440}));
    }

    @Test
    public void toString14() throws Exception {
        assertEquals("1330|1440", ArrayUtils.toString(new long[]{1330, 1440}, "|"));

    }

    @Test
    public void toString15() throws Exception {
        assertEquals("1330.1,1440.2", ArrayUtils.toString(new float[]{1330.1f, 1440.2f}));
    }

    @Test
    public void toString16() throws Exception {
        assertEquals("1330.1|1440.2", ArrayUtils.toString(new float[]{1330.1f, 1440.2f}, "|"));
    }

    @Test
    public void toString17() throws Exception {
        assertEquals("1330.1,1440.2", ArrayUtils.toString(new double[]{1330.1, 1440.2}));
    }

    @Test
    public void toString18() throws Exception {
        assertEquals("1330.1|1440.2", ArrayUtils.toString(new double[]{1330.1, 1440.2}, "|"));
    }

}