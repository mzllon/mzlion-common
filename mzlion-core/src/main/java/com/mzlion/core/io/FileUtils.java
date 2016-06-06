package com.mzlion.core.io;

import com.mzlion.core.exceptions.FatalFileException;
import com.mzlion.core.lang.Assert;

import java.io.*;
import java.nio.channels.FileChannel;


/**
 * <p>
 * 2016-06-05 09:30 文件工具类
 * </p>
 * <p>
 * 该工具类的部分实现参照了<s>commons-io</s>框架提供的方法。
 * </P>
 *
 * @author mzlion
 */
public class FileUtils {

    /**
     * 1kb
     */
    public static final long ONE_KB = 1024;

    /**
     * 1MB
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;

    /**
     * The file copy buffer size (30 MB)
     */
    private static final long FILE_COPY_BUFFER_SIZE = ONE_MB * 30;

    /**
     * 平台临时目录{@link File}
     *
     * @return 目录
     */
    public static File getTempDirectory() {
        return new File(getTempDirectoryPath());
    }

    /**
     * 平台的临时目录
     *
     * @return 目录
     */
    public static String getTempDirectoryPath() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * 用户主目录
     *
     * @return 用户主目录{@code File}
     */
    public static File getUserDirectory() {
        return new File(getUserDirectoryPath());
    }

    /**
     * 返回用户的主目录
     *
     * @return 用户主目录路径
     */
    public static String getUserDirectoryPath() {
        return System.getProperty("user.home");
    }

    //========================File copy==================================

    /**
     * 文件拷贝，如果拷贝文件失败则返回-1
     *
     * @param srcFile 原文件
     * @param output  输出流
     * @return 返回文件的大小
     */
    public static long copyFile(File srcFile, OutputStream output) {
        Assert.assertNotNull(output, "OutputStream must not be null.");
        Assert.assertNotNull(srcFile, "Source file must not be null.");
        FileInputStream in = openFileInputStream(srcFile);
        try {
            return IOUtils.copyLarge(in, output);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 文件拷贝
     *
     * @param srcFile  原文件
     * @param destFile 目标文件
     */
    public static void copyFile(File srcFile, File destFile) {
        copyFile(srcFile, destFile, true);
    }

    /**
     * 文件拷贝
     *
     * @param srcFile      原文件
     * @param destFile     目标文件
     * @param holdFileDate 保持最后修改日期不变
     */
    public static void copyFile(File srcFile, File destFile, boolean holdFileDate) {
        Assert.assertNotNull(srcFile, "Source file must not be null.");
        Assert.assertNotNull(destFile, "Destination file must not be null.");
        if (!srcFile.exists()) throw new FatalFileException("Source [" + srcFile + "] does not exist.");
        if (srcFile.isDirectory())
            throw new FatalFileException("Source [" + srcFile + "] exists but it is a directory.");

        try {
            if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath()))
                throw new FatalFileException(String.format("Source [%s] and destination [%s] are the same.", srcFile, destFile));
            File parentFile = destFile.getParentFile();
            if (parentFile != null) {
                if (!parentFile.mkdirs() && !parentFile.isDirectory())
                    throw new FatalFileException("Destination [" + parentFile + "] directory cannot be created.");
            }
            if (destFile.exists() && !destFile.canWrite())
                throw new FatalFileException(" ===> Destination [" + parentFile + "] directory cannot be written.");
            doCopyFile(srcFile, destFile, holdFileDate);
        } catch (IOException e) {
            throw new FatalFileException(e);
        }
    }

    /**
     * 文件复制内部方法
     *
     * @param srcFile      原文件
     * @param destFile     目标文件
     * @param holdFileDate 保持最后修改日期不变
     * @throws IOException I/O异常
     */
    private static void doCopyFile(File srcFile, File destFile, boolean holdFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory())
            throw new FatalFileException("Destination [" + destFile + "] exists but it is a directory.");

