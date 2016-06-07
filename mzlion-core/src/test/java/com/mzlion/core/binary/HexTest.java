package com.mzlion.core.binary;

import org.junit.Test;

/**
 * Created by mzlion on 2016/6/7.
 */
public class HexTest {
    @Test
    public void encode2String() throws Exception {
        String str = "{\"";
        String hex = Hex.encode2String(str.getBytes());
        int i = Integer.parseInt(hex, 16);
        System.out.println(hex);
        System.out.println(i);
        System.out.println(Integer.toHexString(i));
    }

    @Test
    public void encode() throws Exception {

    }

    @Test
    public void encode1() throws Exception {

    }

    @Test
    public void decode2String() throws Exception {

    }

    @Test
    public void decode2String1() throws Exception {

    }

    @Test
    public void decode() throws Exception {

    }

    @Test
    public void decode1() throws Exception {

    }

}