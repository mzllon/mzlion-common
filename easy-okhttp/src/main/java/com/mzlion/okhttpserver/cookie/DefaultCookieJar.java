package com.mzlion.okhttpserver.cookie;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.List;

/**
 * 默认的Cookie处理，将Cookie缓存到内存中.
 *
 * @author mzlion
 *         Created by mzlion on 2016/4/17.
 */
public class DefaultCookieJar implements CookieJar {

    private CookieStore cookieStore;

    public DefaultCookieJar(CookieStore cookieStore) {
        if (cookieStore == null) {
            throw new NullPointerException("CookieStore must not be null.");
        }
        this.cookieStore = cookieStore;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.add(url, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.get(url);
    }
}
