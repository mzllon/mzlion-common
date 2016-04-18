package com.mzlion.core.io;


import com.mzlion.core.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 输入输出流工具类
 * <p>
 * Created by mzlion on 2016/4/11.
 */
public class IOUtils {
    private static Logger logger = LoggerFactory.getLogger(IOUtils.class);

    /**
     * 文件结束标记
     */
    private static final int EOF = -1;

    /**
     * The default buffer size ({@value}) to use for copy.
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;


    public static void closeCloseable(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 流的拷贝，使用默认缓冲区大小{@linkplain #DEFAULT_BUFFER_SIZE}，如果拷贝文件失败则返回-1.
     *
     * @param in  输入流
     * @param out 输出流
     * @return 返回文件大小
     * @throws NullPointerException 输入流或属输出流为空
     */
    public static long copy(InputStream in, OutputStream out) {
        return copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 流的拷贝，如果拷贝文件失败则返回-1.
     *
     * @param in         输入流
     * @param out        输出流
     * @param bufferSize 缓冲区大小
     * @return 返回文件大小
     * @throws NullPointerException 输入流或属输出流为空
     */
    public static long copy(InputStream in, OutputStream out, int bufferSize) {
        long count = 0;
        int n;
        byte[] buffer = new byte[bufferSize];
        try {
            while (EOF != (n = in.read(buffer))) {
                out.write(buffer, 0, n);
                count += n;
            }
        } catch (IOException e) {
            logger.error("数据拷贝失败", e);
            return -1;
        }
        return count;
    }

    public static long copy(Reader reader, OutputStream out) {
        return copy(reader, out, Charset.defaultCharset());
    }

    public static long copy(Reader reader, OutputStream out, String encoding) {
        if (StringUtils.isEmpty(encoding)) {
            throw new NullPointerException("Encoding must not be null.");
        }
        return copy(reader, out, Charset.forName(encoding));
    }

    public static long copy(Reader reader, OutputStream out, Charset encoding) {

        return -1;
    }
}
