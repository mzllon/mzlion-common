package com.mzlion.core;

import com.mzlion.core.date.DateUtils;
import org.junit.Test;

/**
 * Created by mzlion on 2016/5/22.
 */
public class JustTest {

    @Test
    public void test() throws Exception {
        System.out.println(DateUtils.formatDate(DateUtils.parseDateByLongStr("1464710400000")));
        System.out.println(DateUtils.formatDate(DateUtils.parseDateByLongStr("1464753600000")));
    }

}
