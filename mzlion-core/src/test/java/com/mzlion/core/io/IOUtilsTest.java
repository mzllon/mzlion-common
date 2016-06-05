package com.mzlion.core.io;

import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Testing for {@link IOUtils}
 * <p>
 * Created by mzlion on 2016/6/4.
 */
public class IOUtilsTest {
    @Test
    public void closeQuietly() throws Exception {
        IOUtils.closeQuietly(null);
    }

    @Test
    public void toByteArray() throws Exception {
        byte[] data = "mzlion".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        byte[] toByteArray = IOUtils.toByteArray(in);
        assertArrayEquals(data, toByteArray);
    }

    @Test
    public void toByteArray1() throws Exception {
        byte[] data = "mzlion".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        byte[] toByteArray = IOUtils.toByteArray(reader);
        assertArrayEquals(data, toByteArray);
    }

    @Test
    public void toByteArray2() throws Exception {
        String str = "工具";
        StringReader reader = new StringReader(str);
        byte[] toByteArray = IOUtils.toByteArray(reader, "gbk");
        assertArrayEquals(str.getBytes("gbk"), toByteArray);
    }

    @Test
    public void toByteArray3() throws Exception {
        String str = "工具";
        StringReader reader = new StringReader(str);
        byte[] toByteArray = IOUtils.toByteArray(reader, StandardCharsets.UTF_8);
        assertArrayEquals(str.getBytes(), toByteArray);
    }

    @Test
    public void toString0() throws Exception {
        String str = "mzlion";
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
        String toString = IOUtils.toString(in);
        assertNotNull(toString);
        assertEquals(str, toString);
    }

    @Test
    public void toString1() throws Exception {
        String str = "工具";
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("gbk"));
        String toString = IOUtils.toString(in, "gbk");
        assertNotNull(toString);
        assertEquals(str, toString);
    }

    @Test
    public void toString2() throws Exception {
        String str = "工具";
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("gbk"));
        String toString = IOUtils.toString(in, Charset.forName("gbk"));
        assertNotNull(toString);
        assertEquals(str, toString);
    }

    @Test
    public void toString3() throws Exception {
        CharArrayReader reader = new CharArrayReader("工具".toCharArray());
        String toString = IOUtils.toString(reader);
        assertNotNull(toString);
        assertEquals("工具", toString);
    }

    @Test
    public void copy() throws Exception {
        String src = "Author is mzlion";
        ByteArrayInputStream in = new ByteArrayInputStream(src.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(in, out);
        String dst = out.toString();
        assertNotNull(dst);
        assertEquals(src, dst);
    }

    @Test
    public void copyLarge() throws Exception {
        //nop
    }

    @Test
    public void copyLarge1() throws Exception {
        //nop
    }

    @Test
    public void copy1() throws Exception {
        String src = "Author is mzlion";
        ByteArrayInputStream in = new ByteArrayInputStream(src.getBytes());
        StringWriter writer = new StringWriter();
        assertTrue(IOUtils.copy(in, writer));
        assertEquals(src, writer.toString());
    }

    @Test
    public void copy2() throws Exception {
        String src = "这是工具类";
        ByteArrayInputStream in = new ByteArrayInputStream(src.getBytes());
        StringWriter writer = new StringWriter();
        assertTrue(IOUtils.copy(in, writer, "utf-8"));
        assertEquals(src, writer.toString());
    }

    @Test
    public void copy3() throws Exception {
        String src = "这是工具类";
        ByteArrayInputStream in = new ByteArrayInputStream(src.getBytes());
        StringWriter writer = new StringWriter();
        assertTrue(IOUtils.copy(in, writer, StandardCharsets.UTF_8));
        assertEquals(src, writer.toString());
    }

    @Test
    public void copy4() throws Exception {
        String src = "这是工具类";
        CharArrayReader reader = new CharArrayReader(src.toCharArray());
        StringWriter writer = new StringWriter();
        int count = IOUtils.copy(reader, writer);
        assertEquals(5, count);
        assertEquals(src, writer.toString());
    }

    @Test
    public void copyLarge2() throws Exception {
        //nop
    }

    @Test
    public void copyLarge3() throws Exception {
        //nop
    }

    @Test
    public void copy5() throws Exception {
        String src = "这是工具类";
        CharArrayReader reader = new CharArrayReader(src.toCharArray());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean copy = IOUtils.copy(reader, out);
        assertTrue(copy);
        assertArrayEquals(src.getBytes(), out.toByteArray());
    }

    @Test
    public void copy6() throws Exception {
        String src = "这是工具类";
        StringReader reader = new StringReader(src);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean copy = IOUtils.copy(reader, out, "gbk");
        assertTrue(copy);
        assertArrayEquals(src.getBytes("gbk"), out.toByteArray());
    }

    @Test
    public void copy7() throws Exception {
        String src = "这是工具类";
        StringReader reader = new StringReader(src);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean copy = IOUtils.copy(reader, out, Charset.forName("gbk"));
        assertTrue(copy);
        assertArrayEquals(src.getBytes("gbk"), out.toByteArray());
    }

}