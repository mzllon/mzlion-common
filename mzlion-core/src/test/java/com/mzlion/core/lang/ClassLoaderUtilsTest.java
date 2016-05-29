package com.mzlion.core.lang;

import org.junit.Test;
import sun.misc.Launcher;

import static org.junit.Assert.assertEquals;

/**
 * 测试类
 * <p>
 * Created by mzlion on 2016/5/29.
 */
public class ClassLoaderUtilsTest {

    @Test
    public void getCustomClassLoader() throws Exception {

    }

    @Test
    public void getDefaultClassLoader() throws Exception {
        assertEquals(Launcher.getLauncher().getClassLoader(), ClassLoaderUtils.getDefaultClassLoader());
    }

}