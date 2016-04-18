package com.mzlion.okhttpserver.response.convert;

import okhttp3.Response;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by mzlion on 2016/4/18.
 */
public class StringResponseConvert extends AbstractResponseConverter<String> implements ResponseConverter<String> {

    private Charset charset;

    public StringResponseConvert() {
        this(StandardCharsets.UTF_8);
    }

    public StringResponseConvert(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("Charset must not be null.");
        }
        this.charset = charset;
    }

    public Charset getCharset() {
        return charset;
    }

    @Override
    public String doConvert(Response response, Class<String> targetClass) {
        try {
            return new String(response.body().bytes(), charset);
        } catch (IOException e) {
            throw new ResponseConverterException("Convert failed->", e);
        }
    }

    @Override
    public Class<String> getTargetClass() {
        return String.class;
    }
}
