package com.mzlion.core.http;

import com.mzlion.core.io.FileUtils;
import com.mzlion.core.io.FilenameUtils;
import com.mzlion.core.io.IOUtils;
import com.mzlion.core.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * <p>
 * 2016-04-15 {@link HttpServletResponse}相关的工具类。当处理出现异常或失败时，会有日志打印。
 * <p/>
 *
 * @author mzlion
 */
public class HttpResponseUtils {
    //log
    private static Logger logger = LoggerFactory.getLogger(HttpResponseUtils.class);

    /**
     * 下载文件，针对Image等支持的格式会直接在浏览器显示，不会提示下载。
     * <p>
     * 下载成功后原文件不会被删除。
     * </p>
     *
     * @param request      请求对象
     * @param response     响应对象
     * @param downloadFile 下载文件
     */
    public static void downloadInline(HttpServletRequest request, HttpServletResponse response, File downloadFile) {
        downloadInline(request, response, downloadFile, false);
    }

    /**
     * 下载文件，针对Image等支持的格式会直接在浏览器显示，不会提示下载。
     *
     * @param request      请求对象
     * @param response     响应对象
     * @param downloadFile 下载文件
     * @param isDeleted    下载后是否需要删除
     */
    public static void downloadInline(HttpServletRequest request, HttpServletResponse response, File downloadFile, boolean isDeleted) {
        downloadInline(request, response, downloadFile.getName(), downloadFile, isDeleted);
    }

    /**
     * 下载文件，针对Image等支持的格式会直接在浏览器显示，不会提示下载。
     *
     * @param request      请求对象
     * @param response     响应对象
     * @param filename     对外的显示的文件名
     * @param downloadFile 下载文件
     * @param isDeleted    下载后是否需要删除
     */
    public static void downloadInline(HttpServletRequest request, HttpServletResponse response, String filename, File downloadFile, boolean isDeleted) {
        doDownload(request, response, filename, downloadFile, isDeleted, true);
    }

    /**
     * 下载文件，原文件不会被删除
     *
     * @param request      请求对象
     * @param response     响应对象
     * @param downloadFile 下载文件对象
     */
    public static void downloadAttachment(HttpServletRequest request, HttpServletResponse response, File downloadFile) {
        downloadAttachment(request, response, downloadFile, false);
    }

    /**
     * 下载文件
     *
     * @param request      请求对象
     * @param response     HTTP响应对象
     * @param downloadFile 要下载的文件
     * @param isDeleted    是否需要删除当前文件
     */
    public static void downloadAttachment(HttpServletRequest request, HttpServletResponse response, File downloadFile, boolean isDeleted) {
        downloadAttachment(request, response, downloadFile.getName(), downloadFile, isDeleted);
    }

    /**
     * 下载文件
     *
     * @param request      请求对象
     * @param response     HTTP响应对象
     * @param filename     对外显示的下载文件名
     * @param downloadFile 要下载的文件
     * @param isDeleted    是否需要删除当前文件
     */
    public static void downloadAttachment(HttpServletRequest request, HttpServletResponse response, String filename, File downloadFile, boolean isDeleted) {
        doDownload(request, response, filename, downloadFile, isDeleted, false);
    }

