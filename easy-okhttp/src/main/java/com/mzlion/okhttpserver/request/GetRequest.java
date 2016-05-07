package com.mzlion.okhttpserver.request;

import com.mzlion.core.lang.CollectionUtils;
import com.mzlion.okhttpserver.AbstractHttpRequest;
import com.mzlion.okhttpserver.bean.HttpParameter;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;

/**
 * <p>
 * 2016-04-16 HTTP的GET请求对象
 * </p>
 *
 * @author mzlion
 */
public class GetRequest extends AbstractHttpRequest<GetRequest> {

    /**
     * 构造GET请求对象
     *
     * @param url 请求的URL地址
     */
    public GetRequest(String url) {
        super(url);
        this.builder = new HttpParameter.Builder();
    }

    /**
     * 设置请求参数
     *
     * @param key   请求参数名
     * @param value 请求参数值
     * @return {@link GetRequest}
     */
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
