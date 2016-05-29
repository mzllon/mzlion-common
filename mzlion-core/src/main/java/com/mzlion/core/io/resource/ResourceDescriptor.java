package com.mzlion.core.io.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * 文件或classpath的资源描述接口
 * Created by mzlion on 2016/4/11.
 */
public interface ResourceDescriptor {

    /**
     * 判断资源是否存在
     */
    boolean exists();

    /**
     * 返回{@code URL}资源
     *
     * @throws IOException
     */
    URL getURL() throws IOException;

    /**
     * 返回{@code URI}资源
     *
     * @throws IOException
     */
    URI getURI() throws IOException;

    /**
     * 返回文件对象，如果对象不存在则返回{@code null}
     */
    File getFile();

    /**
     * 返回资源的内容长度，如果资源不存在则返回{@code -1}
     */
    long length();

    /**
     * 返回文件名，如果资源不存在，则返回{@code null}
     */
    String getFilename();

    /**
     * 获取输入流，该输入流支持多次读取
     *
     */
    InputStream getInputStream();


    /**
     * Return a description for this resource,
     * to be used for error output when working with the resource.
     * <p>Implementations are also encouraged to return this value
     * from their {@code toString} method.
     *
     * @see Object#toString()
     */
    String getDescription();

}
