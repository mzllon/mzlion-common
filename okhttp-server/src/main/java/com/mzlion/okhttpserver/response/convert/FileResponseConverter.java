package com.mzlion.okhttpserver.response.convert;

import com.mzlion.core.io.FileUtils;
import okhttp3.Response;

import java.io.File;
import java.io.InputStream;

/**
 * Created by mzlion on 2016/4/18.
 */
public class FileResponseConverter extends AbstractResponseConverter<File> implements ResponseConverter<File> {

    private final File destFile;

    public FileResponseConverter(File destFile) {
        this.destFile = destFile;
    }

    @Override
    public File doConvert(Response response, Class<File> targetClass) {
        InputStream inputStream = response.body().byteStream();
        FileUtils.copyInputStreamToFile(inputStream, destFile);
        return destFile;
    }

    @Override
    public Class<File> getTargetClass() {
        return File.class;
    }
}
