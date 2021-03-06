package com.mzlion.core.io;


import com.mzlion.core.exceptions.FatalStreamException;
import com.mzlion.core.lang.Assert;
import com.mzlion.core.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 2016-04-11 IO流工具类
 * <p>
 * 本工具类提供的方法都不会刷新或关闭流，所以需要调用者自己手动关闭。
 * </p>
 * <p>
 * 该工具类的部分实现参照了<s>commons-io</s>框架提供的方法。
 * </P>
 *
 * @author mzlion
 */
public abstract class IOUtils {
    private static Logger logger = LoggerFactory.getLogger(IOUtils.class);

    /**
     * 文件结束标记
     */
    private static final int EOF = -1;

    /**
     * The default buffer size ({@value}) to use for copy large file.
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    /**
     * 关闭<code>Closeable</code>,该方法等效于{@linkplain Closeable#close()}
     * <p>
     * 该方法主要用于finally块中，并且忽略所有的异常
     * </p>
     * Example code:
     * <pre>
     *   Closeable closeable = null;
     *   try {
     *       closeable = new FileReader("foo.txt");
     *       // process closeable
     *       closeable.close();
     *   } catch (Exception e) {
     *       // error handling
     *   } finally {
     *       IOUtils.closeQuietly(closeable);
     *   }
     * </pre>
     *
     * @param closeable the object to close, may be null or already closed
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    /**
     * 将输入流转为字节数组
     *
     * @param in 输入流
     * @return 如果转换成功则返回字节数组，否则返回{@code null}
     */
    public static byte[] toByteArray(InputStream in) {
        Assert.notNull(in, "Input stream must not be null.");
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        if (copy(in, out) == -1) {
            return null;
        }
        return out.toByteArray();
    }

    /**
     * 从输入流中读取指定长度的字节数组
     *
     * @param in   输入流
     * @param size 读取长度，不能小于0
     * @return 返回读取的数据
     */
    public static byte[] toByteArray(InputStream in, int size) {
        Assert.notNull(in, "Input stream must not be null.");
        if (size < 0) throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
        if (size == 0) return new byte[0];

        byte[] data = new byte[size];
        int offset = 0;
        int readed;

        try {
            while (offset < size && (readed = in.read(data, offset, size - offset)) != EOF) {
                offset += readed;
            }
            if (offset != size) {
                throw new IOException("Unexpected readed size. current: " + offset + ", excepted: " + size);
            }
        } catch (IOException e) {
            throw new FatalStreamException(e);
        }

        return data;
    }

    /**
     * 将<code>Reader</code>的内容转为字节数组，否转换异常则返回{@code null}
     *
     * @param reader read from
     * @return 转换异常时返回{@code null}，否则返回字节数组
     */
    public static byte[] toByteArray(Reader reader) {
        return toByteArray(reader, Charset.defaultCharset());
    }

    /**
     * 将<code>Reader</code>的内容转为字节数组，否转换异常则返回{@code null}
     *
     * @param reader   read from
     * @param encoding 编码
     * @return 转换异常时返回{@code null}，否则返回字节数组
     */
    public static byte[] toByteArray(Reader reader, String encoding) {
        return toByteArray(reader, StringUtils.isEmpty(encoding) ? Charset.defaultCharset() : Charset.forName(encoding));
    }

