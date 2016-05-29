package com.mzlion.core.lang;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by mzlion on 2016/5/29.
 */
public class ClassUtilsTest {

    @Test
    public void isAssignable() throws Exception {
        assertTrue(ClassUtils.isAssignable(Integer.class, int.class));
        assertTrue(ClassUtils.isAssignable(Comparable.class, String.class));
        assertFalse(Integer.class.isAssignableFrom(int.class));
    }

}