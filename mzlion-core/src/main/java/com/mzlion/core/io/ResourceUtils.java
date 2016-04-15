package com.mzlion.core.io;

import com.mzlion.core.lang.ClassLoaderUtils;
import com.mzlion.core.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 文件资源工具类
 * <p>
 * Created by mzlion on 2016/4/11.
 *
 * @author mzlion
 */
public class ResourceUtils {
    //log
    private static Logger logger = LoggerFactory.getLogger(ResourceUtils.class);

    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    public static final String FILE_URL_PREFIX = "file:";

    public static final String JAR_URL_PREFIX = "jar:";

    public static File getFile(String resourceLocation) {
        if (StringUtils.isEmpty(resourceLocation)) {
            logger.error("ResourceLocation must not be null.");
            return null;
        }
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            String realPath = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            if (StringUtils.isEmpty(realPath)) {
                logger.error("The classpath has not a real path.");
                return null;
            }
            ClassLoader classLoader = ClassLoaderUtils.getDefaultClassloader();
            URL url = classLoader.getResource(realPath);
            if (url == null) {
                url = ClassLoader.getSystemResource(realPath);
            }
            if (url == null) {
                logger.error("class path resource [" + realPath + "] cannot be resolved to absolute file path because it does not exist/");
                return null;
            }
            return getFile(url);
        }
        try {
            return getFile(new URL(resourceLocation));
        } catch (MalformedURLException e) {
            // no URL -> treat as file path
            return new File(resourceLocation);
        }
    }

    private static File getFile(URL resourceUrl) {
        if (resourceUrl == null) {
            logger.error("URL must not be null.");
            return null;
        }
        try {
            return new File(toURI(resourceUrl).getSchemeSpecificPart());
        } catch (URISyntaxException e) {
            return new File(resourceUrl.getFile());
        }
    }

    public static URI toURI(URL url) throws URISyntaxException {
        return toURI(url.toString());
    }

    /**
     * 创建{@linkplain URI}对象，空格会被替换为"%20"
     *
     * @param location 资源路径
     * @return {@linkplain URI}
     * @throws URISyntaxException 无效的路径描述
     */
    public static URI toURI(String location) throws URISyntaxException {
        return new URI(StringUtils.replace(location, " ", "%20"));
    }
}
