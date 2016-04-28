package com.mzlion.okhttpserver.response.convert;

import com.mzlion.core.json.gson.JsonUtil;
import com.mzlion.core.lang.StringUtils;
import okhttp3.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by mzlion on 2016/4/18.
 */
public class GsonResponseConverter<E> implements ResponseConverter<E> {

    private final TypeReference<E> typeReference;

    private Charset charset;

    public GsonResponseConverter(TypeReference<E> typeReference) {
        this.charset = StandardCharsets.UTF_8;
        this.typeReference = typeReference;
        if (this.typeReference == null) {
            throw new NullPointerException("TypeReference must not be null.");
        }
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    @Override
    public E convert(Response response) {

        StringResponseConvert stringResponseConvert = new StringResponseConvert(this.charset);
        String responseString = stringResponseConvert.convert(response);
        if (StringUtils.isEmpty(responseString)) {
            return null;
        }
        return JsonUtil.toBean(responseString, this.typeReference.getGenericsType());
    }
}
