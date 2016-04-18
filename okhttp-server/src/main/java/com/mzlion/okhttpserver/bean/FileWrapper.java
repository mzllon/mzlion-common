package com.mzlion.okhttpserver.bean;

import okhttp3.MediaType;

import java.io.File;

/**
 * Created by mzlion on 2016/4/16.
 */
public class FileWrapper {

    private File uploadFile;

    private String filename;

    private long size;

    private MediaType mediaType;

    public FileWrapper(String filename, File uploadFile, long size, MediaType mediaType) {
        this.filename = filename;
        this.uploadFile = uploadFile;
        this.size = size;
        this.mediaType = mediaType;
    }

    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }
}
