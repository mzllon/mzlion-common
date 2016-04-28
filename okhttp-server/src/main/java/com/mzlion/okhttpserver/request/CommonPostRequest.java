package com.mzlion.okhttpserver.request;

import com.mzlion.core.lang.CollectionUtils;
import com.mzlion.okhttpserver.AbstractHttpRequest;
import com.mzlion.okhttpserver.bean.HttpParameter;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;

/**
 * <p>
 * 2016-04-16 HTTP的POST提交封装对象，所有参数采用<code>application/x-www-form-urlencoded</code>编码后传递。
 * 该POST提交数据形如Web开发的常规表单提交：
 * <pre>
 *     <form action="#" method="post" enctype="application/x-www-form-urlencoded">
 *         <input type="hidden" name="id" value="1">
 *         <input type="text" name="username" value="admin">
 *      </form>
 * </p>
 * </pre>
 *
 * @author mzlion
 */
public class CommonPostRequest extends AbstractHttpRequest<CommonPostRequest> {

    private HttpParameter.Builder builder;

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
        FormBody.Builder builder = new FormBody.Builder();
        Map<String, String> urlParameters = this.builder.build().getUrlParameters();
        if (CollectionUtils.isNotEmpty(urlParameters)) {
            for (Map.Entry<String, String> entry : urlParameters.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
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
