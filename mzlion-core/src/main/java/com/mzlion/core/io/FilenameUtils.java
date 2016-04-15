package com.mzlion.core.io;

import com.mzlion.core.lang.StringUtils;

import java.io.File;

/**
 * Created by mzlion on 2016/4/15.
 */
public class FilenameUtils {

    //---------------------------------------------------------------------
    // constant fields
    // ---------------------------------------------------------------------
    /**
     * LINUX系统下目录分隔符
     */
    public static final String LINUX_SEPARATOR = "/";

    /**
     * Windows下的目录分隔符
     */
    public static final String WINDOWS_SEPARATOR = "\\";

    /**
     * 文件名和文件类型的分隔符
     */
    public static final String EXTENSION_SEPARATOR = ".";

    /**
     * 从文件路径中提取文件名,不支持Windows系统下的路径
     * <pre class="code">
     * StringUtils.getFilename("/opt/app/config.proerties"); //---> config.proerties
     * </pre>
     *
     * @param path 文件路径
     * @return 返回文件名或者返回{@code null}如果为空时
     */
    public static String getFilename(String path) {
        if (!StringUtils.hasLength(path)) {
            return null;
        }
        int separatorIndex = path.lastIndexOf(WINDOWS_SEPARATOR);
        if (separatorIndex == -1) {
            separatorIndex = path.lastIndexOf(LINUX_SEPARATOR);
        }
        return separatorIndex != -1 ? path.substring(separatorIndex + 1) : path;
    }

    /**
     * 从文件路径中提取文件后缀名
     * <pre class="code">
     * StringUtils.getFilenameExtension("/opt/app/config.proerties"); //---> properties
     * </pre>
     *
     * @param path 文件路径
     * @return 返回文件后缀名或者返回{@code null}如果为空时
     */
    public static String getFilenameExtension(String path) {
        String filename = getFilename(path);
        int extIndex = filename.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return null;
        }
        return filename.substring(extIndex + 1);
    }

    public static String getFilenameExtension(File file) {
        if (file == null) {
            return null;
        }
        return getFilenameExtension(file.getName());
    }

    /**
     * 从文件路径中删除文件后缀名
     * <pre class="code">
     * StringUtils.stripFilenameExtension("/opt/app/config.proerties"); //---> /opt/app/config
     * </pre>
     *
     * @param path 文件路径
     * @return 返回不带文件后缀名的文件路径
     */
    public static String stripFilenameExtension(String path) {
        String filename = getFilename(path);
        int extIndex = filename.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return path;
        }

        return filename.substring(0, extIndex);
    }

    /**
     * 将相对路径{@code relativePath}转为相对于{@code path}路径下的文件路径
     * <p/>
     * <pre class="code">
     * StringUtils.applyRelativePath("/opt/app/config.proerties", "/xml/jdbc.xml"); //---> /opt/app/xml/jdbc.properties
     * StringUtils.applyRelativePath("/opt/app/config/", "/xml/jdbc.xml"); //---> /opt/app/config/xml/jdbc.properties
     * </pre>
     *
     * @param path         路径(一般是文件的绝对路径)
     * @param relativePath 相对路径
     * @return 返回相对路径的全路径
     */
    public static String applyRelativePath(String path, String relativePath) {
        if (!StringUtils.hasText(path) || !StringUtils.hasText(relativePath)) {
            return null;
        }
        int separatorIndex = path.lastIndexOf(LINUX_SEPARATOR);
        if (separatorIndex != -1) {
            StringBuilder sb = new StringBuilder();
            sb.append(path.substring(0, separatorIndex));
            if (!relativePath.startsWith(LINUX_SEPARATOR)) {
                sb.append(LINUX_SEPARATOR);
            }
            return sb.append(relativePath).toString();
        } else {
            return relativePath;
        }
    }

}
