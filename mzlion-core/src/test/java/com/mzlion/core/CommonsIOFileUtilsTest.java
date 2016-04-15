package com.mzlion.core;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

/**
 * Created by mzlion on 2016/4/14.
 */
public class CommonsIOFileUtilsTest {

    @Test
    public void copyDirectory() throws Exception {
        File srcDir = new File("E:\\c1-commons-lang");
        File destDir = new File("E:\\c1-commons-lang\\src\\main");
        FileUtils.copyDirectoryToDirectory(srcDir, destDir);
    }

}