    //内部方法实现文件下载
    private static void doDownload(HttpServletRequest request, HttpServletResponse response, String filename, File downloadFile,
                                   boolean isDeleted, boolean tryDisplay) {
        logger.debug(" ===> 对外输出文件，请求参数->{}", filename);
        if (request == null) {
            logger.error(" ===> Request must not be null.");
            return;
        }
        if (response == null) {
            logger.error(" ===> Response must not be null.");
            return;
        }
        if (StringUtils.isEmpty(filename)) {
            logger.error(" ===> The response filename must not be null.");
            return;
        }
        if (downloadFile == null) {
            logger.error(" ===> Download file must not be null.");
            return;
        }
        if (!downloadFile.exists()) {
            logger.error(" ===> Download file does not exist.");
            return;
        }
        if (downloadFile.isDirectory()) {
            logger.error(" ===> Download file exists but is a directory.");
            return;
        }
        if (!downloadFile.canRead()) {
            logger.error(" ===> Download file cannot read.");
            return;
        }

        //设置响应输出类型
        setContentType(response, downloadFile);
        //设置输出长度
        response.setHeader("Content-Length", String.valueOf(downloadFile.length()));

        OutputStream out = null;
        try {
            //设置响应头
            if (tryDisplay)
                response.setHeader("Content-disposition", "inline; filename=" + getDownloadFilename(request, filename));
            else
                response.setHeader("Content-disposition", "attachment; filename=" + getDownloadFilename(request, filename));
            out = response.getOutputStream();
            if (FileUtils.copyFile(downloadFile, out) == -1) {
                logger.error(" ===> 下载文件失败->{}", downloadFile);
            }
            if (isDeleted) {
                logger.debug(" ===> 即将删除下载文件->{}", downloadFile);
                downloadFile.delete();
            }
        } catch (IOException e) {
            logger.error(" ===> 下载文件出现异常", e);
        } finally {
            IOUtils.closeCloseable(out);
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
    public static String getDownloadFilename(HttpServletRequest request, String downloadFilename) {
        String browser = getBrowser(request);
        if (BROWSER_IE.equals(browser) || BROWSER_EDGE.equals(browser)) {
            //IE浏览器，采用URLEncoder编码
            String filename = toUtf8String(downloadFilename);
            // see http://support.microsoft.com/default.aspx?kbid=816868
            if (filename.length() > 150) {
                // 根据request的locale 得出可能的编码
                filename = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            }
            return filename;
        }
        if (BROWSER_SAFARI.equals(browser)) {
            //Safari浏览器，采用ISO编码的中文输出
            return new String(downloadFilename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }
        //Chrome浏览器，可以采用MimeUtility编码或ISO编码的中文输出
        //FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
        //MimeUtility.encodeText(downloadFilename,)
        return new String(downloadFilename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
    }

    /**
     * 设置{@linkplain HttpServletResponse}输出MimeType
     *
     * @param response     响应对象
     * @param downloadFile 下载文件
     */
    private static void setContentType(HttpServletResponse response, File downloadFile) {
        String extension = FilenameUtils.getFilenameExtension(downloadFile);
        if (StringUtils.isEmpty(extension)) {
            response.setContentType(ContentType.DEFAULT_BINARY.toString());
            return;
        }
        extension = extension.toLowerCase();
        switch (extension) {
            case "png":
                response.setContentType(ContentType.IMAGE_PNG.toString());
                break;
            case "jpg":
                response.setContentType(ContentType.IMAGE_JPG.toString());
                break;
            case "jpeg":
                response.setContentType(ContentType.IMAGE_JPEG.toString());
                break;
            case "gif":
                response.setContentType(ContentType.IMAGE_GIF.toString());
                break;
            case "bmp":
                response.setContentType(ContentType.IMAGE_BMP.toString());
                break;
            case "txt":
            case "ini":
                response.setContentType(ContentType.DEFAULT_TEXT.toString());
                break;
            case "xls":
            case "xlsx":
                response.setContentType(ContentType.APPLICATION_XLS.toString());
                break;
            case "doc":
            case "docx":
                response.setContentType(ContentType.APPLICATION_DOC.toString());
                break;
            case "ppt":
            case "pptx":
                response.setContentType(ContentType.APPLICATION_PPT.toString());
                break;
            case "zip":
                response.setContentType(ContentType.APPLICATION_ZIP.toString());
                break;
            case "gz":
                response.setContentType(ContentType.APPLICATION_GZ.toString());
                break;
            case "pdf":
                response.setContentType(ContentType.APPLICATION_PDF.toString());
                break;
            default:
                response.setContentType(ContentType.DEFAULT_BINARY.toString());
        }
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

    private static final String BROWSER_IE = "MSIE";
    private static final String BROWSER_EDGE = "Edge";
    private static final String BROWSER_CHROME = "Chrome";
    private static final String BROWSER_SAFARI = "Safari";
    private static final String BROWSER_FIREFOX = "Firefox";
}
