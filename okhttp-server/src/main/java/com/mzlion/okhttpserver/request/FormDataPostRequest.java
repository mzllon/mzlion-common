package com.mzlion.okhttpserver.request;

import com.mzlion.core.lang.CollectionUtils;
import com.mzlion.okhttpserver.AbstractHttpRequest;
import com.mzlion.okhttpserver.bean.FileWrapper;
import com.mzlion.okhttpserver.bean.HttpParameter;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.File;
import java.util.Map;

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
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        HttpParameter httpParameter = this.builder.build();
        Map<String, String> urlParameters = httpParameter.getUrlParameters();
        if (CollectionUtils.isNotEmpty(urlParameters)) {
            for (Map.Entry<String, String> entry : urlParameters.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        Map<String, FileWrapper> fileParameters = httpParameter.getFileParameters();
        if (CollectionUtils.isNotEmpty(fileParameters)) {
            for (Map.Entry<String, FileWrapper> entry : fileParameters.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue().getFilename(),
                        RequestBody.create(entry.getValue().getMediaType(), entry.getValue().getUploadFile()));
            }
        }
        return builder.build();
    }

    @Override
    protected Request generateRequest(RequestBody requestBody) {
        Request.Builder builder = new Request.Builder();
        builder.headers(super.buildHeaders());
        return builder.url(this.url).post(requestBody).build();
    }
}
