package com.mzlion.okhttpserver;

import com.mzlion.okhttpserver.callback.Callback;
import com.mzlion.okhttpserver.response.HttpResponse;
import com.mzlion.okhttpserver.response.convert.ResponseConverter;

import java.io.File;
import java.util.Map;

/**
 * Created by mzlion on 2016/4/16.
 */
public interface HttpRequest<Req extends HttpRequest<Req>> {

    Req url(String url);

    Req header(String key, String value);

    Req removeHeader(String key);

    Req connectionTimeout(int connectionTimeout);

    Req readTimeout(int readTimeout);

    Req sslCertificate(String sslPathname);

    Req sslCertificate(File sslFile);

    Map<String, String> getHeaders();

    <E> HttpResponse<E> execute(ResponseConverter<E> responseConverter);

    void execute(Callback callback);

}
