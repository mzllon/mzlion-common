package com.mzlion.core.http;

import com.mzlion.core.lang.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * <p>
 * 针对MIME的一些封装
 * </p>
 *
 * @author mzlion
 */
public final class ContentType {

    // constants、
    public static final ContentType ALL = create("*/*", (Charset) null);
    public static final ContentType TEXT_XML = create("text/xml", StandardCharsets.UTF_8);
    public static final ContentType TEXT_PLAIN = create("text/plain", StandardCharsets.UTF_8);
    public static final ContentType TEXT_HTML = create("text/html", StandardCharsets.UTF_8);
    public static final ContentType MULTIPART_FORM_DATA = create("multipart/form-data", StandardCharsets.UTF_8);
    public static final ContentType APPLICATION_XML = create("application/xml", StandardCharsets.UTF_8);
    public static final ContentType APPLICATION_OCTET_STREAM = create("application/octet-stream", (Charset) null);
    public static final ContentType APPLICATION_FORM_URLENCODED = create("application/x-www-form-urlencoded", StandardCharsets.UTF_8);
    public static final ContentType APPLICATION_JSON = create("application/json", StandardCharsets.UTF_8);

    //imgage
    public static final ContentType IMAGE_PNG = create("image/png", (Charset) null);
    public static final ContentType IMAGE_JPEG = create("image/jpeg", (Charset) null);
    public static final ContentType IMAGE_JPG = create("image/jpeg", (Charset) null);
    public static final ContentType IMAGE_GIF = create("image/gif", (Charset) null);
    public static final ContentType IMAGE_BMP = create("image/bmp", (Charset) null);

    //zip
    public static final ContentType APPLICATION_ZIP = create("application/zip", (Charset) null);
    public static final ContentType APPLICATION_GZ = create("application/x-gzip", (Charset) null);

    //pdf
    public static final ContentType APPLICATION_PDF = create("application/pdf", (Charset) null);

    //ms
    public static final ContentType APPLICATION_DOC = create("application/msword", (Charset) null);
    public static final ContentType APPLICATION_XLS = create("application/vnd.ms-excel", (Charset) null);
    public static final ContentType APPLICATION_PPT = create("application/vnd.ms-powerpoint", (Charset) null);

    // defaults
    public static final ContentType DEFAULT_TEXT = TEXT_PLAIN;
    public static final ContentType DEFAULT_BINARY = APPLICATION_OCTET_STREAM;

    private String mimeType;

    private Charset charset;


    ContentType(String mimeType, Charset charset) {
        this.mimeType = mimeType;
        this.charset = charset;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Charset getCharset() {
        return charset;
    }

    public static ContentType create(final String mimeType, final Charset charset) {
        if (StringUtils.isEmpty(mimeType)) {
            throw new IllegalArgumentException("MIME type must not be null.");
        }
        final String type = mimeType.toLowerCase(Locale.CHINESE);
        return new ContentType(type, charset);
    }

    public static ContentType create(final String mimeType, final String charset) {
        return create(mimeType, StringUtils.isEmpty(charset) ? null : Charset.forName(charset));
    }

    public static ContentType create(final String mimeType) {
        return create(mimeType, (Charset) null);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(64);
        sb.append(mimeType);
        if (this.charset != null) {
            sb.append("; charset=").append(this.charset.name());
        }
        return sb.toString();
    }
}
