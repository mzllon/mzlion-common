package com.mzlion.core.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.regex.Pattern;

/**
 * {@link HttpServletResponse}相关的工具类
 * <p/>
 * Created by mzlion on 2015/10/14.
 */
public class HttpServletResponseUtils {
    //log
    private static Logger logger = LoggerFactory.getLogger(HttpServletResponseUtils.class);

    /**
     * 下载文件
     *
     * @param response     HTTP响应对象
     * @param filename     对外显示的下载文件名
     * @param downloadFile 要下载的文件
     * @param isDeleted    是否需要删除当前文件
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, String filename,
                                File downloadFile, boolean isDeleted) {
        logger.debug(" ===> 对外输出文件，请求参数->{}", filename);
        //设置文件输出类型
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        String extension = FilenameUtils.getFilenameExtension(downloadFile);

        response.setContentType("application/octet-stream");

        try {
            response.setHeader("Content-disposition", "attachment; filename=" + getDownloadFilename(request, filename));
            //设置输出长度
            response.setHeader("Content-Length", String.valueOf(downloadFile.length()));

            //获取输入流
            bis = new BufferedInputStream(new FileInputStream(downloadFile));
            bos = new BufferedOutputStream(response.getOutputStream());
            IOUtils.copy(bis, bos);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeCloseable(bis);
            IOUtils.closeCloseable(bos);
            if (isDeleted) downloadFile.delete();
        }
    }

    /**
     * 转义文件下载名
     *
     * @param request          HTTP请求对象
     * @param downloadFilename 原下载文件名
     * @return 返回转义后的下载名
     * @throws UnsupportedEncodingException 忽略异常
     */
    private static String getDownloadFilename(HttpServletRequest request, String downloadFilename) throws UnsupportedEncodingException {
        String browser = getBrowser(request);
        if (BROWSER_IE.equals(browser) || BROWSER_EDGE.equals(browser)) {
            //IE浏览器，采用URLEncoder编码
            String filename = toUtf8String(downloadFilename);
            // see http://support.microsoft.com/default.aspx?kbid=816868
            if (filename.length() > 150) {
                // 根据request的locale 得出可能的编码
                filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
            }
            return filename;
        }
        if (BROWSER_SAFARI.equals(browser)) {
            //Safari浏览器，采用ISO编码的中文输出
            return new String(downloadFilename.getBytes("utf-8"), "ISO8859-1");
        }
        //Chrome浏览器，可以采用MimeUtility编码或ISO编码的中文输出
        //FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
        //MimeUtility.encodeText(downloadFilename,)
        return new String(downloadFilename.getBytes("utf-8"), "ISO8859-1");
    }

    /**
     * 转成UTF8格式的字符串
     */
    private static String toUtf8String(String str) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    logger.error("将文件名中的汉字转为UTF8编码的串时错误，输入的字符串为：" + str);
                    b = new byte[0];
                }
                for (byte aB : b) {
                    int k = aB;
                    if (k < 0)
                        k += 256;
                    sb.append("%").append(Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取浏览器类型
     *
     * @param request HTTP请求对象
     * @return 浏览器类型
     * @see #BROWSER_IE
     * @see #BROWSER_EDGE
     * @see #BROWSER_CHROME
     * @see #BROWSER_SAFARI
     * @see #BROWSER_FIREFOX
     */
    public static String getBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        logger.debug(" ===> User-Agent={}", userAgent);
        /*首先判断是否是IE浏览器*/
        Pattern pattern = Pattern.compile("MISE", Pattern.CASE_INSENSITIVE);
        if (pattern.matcher(userAgent).find()) return BROWSER_IE;

        pattern = Pattern.compile("Trident", Pattern.CASE_INSENSITIVE);
        if (pattern.matcher(userAgent).find()) return BROWSER_IE;

        pattern = Pattern.compile("Edge", Pattern.CASE_INSENSITIVE);
        if (pattern.matcher(userAgent).find()) return BROWSER_EDGE;

        pattern = Pattern.compile("Chrome", Pattern.CASE_INSENSITIVE);
        if (pattern.matcher(userAgent).find()) return BROWSER_CHROME;

        pattern = Pattern.compile("Safari", Pattern.CASE_INSENSITIVE);
        if (pattern.matcher(userAgent).find()) return BROWSER_SAFARI;

        pattern = Pattern.compile("Firefox", Pattern.CASE_INSENSITIVE);
        if (pattern.matcher(userAgent).find()) return BROWSER_FIREFOX;

        logger.debug(" ===> 无法识别浏览器类型，置为默认类型");
        return BROWSER_CHROME;
    }

    public static final String BROWSER_IE = "MSIE";
    public static final String BROWSER_EDGE = "Edge";
    public static final String BROWSER_CHROME = "Chrome";
    public static final String BROWSER_SAFARI = "Safari";
    public static final String BROWSER_FIREFOX = "Firefox";
}
