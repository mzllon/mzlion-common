package com.mzlion.core.http;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Testing for {@linkplain HttpResponseUtils}
 * <p>
 * Created by mzlion on 2016/5/28.
 */
public class HttpResponseUtilsTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private String content = "mockito junit";
    private File tempFile;

    @Before
    public void prepareTempFile() throws IOException {
        //prepare file
        tempFile = File.createTempFile("mzlion", ".txt");
        PrintWriter printWriter = new PrintWriter(tempFile);
        printWriter.write(content);
        printWriter.flush();
        printWriter.close();

        //mock
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void downloadInline() throws Exception {
        SampleServletOutputStream sampleServletOutputStream = new SampleServletOutputStream();

        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
        when(response.getOutputStream()).thenReturn(sampleServletOutputStream);

        HttpResponseUtils.downloadInline(request, response, tempFile);

        assertEquals(content, sampleServletOutputStream.getDataAsString());
    }

    @Test
    public void downloadInline1() throws Exception {
        SampleServletOutputStream sampleServletOutputStream = new SampleServletOutputStream();

        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
        when(response.getOutputStream()).thenReturn(sampleServletOutputStream);

        HttpResponseUtils.downloadInline(request, response, tempFile, true);
        assertFalse(tempFile.exists());
        assertEquals(content, sampleServletOutputStream.getDataAsString());
    }

    @Test
    public void downloadInline2() throws Exception {
        //ignore
    }

    @Test
    public void downloadAttachment() throws Exception {
        SampleServletOutputStream sampleServletOutputStream = new SampleServletOutputStream();

        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
        when(response.getOutputStream()).thenReturn(sampleServletOutputStream);

        HttpResponseUtils.downloadAttachment(request, response, tempFile);

        assertEquals(content, sampleServletOutputStream.getDataAsString());
    }

    @Test
    public void downloadAttachment1() throws Exception {
        SampleServletOutputStream sampleServletOutputStream = new SampleServletOutputStream();

        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
        when(response.getOutputStream()).thenReturn(sampleServletOutputStream);

        HttpResponseUtils.downloadAttachment(request, response, tempFile, true);

        assertFalse(tempFile.exists());
        assertEquals(content, sampleServletOutputStream.getDataAsString());
    }

    @Test
    public void downloadAttachment2() throws Exception {
        SampleServletOutputStream sampleServletOutputStream = new SampleServletOutputStream();

        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
        when(response.getOutputStream()).thenReturn(sampleServletOutputStream);

        HttpResponseUtils.downloadAttachment(request, response, "tempFile.txt", tempFile, true);

        assertFalse(tempFile.exists());
        assertEquals(content, sampleServletOutputStream.getDataAsString());
    }

    @Test
    public void downloadAttachment3() throws Exception {
        SampleServletOutputStream sampleServletOutputStream = new SampleServletOutputStream();

        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
        when(response.getOutputStream()).thenReturn(sampleServletOutputStream);

        try (FileInputStream in = new FileInputStream(tempFile)) {
            HttpResponseUtils.downloadAttachment(request, response, "tempFile.txt", in, content.length());
            assertEquals(content, sampleServletOutputStream.getDataAsString());
        }
    }

    @Test
    public void getDownloadFilename() throws Exception {
        String filename = "文件下载.txt";
        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
        String downloadFilename = HttpResponseUtils.getDownloadFilename(request, filename);
        assertArrayEquals(new String(filename.getBytes(), StandardCharsets.ISO_8859_1).getBytes(),
                downloadFilename.getBytes());
    }

    private static class SampleServletOutputStream extends ServletOutputStream {

        private List<Byte> data = new ArrayList<>();

        @Override
        public void write(int b) throws IOException {
            data.add((byte) b);
        }


        private byte[] getData() throws IOException {
            byte[] buffer = new byte[data.size()];
            int i = 0;
            for (Byte aByte : data) {
                buffer[i++] = aByte;
            }
            return buffer;
        }

        private String getDataAsString() throws IOException {
            byte[] data = getData();
            return new String(data);
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }


}