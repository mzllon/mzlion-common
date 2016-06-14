package com.mzlion.core.lang;

import org.junit.Test;

import java.util.Collections;

/**
 * Testing for {@linkplain Assert}
 * <p>
 * Created by mzlion on 2016/5/29.
 */
public class AssertTest {

    @Test(expected = IllegalArgumentException.class)
    public void assertNotNull() throws Exception {
        Assert.notNull(null, "Data must not be null.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertHasLength() throws Exception {
        Assert.hasLength(null, "Text must not be null.");
        Assert.hasLength("", "Text must not be null.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertNotEmpty() throws Exception {
        Assert.notEmpty(Collections.emptyList(), "Collection must not be null or empty.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertNotEmpty1() throws Exception {
        Assert.notEmpty(Collections.emptyMap(), "Map must not be null or empty.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertNotEmpty2() throws Exception {
        Assert.notEmpty(new String[]{}, "Array must not be null or empty.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertNotEmpty3() throws Exception {
        Assert.notEmpty(new char[]{}, "Array must not be null or empty.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertNotEmpty4() throws Exception {
        Assert.notEmpty(new boolean[]{}, "Array must not be null or empty.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertNotEmpty5() throws Exception {
        Assert.notEmpty(new byte[]{}, "Array must not be null or empty.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertNotEmpty6() throws Exception {
        Assert.notEmpty(new short[]{}, "Array must not be null or empty.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertNotEmpty7() throws Exception {
        Assert.notEmpty(new int[]{}, "Array must not be null or empty.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertNotEmpty8() throws Exception {
        Assert.notEmpty(new long[]{}, "Array must not be null or empty.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertNotEmpty9() throws Exception {
        Assert.notEmpty(new float[]{}, "Array must not be null or empty.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertNotEmpty10() throws Exception {
        Assert.notEmpty(new double[]{}, "Array must not be null or empty.");
    }

}