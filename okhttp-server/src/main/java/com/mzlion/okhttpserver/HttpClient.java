package com.mzlion.okhttpserver;

import com.mzlion.core.io.IOUtils;
import com.mzlion.core.lang.ArrayUtils;
import com.mzlion.core.lang.StringUtils;
import com.mzlion.core.prop.DefaultPlaceholderPropertyResolver;
import com.mzlion.core.prop.PropertyResolver;
import com.mzlion.okhttpserver.cookie.DefaultCookieJar;
import com.mzlion.okhttpserver.cookie.MemoryCookieStore;
import com.mzlion.okhttpserver.request.*;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by mzlion on 2016/4/16.
 */
public enum HttpClient {
    INSTANCE {
    };

    //log
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);

    private OkHttpClient.Builder builder;

    private Map<String, String> defaultHeaders;

    private Map<String, String> defaultParameters;

    HttpClient() {
        //default init
        builder = new OkHttpClient.Builder()
                //设置cookie自动管理
                .cookieJar(new DefaultCookieJar(new MemoryCookieStore()))
                //设置默认https验证
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

        //读取默认配置文件
        PropertyResolver propertyResolver = new DefaultPlaceholderPropertyResolver.Builder()
                .path("classpath:defaultOkHttp.properties").build();
        builder
                //连接超时时间
                .connectTimeout(propertyResolver.getProperty("okhttp.connectionTimeout", Long.class), TimeUnit.MILLISECONDS)
                //读取超时时间
                .readTimeout(propertyResolver.getProperty("okhttp.readTimeout", Long.class), TimeUnit.MILLISECONDS);

        defaultHeaders = new ConcurrentHashMap<>(10);
        defaultParameters = new ConcurrentHashMap<>(10);
    }

    public HttpClient setConnectionTimeout(int connectionTimeout) {
        if (connectionTimeout <= 0) {
            LOGGER.error(" ===> Connect timeout must not be less than 0.");
            return this;
        }
        this.builder.connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    public HttpClient setReadTimeout(int readTimeout) {
        if (readTimeout <= 0) {
            LOGGER.error(" ===> Read timeout must not be less than 0.");
            return this;
        }
        this.builder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    public HttpClient setSSLPathname(String sslPathname) {
        if (StringUtils.isEmpty(sslPathname)) {
            LOGGER.error(" ===> SslPathname must not be null.");
            return this;
        }
        return setSSLFile(new File(sslPathname));
    }

    public HttpClient setSSLFile(File sslFile) {
        if (sslFile == null) {
            throw new NullPointerException("The sslFile is null.");
        }
        if (!sslFile.exists()) {
            throw new IllegalArgumentException(String.format("The file [%s] does not exist.", sslFile));
        }
        if (sslFile.isDirectory()) {
            throw new IllegalArgumentException(String.format("The file [%s] exists but is a directory.", sslFile));
        }
        try {
            setSSLInputStream(new FileInputStream(sslFile));
        } catch (FileNotFoundException e) {
            //ignore
        }
        return this;
    }

    /**
     * 加载自定义SSL证书
     *
     * @param inputStreams 证书输入流
     * @return {@link HttpClient}
     */
    public HttpClient setSSLInputStream(InputStream... inputStreams) {
        if (ArrayUtils.isEmpty(inputStreams)) {
            LOGGER.warn(" ===> InputStream Array is null.");
        }
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            InputStream in;
            for (int i = 0, length = inputStreams.length; i < length; i++) {
                in = inputStreams[i];
                if (in == null) {
                    throw new NullPointerException("The array index [" + i + "] is null.");
                }
                keyStore.setCertificateEntry("Mzlion" + i, certificateFactory.generateCertificate(in));
                IOUtils.closeCloseable(in);
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory());
            return this;
        } catch (CertificateException | KeyStoreException | NoSuchAlgorithmException e) {
            //ignore
        } catch (IOException | KeyManagementException e) {
            LOGGER.error(" ===> SSL does load failed.", e);
            throw new RuntimeException(e);
        }
        return this;
    }

    public HttpClient setDefaultHeader(String key, String value) {
        if (StringUtils.hasText(key) && StringUtils.hasLength(value)) {
            this.defaultHeaders.put(key, value);
        }
        return this;
    }

    public HttpClient setDefaultParams(String name, String value) {
        if (StringUtils.hasText(name) && StringUtils.hasLength(value)) {
            this.defaultParameters.put(name, value);
        }
        return this;
    }

    protected Map<String,String> getDefaultHeaders() {
        return defaultHeaders;
    }

    protected Map<String,String> getDefaultParameters() {
        return defaultParameters;
    }


    /**
     * 返回{@linkplain OkHttpClient}对象
     *
     * @return {@link OkHttpClient}
     */
    public OkHttpClient getOkHttpClient() {
        return builder.build();
    }


    //=====================request======================
    public static GetRequest get(String url) {
        return new GetRequest(url);
    }

    public static CommonPostRequest post(String url) {
        return new CommonPostRequest(url);
    }

    public static FormDataPostRequest postFormData(String url) {
        return new FormDataPostRequest(url);
    }

    public static StringBodyRequest postString(String url) {
        return new StringBodyRequest(url);
    }

    public static BinaryBodyPostRequest postBin(String url) {
        return new BinaryBodyPostRequest(url);
    }
}
