package com.mzlion.core.io;

import com.mzlion.core.lang.ClassLoaderUtils;
import com.mzlion.core.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by mzlion on 2016/4/11.
 */
/*public*/ class ClassPathResourceDescriptor extends AbstractResourceDescriptor {


    private final String path;

    private ClassLoader classLoader;

    private Class<?> clazz;

    public ClassPathResourceDescriptor(String path) {
        this(path, (ClassLoader) null);
    }

    public ClassPathResourceDescriptor(String path, ClassLoader classLoader) {
        if (StringUtils.isEmpty(path)) {
            throw new IllegalArgumentException("Path must not be null.");
        }
        this.path = path;
        this.classLoader = (classLoader == null) ? ClassLoaderUtils.getDefaultClassloader() : classLoader;
    }

    public ClassPathResourceDescriptor(String path, Class<?> clazz) {
        if (StringUtils.isEmpty(path)) {
            throw new IllegalArgumentException("Path must not be null.");
        }
        this.path = path;
        this.clazz = clazz;
    }

    public ClassPathResourceDescriptor(String path, ClassLoader classLoader, Class<?> clazz) {
        if (StringUtils.isEmpty(path)) {
            throw new IllegalArgumentException("Path must not be null.");
        }
        this.path = path;
        this.classLoader = classLoader;
        this.clazz = clazz;
    }

    public final String getPath() {
        return path;
    }

    /**
     * 判断资源是否存在
     */
    @Override
    public boolean exists() {
        URL url;
        if (this.clazz != null) {
            url = this.clazz.getResource(this.path);
        } else {
            url = this.classLoader.getResource(this.path);
        }
        return url != null;
    }

    /**
     * 获取输入流，该输入流支持多次读取
     */
    @Override
    public InputStream getInputStream() {
        InputStream in;
        if (this.clazz != null) {
            in = this.clazz.getResourceAsStream(this.path);
        } else {
            in = this.classLoader.getResourceAsStream(this.path);
        }
        return in;
    }

    /**
     * 返回{@code URL}资源
     *
     * @throws IOException
     */
    @Override
    public URL getURL() throws IOException {
        URL url;
        if (this.clazz != null) {
            url = this.clazz.getResource(this.path);
        } else {
            url = this.classLoader.getResource(this.path);
        }
        return url;
    }

    /**
     * 返回文件名，如果资源不存在，则返回{@code null}
     */
    @Override
    public String getFilename() {
        return FilenameUtils.getFilename(this.path);
    }

    /**
     * Return a description for this resource,
     * to be used for error output when working with the resource.
     * <p>Implementations are also encouraged to return this value
     * from their {@code toString} method.
     *
     * @see Object#toString()
     */
    @Override
    public String getDescription() {
        StringBuilder builder = new StringBuilder("class path resource [");
        String pathToUse = path;
        if (this.clazz != null && !pathToUse.startsWith("/")) {
            //builder.append(ClassUtils.classPackageAsResourcePath(this.clazz));
            builder.append('/');
        }
        if (pathToUse.startsWith("/")) {
            pathToUse = pathToUse.substring(1);
        }
        builder.append(pathToUse);
        builder.append(']');
        return builder.toString();
    }
}
