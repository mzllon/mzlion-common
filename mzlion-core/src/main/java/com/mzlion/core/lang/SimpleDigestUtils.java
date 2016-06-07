package com.mzlion.core.lang;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mzlion on 2016/6/5.
 */
public class SimpleDigestUtils {

    private static final int STREAM_BUFFER_LENGTH = 1024;

    public static byte[] md5(InputStream in) {
        Assert.assertNotNull(in, "InputStream must not be null.");
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            return updateDigest(digest, in).digest();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }


    public static MessageDigest updateDigest(final MessageDigest digest, final InputStream data) {
        final byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
        try {
            int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
            while (read > -1) {
                digest.update(buffer, 0, read);
                read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
            }
            return digest;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
