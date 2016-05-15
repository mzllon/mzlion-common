package com.mzlion.okhttpserver.bean;

import com.mzlion.core.lang.CollectionUtils;
import com.mzlion.core.lang.StringUtils;
import com.mzlion.okhttpserver.utils.MediaTypeParser;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mzlion on 2016/4/16.
 */
public class HttpParameter implements Serializable {

    private Map<String, String> urlParameters;

    private Map<String, FileWrapper> fileParameters;

    private HttpParameter(Builder builder) {
        this.urlParameters = builder.urlParameters;
        this.fileParameters = builder.fileParameters;
    }

    public Map<String, String> getUrlParameters() {
        return urlParameters;
    }

    public Map<String, FileWrapper> getFileParameters() {
        return fileParameters;
    }

    public static class Builder {

        private Map<String, String> urlParameters;

        private Map<String, FileWrapper> fileParameters;

        public Builder() {
            this.urlParameters = new ConcurrentHashMap<>(10);
            this.fileParameters = new ConcurrentHashMap<>(10);
        }

        public Builder parameter(String name, String value) {
            if (StringUtils.hasText(name) && StringUtils.hasLength(value))
                this.urlParameters.put(name, value);
            return this;
        }

        public Builder parameter(String name, File file) {
            return parameter(name, file, file.getName());
        }

        public Builder parameter(String name, File file, String filename) {
            if (StringUtils.hasText(name) && file != null) {
                if (StringUtils.isEmpty(filename)) {
                    filename = file.getName();
                }
                FileWrapper fileWrapper = new FileWrapper(filename, file, file.length(), MediaTypeParser.parse(filename));
                this.fileParameters.put(name, fileWrapper);
            }
            return this;
        }

        public Builder parameter(HttpParameter httpParameter) {
            if (httpParameter != null) {
                if (CollectionUtils.isNotEmpty(httpParameter.urlParameters))
                    this.urlParameters.putAll(httpParameter.urlParameters);
                if (CollectionUtils.isNotEmpty(httpParameter.fileParameters))
                    this.fileParameters.putAll(httpParameter.fileParameters);
            }
            return this;
        }

        public Builder parameter(Map<String, String> parameters) {
            if (CollectionUtils.isEmpty(parameters)) {
                throw new IllegalArgumentException("Parameters must not be null or empty.");
            }
            this.urlParameters.putAll(parameters);
            return this;
        }

        public HttpParameter build() {
            return new HttpParameter(this);
        }
    }

}
