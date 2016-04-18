package com.mzlion.okhttpserver.request;

import com.mzlion.okhttpserver.AbstractHttpRequest;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by mzlion on 2016/4/16.
 */
public class StringBodyRequest extends AbstractHttpRequest<StringBodyRequest> {

    protected String content;

    public StringBodyRequest(String url) {
        super(url);
    }

    public StringBodyRequest content(String content) {
        this.content = content;
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
