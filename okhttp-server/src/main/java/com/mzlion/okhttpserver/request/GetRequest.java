package com.mzlion.okhttpserver.request;

import com.mzlion.core.lang.CollectionUtils;
import com.mzlion.okhttpserver.AbstractHttpRequest;
import com.mzlion.okhttpserver.bean.HttpParameter;
import javafx.scene.control.ScrollBar;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;

/**
 * Created by mzlion on 2016/4/16.
 */
public class GetRequest extends AbstractHttpRequest<GetRequest> {

    public GetRequest(String url) {
        super(url);
        this.builder = new HttpParameter.Builder();
    }

    public GetRequest parameter(String key, String value) {
        builder.parameter(key, value);
        return this;
    }

    @Override
    protected RequestBody generateRequestBody() {
        return null;
    }

    @Override
    protected Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get().headers(super.buildHeaders());
        this.buildUrlByParameters();
        return requestBuilder.url(this.url).build();
    }

    private void buildUrlByParameters() {
        StringBuilder sb = new StringBuilder(this.url);
        if (this.url.contains("?")) {
            sb.append("&");
        } else {
            sb.append("?");
        }
        Map<String, String> urlParameters = super.builder.build().getUrlParameters();
        if (CollectionUtils.isNotEmpty(urlParameters)) {
            for (String name : urlParameters.keySet()) {
                sb.append(name).append("=").append(urlParameters.get(name)).append("&");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        this.url = sb.toString();
    }
}
