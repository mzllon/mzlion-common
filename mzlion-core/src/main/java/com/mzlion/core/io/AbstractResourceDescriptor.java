package com.mzlion.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * {@linkplain ResourceDescriptor}基本实现
 * <p>
 * Created by mzlion on 2016/4/11.
 *
 * @author mzlion
 */
public abstract class AbstractResourceDescriptor implements ResourceDescriptor {

    /**
     * 判断资源是否存在
     */
    @Override
    public boolean exists() {
        File file = this.getFile();
        if (file == null) {
            try {
                InputStream in = this.getInputStream();
                in.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return getFile().exists();
    }

    /**
     * 返回{@code URL}资源
     *
     * @throws IOException
     */
    @Override
    public URL getURL() throws IOException {
        throw new FileNotFoundException(this.getDescription() + " cannot be resolved to URL");
    }

    /**
     * 返回{@code URI}资源
     *
     * @throws IOException
     */
    @Override
    public URI getURI() throws IOException {
        URL url = this.getURL();
        try {
            return ResourceUtils.toURI(url);
        } catch (URISyntaxException e) {
            throw new IOException("Invalid URI [" + url + "]", e);
        }
    }

    /**
     * 返回文件对象，如果对象不存在则返回{@code null}
     */
    @Override
    public File getFile() {
        return null;
    }

    /**
     * 返回资源的内容长度，如果资源不存在则返回{@code -1}
     */
    @Override
    public long length() {
        InputStream in = this.getInputStream();
        if (in == null) {
            return -1;
        }
        long size = 0;
        byte[] buffer = new byte[1024];
        int read;
        try {
            while ((read = in.read(buffer)) != -1) {
                size += read;
            }
            return size;
        } catch (IOException e) {
            return -1;
        } finally {
            IOUtils.closeCloseable(in);
        }
    }

    /**
     * 返回文件名，如果资源不存在，则返回{@code null}
     */
    @Override
    public String getFilename() {
        return null;
    }

    @Override
    public String toString() {
        return this.getDescription();
    }
}
