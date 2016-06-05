package com.mzlion.core.lang;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * <p>
 * 2016-05-22 12:18 Testing for {@link ObjectUtils}
 * </p>
 *
 * @author mzlion
 */
public class ObjectUtilsTest {

    @Test
    public void nullSafeEquals() throws Exception {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(ObjectUtils.nullSafeEquals(arr1, arr2));
        int[] arr3 = {1, 2, 3, 4};
        assertFalse(ObjectUtils.nullSafeEquals(arr1, arr3));

        String[] str1 = {"andy", "mzlion"};
        String[] str2 = {"andy", "mzlion"};
        assertTrue(ObjectUtils.nullSafeEquals(str1, str2));
        String[] str3 = {"andy", "mzllon"};
        assertFalse(ObjectUtils.nullSafeEquals(str1, str3));
    }

}