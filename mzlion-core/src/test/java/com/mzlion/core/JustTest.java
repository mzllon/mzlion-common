package com.mzlion.core;

import org.junit.Test;

/**
 * Created by mzlion on 2016/5/22.
 */
public class JustTest {

    @Test
    public void test() throws Exception {
        int cap = 17;
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        System.out.println(n);
    }

}
