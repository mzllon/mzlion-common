package com.mzlion.okhttpserver.request;

import com.mzlion.okhttpserver.AbstractHttpRequest;
import com.mzlion.okhttpserver.bean.HttpParameter;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by mzlion on 2016/4/16.
 */
public class CommonPostRequest extends AbstractHttpRequest<CommonPostRequest> {

    protected HttpParameter.Builder builder;

    public CommonPostRequest(String url) {
        super(url);
        this.builder = new HttpParameter.Builder();
    }

    public CommonPostRequest parameter(String name, String value) {
        builder.parameter(name, value);
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
