package com.mzlion.okhttpserver;

import com.mzlion.core.http.HttpHeaderUtils;
import com.mzlion.core.lang.CollectionUtils;
import com.mzlion.core.lang.StringUtils;
import com.mzlion.core.system.UserAgentUtils;
import com.mzlion.okhttpserver.bean.HttpParameter;
import com.mzlion.okhttpserver.callback.Callback;
import com.mzlion.okhttpserver.response.HttpResponse;
import com.mzlion.okhttpserver.response.convert.DefaultResponseConverter;
import com.mzlion.okhttpserver.response.convert.ResponseConverter;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by mzlion on 2016/4/16.
 */
@SuppressWarnings("all")
public abstract class AbstractHttpRequest<Req extends HttpRequest<Req>> implements HttpRequest<Req> {

    private static String acceptLanguage;

    protected String url;
    protected int connectionTimeout;
    protected int readTimeout;
    protected Map<String, String> headers;
    protected HttpParameter.Builder builder;

    protected List<File> sslFileList;

    public AbstractHttpRequest(String url) {
        this.url = url;
        this.headers = new ConcurrentHashMap<>(10);
        this.sslFileList = new ArrayList<>(10);

        //加载默认Header
        Map<String, String> defaultHeaders = HttpClient.INSTANCE.getDefaultHeaders();
        if (CollectionUtils.isNotEmpty(defaultHeaders)) {
            this.headers.putAll(defaultHeaders);
        }

        //加载默认传递参数
        Map<String, String> defaultParameters = HttpClient.INSTANCE.getDefaultParameters();
        if (CollectionUtils.isNotEmpty(defaultParameters)) {
            this.builder.parameter(defaultParameters);
        }
    }

    @Override
    public Req url(String url) {
        this.url = url;
        return (Req) this;
    }

    @Override
    public Req header(String key, String value) {
        if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
            this.headers.put(key, value);
        }
        return (Req) this;
    }

    @Override
    public Req removeHeader(String key) {
        if (StringUtils.isNotEmpty(key)) {
            this.headers.remove(key);
        }
        return (Req) this;
    }

    @Override
    public Req connectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return (Req) this;
    }

    @Override
    public Req readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return (Req) this;
    }

    @Override
    public Req sslCertificate(String sslPathname) {
        if (StringUtils.isNotEmpty(sslPathname)) {
            sslCertificate(new File(sslPathname));
        }
        return (Req) this;
    }

    @Override
    public Req sslCertificate(File sslFile) {
        if (null != sslFile)
            this.sslFileList.add(sslFile);
        return (Req) this;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public <E> HttpResponse<E> execute(ResponseConverter<E> responseConverter) {
        RequestBody requestBody = this.generateRequestBody();
        final Request request = this.generateRequest(requestBody);
        Call call = generateCall(request);
        try {
            Response response = call.execute();
            final HttpResponse<E> httpResponse = new HttpResponse<>();
            if (responseConverter == null) {
                responseConverter = (ResponseConverter<E>) new DefaultResponseConverter();
            }
            httpResponse.setRawResponse(response);
            httpResponse.setSuccess(response.isSuccessful());
            if (!response.isSuccessful()) {
                httpResponse.setErrorMessage(response.message());
            }
            httpResponse.setEntity(responseConverter.convert(response));
            return httpResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void execute(Callback callback) {
        throw new UnsupportedOperationException();
    }

    protected abstract RequestBody generateRequestBody();

    /**
     * 根据不同的请求方式，将RequestBody转换成Request对象
     *
     * @param requestBody 请求体
     * @return {@link Request}
     * @see RequestBody
     */
    protected abstract Request generateRequest(RequestBody requestBody);

    protected Call generateCall(final Request request) {
        if (this.readTimeout <= 0 && this.connectionTimeout <= 0 && CollectionUtils.isEmpty(this.sslFileList)) {
            return HttpClient.INSTANCE.getOkHttpClient().newCall(request);
        }
        OkHttpClient.Builder builder = HttpClient.INSTANCE.getOkHttpClient().newBuilder();
        if (this.connectionTimeout > 0) {
            builder.connectTimeout(this.connectionTimeout, TimeUnit.MILLISECONDS);
        }
        if (this.readTimeout > 0) {
            builder.readTimeout(this.readTimeout, TimeUnit.MILLISECONDS);
        }
        if (CollectionUtils.isNotEmpty(this.sslFileList)) {
            // TODO: 2016/4/17 SSL证书认证
        }
        return builder.build().newCall(request);
    }


    protected Headers buildHeaders() {
        Headers.Builder builder = new Headers.Builder();
        //设置仿真浏览器信息
        this.simulator(builder);
        //设置默认请求头
        if (CollectionUtils.isNotEmpty(headers)) {
            for (String key : headers.keySet()) {
                builder.set(key, headers.get(key));
            }
        }
        return builder.build();
    }

    private void simulator(Headers.Builder builder) {
        //添加 Accept-Language
        if (StringUtils.isEmpty(acceptLanguage)) {
            acceptLanguage = HttpHeaderUtils.getAcceptLanguage();
        }
        builder.add("Accept-Language", acceptLanguage);
        //添加 UserAgent
        builder.add("User-Agent", UserAgentUtils.generate());
    }
}
