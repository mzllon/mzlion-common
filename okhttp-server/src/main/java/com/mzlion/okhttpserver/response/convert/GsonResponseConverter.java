package com.mzlion.okhttpserver.response.convert;

import com.mzlion.core.json.gson.JsonUtil;
import com.mzlion.core.lang.StringUtils;
import okhttp3.Response;

import java.nio.charset.Charset;

/**
 * Created by mzlion on 2016/4/18.
 */
public class GsonResponseConverter<E> extends AbstractResponseConverter<E> implements ResponseConverter<E> {

    private final Class<E> targetClass;

    private Charset charset;

    public GsonResponseConverter(Class<E> targetClass) {
        this.targetClass = targetClass;

    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    @Override
    public E doConvert(Response response, Class<E> targetClass) {
        StringResponseConvert stringResponseConvert = new StringResponseConvert(this.charset);
        String responseString = stringResponseConvert.convert(response);
        if (StringUtils.isEmpty(responseString)) {
            return null;
        }
        return JsonUtil.toBean(responseString, this.targetClass);
    }

    @Override
    public Class<E> getTargetClass() {
        return targetClass;
    }
}
