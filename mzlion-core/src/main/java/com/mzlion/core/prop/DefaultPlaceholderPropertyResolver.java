package com.mzlion.core.prop;


import com.mzlion.core.io.ResourceUtils;
import com.mzlion.core.lang.ArrayUtils;
import com.mzlion.core.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 2016/4/11:默认处理{@code properties}文件实现类,支持占位符的解析。
 * 该实现类支持classpath:模式的文件路径
 * </p>
 *
 * @author mzlion
 */
public class DefaultPlaceholderPropertyResolver implements PropertyResolver {
    //log
    private Logger logger = LoggerFactory.getLogger(DefaultPlaceholderPropertyResolver.class);

    private static final String PLACEHOLDER_PREFIX = "${";

    private Map<String, String> propertyStringValue;

    private DefaultPlaceholderPropertyResolver(Builder builder) {
        List<String> pathEntries = builder.pathEntries;
        this.processFile(pathEntries);
    }

    /**
     * 处理{@code properties}文件
     *
     * @param pathEntries 所有的{@code properties}文件
     */
    private void processFile(List<String> pathEntries) {
        logger.debug(" ===> 处理属性文件->{}", pathEntries);
        List<File> fileList = new ArrayList<>(pathEntries.size());
        File file;
        for (String pathEntry : pathEntries) {
            file = ResourceUtils.getFile(pathEntry);
            if (file == null) {
                throw new IllegalArgumentException("The pathEntry [" + pathEntry + "] cannot be resolved.");
            }
            fileList.add(file);
        }

        //==============解析properties文件=============
        logger.debug(" ===> 解析properties文件");
        int i, size = fileList.size();
        List<Properties> propertiesList = new ArrayList<>(size);

        Properties properties;
        int count = 0;
        for (i = 0; i < size; i++) {
            file = fileList.get(i);
            try (FileInputStream in = new FileInputStream(file)) {
                properties = new Properties();
                properties.load(in);
                propertiesList.add(properties);
                count += properties.size();
            } catch (IOException e) {
                throw new PropertyResolverException(e);
            }
        }

        //===============取出其中的key=value=================
        logger.debug("取出其中的key=value");
        Map<String, String> stringValueMap = new HashMap<>(count);
        size = propertiesList.size();
        for (i = 0; i < size; i++) {
            properties = propertiesList.get(i);
            for (Object keyObj : properties.keySet()) {
                stringValueMap.put((String) keyObj, properties.getProperty((String) keyObj));
            }
        }


        //===========解析占位符=================
        logger.info("解析占位符->{}", stringValueMap);
        String key, value;
        propertyStringValue = new HashMap<>(stringValueMap.size());
        for (Map.Entry<String, String> entry : stringValueMap.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            propertyStringValue.put(key, this.replacePlaceholders(value, stringValueMap));
        }
        logger.info("处理属性文件成功");
    }

    /**
     * 占位符替换处理
     *
     * @param value          待处理的字符串
     * @param stringValueMap 原始键值对
     * @return 处理后的字符串
     */
    private String replacePlaceholders(String value, Map<String, String> stringValueMap) {
        logger.debug("替换占位符方法被调用->value={}", value);
        if (!value.contains(PLACEHOLDER_PREFIX)) {
            logger.debug("Value[{}]不存在占位符标记", value);
            return value;
        }
        StringBuilder buffer = new StringBuilder();
        final char[] chars = value.toCharArray();
        for (int pos = 0, length = chars.length; pos < length; pos++) {
            if (chars[pos] == '$') {
                // peek ahead
                if (chars[pos + 1] == '{') {
                    StringBuilder placeholder = new StringBuilder(100);
                    int x = pos + 2;
                    for (; x < length && chars[x] != '}'; x++) {
                        placeholder.append(chars[x]);
                        if (x == length - 1) {
                            return value;
                        }
                    }
                    //step1.首先从当前属性文件中查找
                    String extractValue = stringValueMap.get(placeholder.toString());
                    //step2.然后从系统环境变量查找
                    if (StringUtils.isEmpty(extractValue)) {
                        extractValue = extractFromSystem(placeholder.toString());
                    }
                    logger.debug("占位符[{}]对应的值为[{}}", placeholder.toString(), extractValue);
                    buffer.append(extractValue == null ? "" : extractValue);
                    pos = x + 1;
                    // make sure spinning forward did not put us past the end of the buffer...
                    if (pos >= chars.length) {
                        break;
                    }
                }
            }
            buffer.append(chars[pos]);
        }
        return this.replacePlaceholders(buffer.toString(), stringValueMap);
    }

    /**
     * 从系统环境变量查找值
     *
     * @param key 键名
     * @return 值
     */
    private String extractFromSystem(String key) {
        return System.getProperty(key);
    }

    /**
     * 判断{@code key}是否存在
     *
     * @param key 键名，非空
     * @return 如果存在则返回{@code true}，否则返回{@code false}
     */
    @Override
    public boolean containsProperty(String key) {
        return StringUtils.isNotEmpty(key) && propertyStringValue.get(key) != null;
    }

    /**
     * 返回所有的键值对
     */
    @Override
    public Map<String, String> getAllProperties() {
        return new HashMap<>(propertyStringValue);
    }

    /**
     * 返回{@code key}对应的值
     *
     * @param key 键名，非空
     * @return 如果存在则返回值，否则返回{@code null}
     */
    @Override
    public String getProperty(String key) {
        return getProperty(key, null);
    }

    /**
     * 返回{@code key}对应的值，如果值不存在，则返回{@code defaultValue}
     *
     * @param key          键名，非空
     * @param defaultValue 当值为空是则返回该值
     * @return 如果存在则返回值，否则返回{@code defaultValue}
     */
    @Override
    public String getProperty(String key, String defaultValue) {
        if (StringUtils.isEmpty(key)) {
            return defaultValue;
        }
        String value = propertyStringValue.get(key);
        return value == null ? defaultValue : value;
    }

    /**
     * 处理占位符{@code ${...}}字符串，通过调用{@linkplain #getProperty(String)}替换为对应的值。
     * 如果无法替换则忽略
     *
     * @param text 待处理的字符串
     * @return 返回已处理的字符串
     */
    @Override
    public String resolvePlaceholders(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        return this.replacePlaceholders(text, this.propertyStringValue);
    }

    /**
     * 构造对象
     */
    public static class Builder {
        private List<String> pathEntries;

        /**
         * 创建构造对象
         *
         * @see Builder
         */
        public Builder() {
            pathEntries = new ArrayList<>(10);
        }

        /**
         * 添加{@code properties}文件的路径，支持classpath:模式和普通文件模式
         *
         * @param path 文件路径，支持classpath:模式和普通文件模式
         * @return {@link Builder}
         */
        public Builder path(String path) {
            if (StringUtils.isNotEmpty(path)) {
                pathEntries.add(path);
            }
            return this;
        }

        /**
         * 添加{@code properties}文件的路径，支持classpath:模式和普通文件模式
         *
         * @param pathEntries 文件路径，支持classpath:模式和普通文件模式
         * @return {@link Builder}
         */
        public Builder pathList(String... pathEntries) {
            if (ArrayUtils.isNotEmpty(pathEntries)) {
                for (String pathEntry : pathEntries) {
                    path(pathEntry);
                }
            }
            return this;
        }

        /**
         * {@link DefaultPlaceholderPropertyResolver}对象的构建
         *
         * @return {@link DefaultPlaceholderPropertyResolver}
         */
        public DefaultPlaceholderPropertyResolver build() {
            return new DefaultPlaceholderPropertyResolver(this);
        }
    }

}
