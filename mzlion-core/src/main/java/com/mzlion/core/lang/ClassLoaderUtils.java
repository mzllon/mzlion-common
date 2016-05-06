package com.mzlion.core.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.ClassLoaderUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

/**
 * Created by mzlion on 2016/4/10.
 */
public class ClassLoaderUtils {
    //log
    private static Logger logger = LoggerFactory.getLogger(ClassLoaderUtils.class);

    /**
     * 得到自定义类加载器
     *
     * @param pathEntries 类或jar的路径
     * @return 返回类加载器
     */
    public static ClassLoader getCustomClassLoader(String... pathEntries) {
        if (ArrayUtils.isEmpty(pathEntries)) {
            logger.debug(" 输入参数[entries]为空");
            return null;
        }

        int i = 0, length = pathEntries.length;
        URL[] urls = new URL[length];
        File file;
        String pathEntry;
        for (; i < length; i++) {
            pathEntry = pathEntries[i];
            if (StringUtils.isEmpty(pathEntry)) {
                logger.debug("数组袁术中存在空");
                return null;
            }
            file = new File(pathEntry);
            if (!file.exists()) {
                logger.debug("该文件不存在->{}", pathEntry);
                return null;
            }
            try {
                urls[i] = file.toURI().toURL();
            } catch (MalformedURLException e) {
                logger.error("解析文件失败", e);
                return null;
            }
        }

        ClassLoader parent = getDefaultClassLoader();
        URLClassLoader urlClassLoader = new URLClassLoader(urls, parent);
        logger.debug("得到的类加载器为->{}", Arrays.toString(urls));
        return urlClassLoader;
    }

    /**
     * 返回一个默认的类加载器：首先会从当前线程获取类加载，如果获取失败则获取当前类的类加载器。
     *
     * @return 返回类类加载器
     * @see Thread#getContextClassLoader()
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Throwable e) {
            //无法从当前线程获取到
        }
        if (classLoader == null) {
            classLoader = ClassLoaderUtil.class.getClassLoader();
        }
        return classLoader;
    }
}
