package com.mzlion.okhttpserver.request;

import com.mzlion.okhttpserver.AbstractHttpRequest;
import com.mzlion.okhttpserver.bean.HttpParameter;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.File;

/**
 * Created by mzlion on 2016/4/16.
 */
public class FormDataPostRequest extends AbstractHttpRequest<FormDataPostRequest> {

    protected HttpParameter.Builder builder;

    public FormDataPostRequest(String url) {
        super(url);
        this.builder = new HttpParameter.Builder();
    }

    public FormDataPostRequest parameter(String name, String value) {
        builder.parameter(name, value);
        return this;
    }

    public FormDataPostRequest parameter(String name, File uploadFile) {
        return parameter(name, uploadFile, uploadFile.getName());
    }

    public FormDataPostRequest parameter(String name, File uploadFile, String filename) {
        this.builder.parameter(name, uploadFile, filename);
        return this;
    }

    public FormDataPostRequest parameter(HttpParameter httpParameter) {
        this.builder.parameter(httpParameter);
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
