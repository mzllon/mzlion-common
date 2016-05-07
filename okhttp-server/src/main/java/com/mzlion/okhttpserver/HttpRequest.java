package com.mzlion.okhttpserver;

import com.mzlion.okhttpserver.callback.Callback;
import com.mzlion.okhttpserver.response.HttpResponse;
import com.mzlion.okhttpserver.response.convert.ResponseConverter;

import java.io.File;

/**
 * <p>
 * 2016-04-16 构建HTTP的请求对象,接口中的大部分方法均返回接口本身便于链式写法.
 * </p>
 *
 * @author mzlion
 */
public interface HttpRequest<Req extends HttpRequest<Req>> {

    /**
     * 设置请求地址
     *
     * @param url 请求地址
     * @return 返回当前类{@linkplain Req}的对象自己
     */
    Req url(String url);

    /**
     * 添加请求头信息
     *
     * @param key   请求头键名
     * @param value 请求头值
     * @return 返回当前类{@linkplain Req}的对象自己
     */
    Req header(String key, String value);

    /**
     * 从请求头中移除键值
     *
     * @param key 请求头键名
     * @return 返回当前类{@linkplain Req}的对象自己
     */
    Req removeHeader(String key);

    /**
     * 为构建本次{@linkplain HttpRequest}设置单独连接超时时间。调用此方法会重新创建{@linkplain okhttp3.OkHttpClient}。
     *
     * @param connectionTimeout 连接超时时间
     * @return 返回当前类{@linkplain Req}的对象自己
     */
    Req connectionTimeout(int connectionTimeout);

    /**
     * 为构建本次{@linkplain HttpRequest}设置单独读取流超时。
     *
     * @param readTimeout 流读取超时时间
     * @return 返回当前类{@linkplain Req}的对象自己
     */
    Req readTimeout(int readTimeout);

    /**
     * 为构建本次{@linkplain HttpRequest}设置单独SSL证书
     *
     * @param sslFile SSL证书文件
     * @return 返回当前类{@linkplain Req}的对象自己
     */
    Req sslCertificate(File sslFile);

    /**
     * 执行HTTP请求,获取响应结果
     *
     * @param responseConverter 响应结果处理器
     * @param <E>               泛型类
     * @return 将响应结果转为具体的JavaBean
     */
    <E> HttpResponse<E> execute(ResponseConverter<E> responseConverter);

    /**
     * 异步执行HTTP请求，
     *
     * @param callback 回调接口
     */
    void execute(Callback callback);

}
