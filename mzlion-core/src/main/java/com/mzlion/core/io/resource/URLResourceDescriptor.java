package com.mzlion.core.io.resource;

import com.mzlion.core.lang.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by mzlion on 2016/5/6.
 */
public class URLResourceDescriptor extends AbstractResourceDescriptor {

    private final URL url;

    public URLResourceDescriptor(URL url) {
        Assert.notNull(url, "URL must not be null");
        this.url = url;
    }

    public URLResourceDescriptor(String path) throws MalformedURLException {
        Assert.hasLength(path, "Path must not be null");
        this.url = new URL(path);
    }

    /**
     * 获取输入流，该输入流支持多次读取
     */
    @Override
    public InputStream getInputStream() {
        URLConnection con = null;
        try {
            con = this.url.openConnection();
            return con.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            if (con instanceof HttpURLConnection) {
                ((HttpURLConnection) con).disconnect();
            }
        }
        return null;
    }

    /**
     * 返回{@code URL}资源
     *
     * @throws IOException
     */
    @Override
    public URL getURL() throws IOException {
        return this.url;
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
        return "URL [" + this.url + "]";
    }
}
