package com.mzlion.core.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;


/**
 * Created by mzlion on 2016/4/14.
 */
public class FileUtils {
    //log
    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 1kb
     */
    public static final long _1_KB = 1024;

    /**
     * 1MB
     */
    public static final long _1_MB = _1_KB * _1_KB;

    /**
     * The file copy buffer size (30 MB)
     */
    private static final long FILE_COPY_BUFFER_SIZE = _1_MB * 30;


    //=================================================================
    //==========================复制===================================
    //=================================================================

    //========================文件复制==================================

    /**
     * 文件拷贝，如果拷贝文件失败则返回-1
     *
     * @param srcFile 原文件
     * @param output  输出流
     * @return 返回文件的大小
     */
    public static long copyFile(File srcFile, OutputStream output) {
        try {
            return Files.copy(srcFile.toPath(), output);
        } catch (IOException e) {
            logger.error(" ===> 文件拷贝失败", e);
            return -1;
        } finally {
            IOUtils.closeCloseable(output);
        }
    }

    /**
     * 文件拷贝
     *
     * @param srcFile  原文件
     * @param destFile 目标文件
     * @return 拷贝成功则返回{@code true}，否则返回{@code false}
     */
    public static boolean copyFile(File srcFile, File destFile) {
        return copyFile(srcFile, destFile, true);
    }

