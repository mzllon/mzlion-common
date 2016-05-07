package com.mzlion.okhttpserver.request;

import com.mzlion.core.io.IOUtils;
import com.mzlion.okhttpserver.AbstractHttpRequest;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.*;

/**
 * Created by mzlion on 2016/4/16.
 */
public class BinaryBodyPostRequest extends AbstractHttpRequest<BinaryBodyPostRequest> {

    protected byte[] content;

    public BinaryBodyPostRequest(String url) {
        super(url);
    }

    public BinaryBodyPostRequest stream(InputStream in) {
        if (in == null) {
            throw new NullPointerException("In must not be null.");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            if (IOUtils.copy(in, out) == -1) {
                throw new IOException("Copy failed");
            }
            this.content = out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Reading stream failed->", e);
        } finally {
            IOUtils.closeCloseable(in);
        }
        return this;
    }

    public BinaryBodyPostRequest file(File file) {
        if (file == null) {
            throw new NullPointerException("File must not be null.");
        }
        try {
            return this.stream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            //ignore
        }
        return this;
    }

    @Override
    protected RequestBody generateRequestBody() {
        return null;
    }

    @Override
    protected Request generateRequest(RequestBody requestBody) {
        return null;
    }
}