    /**
     * 将<code>Reader</code>的内容转为字节数组，否转换异常则返回{@code null}
     *
     * @param reader   read from
     * @param encoding 编码
     * @return 转换异常时返回{@code null}，否则返回字节数组
     */
    public static byte[] toByteArray(Reader reader, Charset encoding) {
        Assert.notNull(reader, "Reader must not be null.");
        if (encoding == null) {
            encoding = Charset.defaultCharset();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        if (!copy(reader, out, encoding)) {
            return null;
        }
        return out.toByteArray();
    }

    public static String toString(InputStream in) {
        return toString(in, Charset.defaultCharset());
    }

    public static String toString(InputStream in, String encoding) {
        return toString(in, StringUtils.isEmpty(encoding) ? Charset.defaultCharset() : Charset.forName(encoding));
    }

    public static String toString(InputStream in, Charset encoding) {
        Assert.notNull(in, "InputStream must not be null.");
        StringWriter writer = new StringWriter();
        if (!copy(in, writer, encoding)) {
            return null;
        }
        return writer.toString();
    }

    public static String toString(Reader reader) {
        Assert.notNull(reader, "Reader must not be null.");
        StringWriter writer = new StringWriter();
        if (copy(reader, writer) == -1) {
            return null;
        }
        return writer.toString();
    }


    // copy from InputStream
    //-----------------------------------------------------------------------

    /**
     * 流的拷贝，超大流(超过2GB)拷贝返回的结果为-1。如果是超大流拷贝请使用{@linkplain #copyLarge(InputStream, OutputStream)}
     *
     * @param in  输入流
     * @param out 输出流
     * @return 返回流大小，如果拷贝失败或流过大均返回-1
     */
    public static int copy(InputStream in, OutputStream out) {
        long count = copyLarge(in, out);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    /**
     * 流的拷贝，如果拷贝流失败则返回-1.
     *
     * @param in  输入流
     * @param out 输出流
     * @return 返回流大小，如果拷贝失败则返回-1
     */
    public static long copyLarge(InputStream in, OutputStream out) {
        return copyLarge(in, out, new byte[DEFAULT_BUFFER_SIZE]);
    }

    /**
     * 流的拷贝，如果拷贝流失败则返回-1.
     *
     * @param in     输入流
     * @param out    输出流
     * @param buffer 缓冲区
     * @return 返回流大小，如果拷贝失败则返回-1
     */
    public static long copyLarge(InputStream in, OutputStream out, byte[] buffer) {
        Assert.notNull(in, "InputStream must not be null.");
        Assert.notNull(out, "OutputStream must not be null.");
        Assert.notEmpty(buffer, "The buffer array must not null or empty.");
        long count = 0;
        int n;
        try {
            while (EOF != (n = in.read(buffer))) {
                out.write(buffer, 0, n);
                count += n;
            }
            return count;
        } catch (IOException e) {
            throw new FatalStreamException("Copy bytes from a large InputStream to an OutputStream error", e);
        }
    }

    /**
     * 将输入流的字节数组转换为<code>Writer</code>字符内容，使用系统默认编码。
     *
     * @param in     字节输入流
     * @param writer 字符输出流
     * @return 拷贝成功则返回{@code true},否则返回{@code false}
     */
    public static boolean copy(InputStream in, Writer writer) {
        return copy(in, writer, Charset.defaultCharset());
    }

    /**
     * 将输入流的字节数组转换为<code>Writer</code>字符内容，使用系统默认编码。
     *
     * @param in       字节输入流
     * @param writer   字符输出流
     * @param encoding 字符编码，如果为空则使用平台默认编码
     * @return 拷贝成功则返回{@code true},否则返回{@code false}
     */
    public static boolean copy(InputStream in, Writer writer, String encoding) {
        return copy(in, writer, StringUtils.isEmpty(encoding) ? Charset.defaultCharset() : Charset.forName(encoding));
    }

    /**
     * 将输入流的字节数组转换为<code>Writer</code>字符内容，使用系统默认编码。
     *
     * @param in       字节输入流
     * @param writer   字符输出流
     * @param encoding 字符编码，如果为空则使用平台默认编码
     * @return 拷贝成功则返回{@code true},否则返回{@code false}
     */
    public static boolean copy(InputStream in, Writer writer, Charset encoding) {
        Assert.notNull(in, "Input stream must not be null.");
        InputStreamReader reader = new InputStreamReader(in, encoding == null ? Charset.defaultCharset() : encoding);
        return copy(reader, writer) > 0;
    }


    // copy from Reader
    //-----------------------------------------------------------------------

    /**
     * 将字符输入流转换为字符输出流，如果字符输入流的大小超过2GB，则返回-1
     *
     * @param reader 字符输入流
     * @param writer 字符输出流
     * @return 拷贝失败或流超过2GB则返回-1，否则返回流的大小
     */
    public static int copy(Reader reader, Writer writer) {
        long count = copyLarge(reader, writer);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    /**
     * 字符流的拷贝，支持大字符流(超过2GB)拷贝
     *
     * @param reader 字符输入流
     * @param writer 字符输出流
     * @return 拷贝成功则返回流的大小，否则返回-1
     */
    public static long copyLarge(Reader reader, Writer writer) {
        return copyLarge(reader, writer, new char[DEFAULT_BUFFER_SIZE]);
    }

    /**
     * 字符流的拷贝，支持大字符流(超过2GB)拷贝
     *
     * @param reader 字符输入流
     * @param writer 字符输出流
     * @param buffer 缓冲区
     * @return 拷贝成功则返回流的大小，否则返回-1
     */
    public static long copyLarge(Reader reader, Writer writer, char[] buffer) {
        long count = 0;
        int n = 0;
        try {
            while (EOF != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
                count += n;
            }
        } catch (IOException e) {
            logger.error(" ===> Copy bytes from a large Reader to an Writer error", e);
            return -1;
        }
        return count;
    }

    /**
     * 将字符输入流转为字节输出流，使用平台默认编码
     *
     * @param reader 字符输入流
     * @param out    字节输出流
     * @return 拷贝成功则返回{@code true},否则返回{@code false}
     */
    public static boolean copy(Reader reader, OutputStream out) {
        return copy(reader, out, Charset.defaultCharset());
    }

    /**
     * 将字符输入流转为字节输出流，使用指定编码
     *
     * @param reader   字符输入流
     * @param out      字节输出流
     * @param encoding 编码
     * @return 拷贝成功则返回{@code true},否则返回{@code false}
     */
    public static boolean copy(Reader reader, OutputStream out, String encoding) {
        return copy(reader, out, StringUtils.isEmpty(encoding) ? Charset.defaultCharset() : Charset.forName(encoding));
    }

    /**
     * 将字符输入流转为字节输出流，使用指定编码
     *
     * @param reader   字符输入流
     * @param out      字节输出流
     * @param encoding 编码
     * @return 拷贝成功则返回{@code true},否则返回{@code false}
     */
    public static boolean copy(Reader reader, OutputStream out, Charset encoding) {
        Assert.notNull(reader, "Reader must not be null.");
        Assert.notNull(out, "Output stream must not be null.");
        encoding = encoding == null ? Charset.defaultCharset() : encoding;
        OutputStreamWriter writer = new OutputStreamWriter(out, encoding);
        if (copy(reader, writer) == -1) {
            return false;
        }
        try {
            // we have to flush here.
            writer.flush();
        } catch (IOException e) {
            logger.error(" ===> Flush outputStream error", e);
            return false;
        }
        return true;
    }

    public static String md5(final InputStream in) {
        Assert.notNull(in, "InputStream must not be null.");

        return null;
    }
}
