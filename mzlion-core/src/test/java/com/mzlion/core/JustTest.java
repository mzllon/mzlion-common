package com.mzlion.core;

import org.junit.Test;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by mzlion on 2016/5/22.
 */
public class JustTest {

    @Test
    public void test() throws Exception {
//        System.out.println(DateUtils.formatDate(DateUtils.parseDateByLongStr("1464710400000")));
//        System.out.println(DateUtils.formatDate(DateUtils.parseDateByLongStr("1464753600000")));
        System.out.println(DatatypeConverter.printBase64Binary("严".getBytes()));
        System.out.println(DatatypeConverter.parseInteger("xsd:10"));
    }

}