    /**
     * 文件拷贝
     *
     * @param srcFile      原文件
     * @param destFile     目标文件
     * @param holdFileDate 保持最后修改日期不变
     * @return 拷贝成功则返回{@code true}，否则返回{@code false}
     */
    public static boolean copyFile(File srcFile, File destFile, boolean holdFileDate) {
        if (srcFile == null) {
            logger.error(" ===> SrcFile must not be null.");
            return false;
        }
        if (destFile == null) {
            logger.error(" ===> DestFile must not be null.");
            return false;
        }
        if (!srcFile.exists()) {
            logger.error(" ===> Source [{}] does not exist.", srcFile);
            return false;
        }
        if (srcFile.isDirectory()) {
            logger.error(" ===> Source [{}] exists but it is a directory.");
            return false;
        }
        try {
            if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
                logger.error(" ===> Source [{}] and destination [{}] are the same.", srcFile, destFile);
                return false;
            }
            File parentFile = destFile.getParentFile();
            if (parentFile != null) {
                if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
                    logger.error(" ===> Destination [{}] directory cannot be created.", parentFile);
                    return false;
                }
            }
            if (destFile.exists() && !destFile.canWrite()) {
                logger.error(" ===> Destination [{}] directory cannot be written.", parentFile);
                return false;
            }
            doCopyFile(srcFile, destFile, holdFileDate);
            return true;
        } catch (IOException e) {
            logger.error(" ===> Process File throw IOException->", e);
            return false;
        }
    }

    private static boolean doCopyFile(File srcFile, File destFile, boolean holdFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            logger.error(" ===> Destination [{}] exists but it is a directory.");
            return false;
        }

        try (
                FileInputStream inputStream = new FileInputStream(srcFile);
                FileOutputStream outputStream = new FileOutputStream(destFile);
                FileChannel input = inputStream.getChannel();
                FileChannel output = outputStream.getChannel();
        ) {
            long size = input.size(), pos = 0, count = 0;
            while (pos < size) {
                count = size - pos > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : size - pos;
                pos += output.transferFrom(input, pos, count);
            }
            if (srcFile.length() != destFile.length()) {
                throw new IOException(String.format(" ===> Failed to copy full contents from [%s] to [%s]", srcFile.getPath(), destFile.getPath()));
            }
            if (holdFileDate) {
                destFile.setLastModified(srcFile.lastModified());
            }
            return true;
        }
    }

    /**
     * 将文件拷贝到目录
     *
     * @param srcFile 原文件
     * @param destDir 目标目录
     * @return 拷贝成功则返回{@code true}，否则返回{@code false}
     */
    public static boolean copyFileToDirectory(File srcFile, File destDir) {
        return copyFileToDirectory(srcFile, destDir, true);
    }

    //========================目录复制==================================

    /**
     * 将文件拷贝到目录
     *
     * @param srcFile      原文件
     * @param destDir      目标目录
     * @param holdFileDate 保持最后修改日期不变
     * @return 拷贝成功则返回{@code true}，否则返回{@code false}
     */
    private static boolean copyFileToDirectory(File srcFile, File destDir, boolean holdFileDate) {
        if (destDir == null) {
            logger.error(" ===> DestDir must not be null.");
            return false;
        }
        if (destDir.exists() && !destDir.isDirectory()) {
            logger.error(" ===> Destination [{}] is not a directory.");
            return false;
        }
        File destFile = new File(destDir, srcFile.getName());
        return copyFile(srcFile, destFile, holdFileDate);
    }

    /**
     * 目录复制
     *
     * @param srcDir  原目录
     * @param destDir 目标目录
     * @return 拷贝成功则返回{@code true}，否则返回{@code false}
     */
    public static boolean copyDirectory(File srcDir, File destDir) {
        return copyDirectory(srcDir, destDir, true);
    }

    /**
     * 目录复制
     *
     * @param srcDir       原目录
     * @param destDir      目标目录
     * @param holdFileDate 保持最后修改日期不变
     * @return 拷贝成功则返回{@code true}，否则返回{@code false}
     */
    public static boolean copyDirectory(File srcDir, File destDir, boolean holdFileDate) {
        return copyDirectory(srcDir, destDir, holdFileDate, null);
    }

    /**
     * 目录复制
     *
     * @param srcDir       原目录
     * @param destDir      目标目录
     * @param holdFileDate 保持最后修改日期不变
     * @param filter       文件过滤器
     * @return 拷贝成功则返回{@code true}，否则返回{@code false}
     */
    public static boolean copyDirectory(File srcDir, File destDir, boolean holdFileDate, FileFilter filter) {
        if (srcDir == null) {
            logger.error(" ===> SrcDir must not be null.");
            return false;
        }
        if (destDir == null) {
            logger.error(" ===> DestDir must not be null.");
            return false;
        }
        if (!srcDir.exists()) {
            logger.error(" ===> Source [{}] does not exist.", srcDir);
            return false;
        }
        if (destDir.isDirectory()) {
            logger.error(" ===> Destination [{}] exists but it is a directory.");
            return false;
        }

        try {
            if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
                logger.error(" ===> Source [{}] and destination [{}] are the same.", srcDir, destDir);
                return false;
            }
            //当目标目录是原目录的子目录时,不支持复制.
            if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
                logger.error(" ===> Destination [{}] is child Directory of source [{}] are the same.", destDir, srcDir);
                return false;
            }
            return doCopyDirectory(srcDir, destDir, holdFileDate, filter);
        } catch (IOException e) {
            logger.error(" ===> Process File throw IOException->", e);
            return false;
        }
    }

    /**
     * 目录复制
     *
     * @param srcDir       原目录
     * @param destDir      目标目录
     * @param holdFileDate 保持最后修改日期不变
     * @param filter       文件过滤器
     * @return 拷贝成功则返回{@code true}，否则返回{@code false}
     * @throws IOException 拷贝异常
     */
    private static boolean doCopyDirectory(File srcDir, File destDir, boolean holdFileDate, FileFilter filter) throws IOException {
        File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
        if (srcFiles == null) {
            logger.error(" ===> Failed to list contents of [{}].", srcDir);
            return false;
        }
        if (destDir.exists() && !destDir.isDirectory()) {
            logger.error(" ===> Destination [{}] exists but is not a directory", destDir);
            return false;
        }
        if (!destDir.mkdirs() && !destDir.isDirectory()) {
            logger.error(" ===> Destination [{}] directory cannot be created.");
            return false;
        }
        if (!destDir.canWrite()) {
            logger.error("Destination [{}] cannot be written to.", destDir);
            return false;
        }
        for (File srcFile : srcFiles) {
            File destFile = new File(destDir, srcFile.getName());
            if (srcFile.isDirectory()) {
                if (!doCopyDirectory(srcFile, destFile, holdFileDate, filter)) {
                    logger.error(" ===> Copy directory failed.");
                    return false;
                }
            } else {
                if (!doCopyFile(srcFile, destFile, holdFileDate)) {
                    logger.error(" ===> Copy file failed.");
                    return false;
                }
            }
        }

        if (holdFileDate) {
            destDir.setLastModified(srcDir.lastModified());
        }
        return true;
    }


    //=================================================================
    //========================剪切/移动=================================
    //=================================================================

    //========================移动文件=================================


    public static boolean moveFile(File srcFile, File destFile) {
        if (srcFile == null) {
            logger.error(" ===> Source must not be null.");
            return false;
        }
        if (destFile == null) {
            logger.error(" ===> Destination must not be null.");
            return false;
        }
        if (!srcFile.exists()) {
            logger.error(" ===> Source [{}] does not exist.", srcFile);
            return false;
        }
        if (!destFile.exists()) {
            logger.error(" ===> Destination [{}] does not exist.", destFile);
            return false;
        }
        if (srcFile.isDirectory()) {
            logger.error(" ===> Source [{}] is a directory.", destFile);
            return false;
        }
        if (srcFile.isFile() && srcFile.exists()) {
            logger.error(" ===> Destination [{}] already exists.", destFile);
            return false;
        }
        if (destFile.isDirectory() && !destFile.canWrite()) {
            logger.error(" ===> Destination [{}] cannot be written to.", destFile);
            return false;
        }
        File targetFile;
        if (destFile.isDirectory()) {
            targetFile = new File(destFile, srcFile.getName());
        } else {
            targetFile = destFile;
        }
        boolean renameTo = srcFile.renameTo(targetFile);
        if (!renameTo) {
            //调用系统的重命名失败(移动),可能属于不同FS文件系统
            if (!copyFile(srcFile, targetFile)) {
                logger.error(" ===> File does copy failed.", destFile);
                return false;
            }
            if (!srcFile.delete()) {
                logger.error(" ===> Failed to delete original file [{}], after copy to [{}}", srcFile, targetFile);
                targetFile.delete();
                return false;
            }
        }
        logger.info(" ===> Original file [{}] moved destination [{}] success.", srcFile, targetFile);
        return true;
    }
}
