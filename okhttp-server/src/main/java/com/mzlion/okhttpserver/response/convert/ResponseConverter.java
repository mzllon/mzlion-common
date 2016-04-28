package com.mzlion.okhttpserver.response.convert;

import okhttp3.Response;

/**
 * Created by mzlion on 2016/4/18.
 */
public interface ResponseConverter<E> {

    E convert(Response response);

}
