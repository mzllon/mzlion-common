package com.mzlion.okhttpserver.request;

import com.mzlion.okhttpserver.AbstractHttpRequest;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by mzlion on 2016/4/16.
 */
public class StringBodyRequest extends AbstractHttpRequest<StringBodyRequest> {

    private String content;
    private Charset charset;

    public StringBodyRequest(String url) {
        super(url);
        this.charset = StandardCharsets.UTF_8;
    }

    public StringBodyRequest content(String content) {
        this.content = content;
        return this;
    }

    public StringBodyRequest charset(String charset) {
        this.charset = Charset.forName(charset);
        return this;
    }

    @Override
    protected RequestBody generateRequestBody() {
        return null;
    }

    @Override
    protected Request generateRequest(RequestBody requestBody) {
        return null;
    }
}
