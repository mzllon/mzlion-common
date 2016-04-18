package com.mzlion.okhttpserver.response.convert;

import okhttp3.Response;

/**
 * Created by mzlion on 2016/4/18.
 */
public abstract class AbstractResponseConverter<E> implements ResponseConverter<E> {

    public abstract E doConvert(Response response, Class<E> targetClass);

    @Override
    public E convert(Response response) {
        if (response.isSuccessful()) {
            return this.doConvert(response, getTargetClass());
        }
        return null;
    }
}