        try (FileInputStream in = new FileInputStream(srcFile);
             FileOutputStream out = new FileOutputStream(destFile);
             FileChannel inChannel = in.getChannel();
             FileChannel outChannel = out.getChannel()) {
            long size = inChannel.size(), pos = 0, count;
            while (pos < size) {
                count = size - pos > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : size - pos;
                pos += outChannel.transferFrom(inChannel, pos, count);
            }
        }
        //必须放在try(){}之外，否则该修改无效
        if (srcFile.length() != destFile.length()) {
            throw new IOException(String.format("Failed to copy full contents from [%s] to [%s]", srcFile.getPath(), destFile.getPath()));
        }
        if (holdFileDate) destFile.setLastModified(srcFile.lastModified());
    }

    /**
     * 将文件拷贝到目录
     *
     * @param srcFile 原文件
     * @param destDir 目标目录
     * @return 拷贝成功则返回{@code true}，否则返回{@code false}
     */
    public static void copyFileToDirectory(File srcFile, File destDir) {
        copyFileToDirectory(srcFile, destDir, true);
    }

    /**
     * 将文件拷贝到目录
     *
     * @param srcFile      原文件
     * @param destDir      目标目录
     * @param holdFileDate 保持最后修改日期不变
     * @return 拷贝成功则返回{@code true}，否则返回{@code false}
     */
    private static void copyFileToDirectory(File srcFile, File destDir, boolean holdFileDate) {
        Assert.assertNotNull(srcFile, "Source file must not be null.");
        Assert.assertNotNull(destDir, "Destination Directory must not be null.");
        if (destDir.exists() && !destDir.isDirectory())
            throw new FatalFileException("Destination [" + destDir + "] is not a directory.");

        File destFile = new File(destDir, srcFile.getName());
        copyFile(srcFile, destFile, holdFileDate);
    }


    //--------------------------------Directory copy--------------------------------

    /**
     * 目录复制
     *
     * @param srcDir  原目录
     * @param destDir 目标目录
     */
    public static void copyDirectory(File srcDir, File destDir) {
        copyDirectory(srcDir, destDir, true);
    }

    /**
     * 目录复制
     *
     * @param srcDir       原目录
     * @param destDir      目标目录
     * @param holdFileDate 保持最后修改日期不变
     */
    public static void copyDirectory(File srcDir, File destDir, boolean holdFileDate) {
        copyDirectory(srcDir, destDir, holdFileDate, null);
    }

    /**
     * 目录复制
     *
     * @param srcDir       原目录
     * @param destDir      目标目录
     * @param holdFileDate 保持最后修改日期不变
     * @param filter       文件过滤器
     */
    public static void copyDirectory(File srcDir, File destDir, boolean holdFileDate, FileFilter filter) {
        Assert.assertNotNull(srcDir, "Source Directory must not be null.");
        Assert.assertNotNull(destDir, "Destination Directory must not be null.");
        if (!srcDir.exists()) throw new FatalFileException("Source [" + srcDir + "] does not exist.");
        if (destDir.isFile())
            throw new FatalFileException("Destination [" + destDir + "] exists but is not a directory.");

        try {
            if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath()))
                throw new FatalFileException(String.format("Source [%s] and destination [%s] are the same.", srcDir, destDir));
            //当目标目录是原目录的子目录时,不支持复制.
            if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath() + File.separator))
                throw new FatalFileException(String.format("Destination [%s] is child directory of source [%s].", destDir, srcDir));
            doCopyDirectory(srcDir, destDir, holdFileDate, filter);
        } catch (IOException e) {
            throw new FatalFileException(e);
        }
    }

    /**
     * 目录复制
     *
     * @param srcDir       原目录
     * @param destDir      目标目录
     * @param holdFileDate 保持最后修改日期不变
     * @param filter       文件过滤器
     * @throws IOException 拷贝异常
     */
    private static void doCopyDirectory(File srcDir, File destDir, boolean holdFileDate, FileFilter filter) throws IOException {
        File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
        if (srcFiles == null) {
            throw new IOException("Failed to list contents of [" + srcDir + "]");
        }
        if (destDir.exists() && !destDir.isDirectory())
            throw new IOException("Destination [" + destDir + "] exists but is not a directory.");
        if (!destDir.mkdirs() && !destDir.isDirectory())
            throw new IOException("Destination [" + destDir + "] directory cannot be created.");
        if (!destDir.canWrite()) throw new IOException("Destination [" + destDir + "] cannot be written to.");

        for (File srcFile : srcFiles) {
            File destFile = new File(destDir, srcFile.getName());
            if (srcFile.isDirectory()) {
                doCopyDirectory(srcFile, destFile, holdFileDate, filter);
            } else {
                doCopyFile(srcFile, destFile, holdFileDate);
            }
        }

        if (holdFileDate) {
            destDir.setLastModified(srcDir.lastModified());
        }
    }


    //--------------------------------move--------------------------------

    /**
     * 移动文件
     *
     * @param srcFile  原文件
     * @param destFile 目标文件
     * @throws FatalFileException
     */
    public static void moveFile(File srcFile, File destFile) {
        Assert.assertNotNull(srcFile, "Source must not be null.");
        Assert.assertNotNull(destFile, "Destination must not be null.");
        if (!srcFile.exists()) throw new FatalFileException("Source [" + srcFile + "] does not exist.");
        if (srcFile.isDirectory()) throw new FatalFileException("Source [" + srcFile + "] is a directory.");
//        if (!destFile.exists()) throw new FatalFileException("Destination [" + destFile + "] does not exist.");
        if (destFile.isFile() && destFile.exists())
            throw new FatalFileException("Destination [" + destFile + "] already exists.");
        if (destFile.isDirectory() && !destFile.canWrite())
            throw new FatalFileException("Destination [" + destFile + "] cannot be written to.");

        File targetFile;
        if (destFile.isDirectory()) {
            targetFile = new File(destFile, srcFile.getName());
        } else {
            targetFile = destFile;
        }
        boolean renameTo = srcFile.renameTo(targetFile);
        if (!renameTo) {
            //调用系统的重命名失败(移动),可能属于不同FS文件系统
            copyFile(srcFile, targetFile);
            if (!srcFile.delete()) {
                targetFile.delete();
                throw new FatalFileException(String.format("Failed to delete original file [%s], after copy to [%s]", srcFile, destFile));
            }
        }
    }

    /**
     * 移动目录
     *
     * @param source  原文件或目录
     * @param destDir 目标目录
     * @throws FatalFileException
     */
    public static void moveDirectory(File source, File destDir) {
        moveDirectory(source, destDir, false);
    }

    /**
     * 移动目录
     *
     * @param srcDir  原文件或目录
     * @param destDir 目标目录
     * @param toDir   如果目录不存在，是否创建
     * @throws FatalFileException
     */
    public static void moveDirectory(File srcDir, File destDir, boolean toDir) {
        Assert.assertNotNull(srcDir, "Source must not be null.");
        Assert.assertNotNull(destDir, "Destination must not be null.");
        if (!srcDir.exists()) throw new FatalFileException("Source [" + srcDir + "] does not exist.");
        if (!srcDir.isDirectory()) throw new FatalFileException("Destination [" + srcDir + "] is not a directory.");
        if (destDir.exists() && !destDir.isDirectory())
            throw new FatalFileException("Destination [" + destDir + "] is not a directory.");

        File targetDir = toDir ? new File(destDir, srcDir.getName()) : destDir;

        if (!targetDir.mkdirs()) throw new FatalFileException("Directory [" + targetDir + "] could not be created.");
        boolean renameTo = srcDir.renameTo(targetDir);
        if (!renameTo) {
            copyDirectory(srcDir, targetDir);
            delete(srcDir);
            if (srcDir.exists())
                throw new FatalFileException(String.format("Failed to delete original directory '%s' after copy to '%s'", srcDir, destDir));
        }
    }


    //--------------------------------delete--------------------------------

    /**
     * 文件删除，支持目录删除
     *
     * @param file 文件
     * @throws FatalFileException
     */
    public static void delete(File file) {
        Assert.assertNotNull(file, "File must not be null.");
        if (!file.exists()) return;
        if (file.isDirectory()) {
            cleanDirectory(file);
        }
        if (!file.delete()) {
            throw new FatalFileException("Unable to delete file: " + file);
        }
    }

    /**
     * 清理目录
     *
     * @param directory 目录
     * @throws FatalFileException
     */
    public static void cleanDirectory(File directory) {
        Assert.assertNotNull(directory, "Directory must not be null.");
        if (!directory.exists()) throw new FatalFileException("Directory [" + directory + "] does not exist.");
        if (!directory.isDirectory()) throw new FatalFileException("The [" + directory + "] is not a directory.");
        File[] listFiles = directory.listFiles();
        if (listFiles == null) {
            throw new FatalFileException("Failed to list contents of " + directory);
        }
        for (File listFile : listFiles) {
            if (listFile.isDirectory()) {
                cleanDirectory(listFile);
            }
            if (!listFile.delete()) {
                throw new FatalFileException("Unable to delete file: " + listFile);
            }
        }
    }

    /**
     * 打开文件的输入流，提供了比<code>new FileInputStream(file)</code>更好更优雅的方式.
     *
     * @param file 文件
     * @return {@link FileInputStream}
     * @throws FatalFileException
     */
    public static FileInputStream openFileInputStream(File file) {
        Assert.assertNotNull(file, "File must not be null.");
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new FatalFileException("File '" + file + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw new FatalFileException("File '" + file + "' cannot be read");
            }
            try {
                return new FileInputStream(file);
            } catch (IOException e) {
                throw new FatalFileException(e);
            }
        }
        throw new FatalFileException("File '" + file + "' does not exist");
    }

    /**
     * 打开件输出流
     *
     * @param file 文件
     * @return {@link FileOutputStream}
     */
    public static FileOutputStream openFileOutputStream(File file) {
        return openFileOutputStream(file, false);
    }

    /**
     * 打开件输出流
     *
     * @param file   文件
     * @param append 附加
     * @return {@link FileOutputStream}
     */
    private static FileOutputStream openFileOutputStream(File file, boolean append) {
        Assert.assertNotNull(file, "File must not be null.");
        if (file.exists()) {
            if (file.isDirectory())
                throw new FatalFileException("Destination [" + file + "] exists but is a directory.");
            if (!file.canWrite())
                throw new FatalFileException(String.format("Destination [%s] exists but cannot write.", file));
        } else {
            File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory())
                    throw new FatalFileException("Directory [" + parent + "] could not be created.");
            }
        }
        try {
            return new FileOutputStream(file, append);
        } catch (IOException e) {
            throw new FatalFileException(e);
        }
    }


    //--------------------------------checksum--------------------------------

    public static String md5(File file) {
        Assert.assertNotNull(file, "File must not be null.");
        FileInputStream in = openFileInputStream(file);
        return IOUtils.md5(in);
    }

}
