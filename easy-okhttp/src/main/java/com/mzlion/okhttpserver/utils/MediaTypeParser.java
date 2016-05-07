package com.mzlion.okhttpserver.utils;

import okhttp3.MediaType;

import java.net.FileNameMap;
import java.net.URLConnection;

/**
 * Created by mzlion on 2016/4/16.
 */
public class MediaTypeParser {

    public static MediaType parse(String filename) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentType = fileNameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return MediaType.parse(contentType);
    }

}
