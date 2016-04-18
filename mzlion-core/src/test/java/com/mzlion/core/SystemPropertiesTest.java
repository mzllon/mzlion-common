package com.mzlion.core;

import org.junit.Test;

/**
 * Created by mzlion on 2016/4/17.
 */
public class SystemPropertiesTest {

    @Test
    public void test() throws Exception {
        System.getProperties().list(System.out);
    }
}
