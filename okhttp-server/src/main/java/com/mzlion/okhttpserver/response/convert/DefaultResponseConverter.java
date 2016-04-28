package com.mzlion.okhttpserver.response.convert;

import okhttp3.Response;

/**
 * Created by mzlion on 2016/4/18.
 */
public class DefaultResponseConverter implements ResponseConverter<Response> {

    @Override
    public Response convert(Response response) {
        return response;
    }

}
