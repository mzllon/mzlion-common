package com.mzlion.core.io;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.PrintWriter;

import static org.junit.Assert.*;

/**
 * Testing for {@link FileUtils}
 * <p>
 * Created by mzlion on 2016/6/5.
 */
public class FileUtilsTest {
    private static File fuFile;
    private static File firstFile;

    @BeforeClass
    public static void before() throws Exception {
        File tempDirectory = FileUtils.getUserDirectory();
        fuFile = new File(tempDirectory, "FileUtils");
        fuFile.mkdirs();
        firstFile = new File(fuFile, "first.txt");
        try (PrintWriter writer = new PrintWriter(firstFile)) {
            writer.println("This is a first file.");
        }
    }

    @Test
    public void getTempDirectory() throws Exception {
        File tempDirectory = FileUtils.getTempDirectory();
        assertNotNull(tempDirectory);
        System.out.println(tempDirectory);
    }

    @Test
    public void getUserDirectory() throws Exception {
        File userDirectory = FileUtils.getUserDirectory();
        assertNotNull(userDirectory);
        System.out.println(userDirectory);
    }

    @Test
    public void copyFile() throws Exception {
        File file = new File(this.getClass().getClassLoader().getResource("info.txt").toURI());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        long count = FileUtils.copyFile(file, out);
        assertEquals(4, count);
        assertEquals("INFO", out.toString());
    }

    @Test
    public void copyFile1() throws Exception {
        File dstFile = new File(fuFile, System.currentTimeMillis() + ".txt");
        FileUtils.copyFile(firstFile, dstFile);
        assertEquals(firstFile.length(), dstFile.length());
        assertEquals(firstFile.lastModified(), dstFile.lastModified());
    }

    @Test
    public void copyFile2() throws Exception {
        File dstFile = new File(fuFile, System.currentTimeMillis() + ".txt");
        FileUtils.copyFile(firstFile, dstFile, true);
        assertEquals(firstFile.lastModified(), dstFile.lastModified());
    }

    @Test
    public void copyFileToDirectory() throws Exception {
        File dstDir = new File(fuFile, "temp");
        FileUtils.copyFileToDirectory(firstFile, dstDir);
        File[] files = dstDir.listFiles();
        assertNotNull(files);
        assertEquals(1, files.length);
        assertEquals(files.length, files[0].length());
    }

    @Test
    public void copyDirectory() throws Exception {
        File srcDir = new File("e:/utilities");
        File destDir = new File("e:/utilities_1");
        System.out.println(srcDir.getCanonicalPath());
        System.out.println(destDir.getCanonicalPath());

        FileUtils.copyDirectory(srcDir, destDir, true, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return true;
            }
        });
        assertTrue(destDir.exists());
        assertEquals(srcDir.list().length, destDir.list().length);
    }

    @Test
    public void copyDirectory1() throws Exception {
//        File srcFile = new File("E:\\utilities_1\\mzlion-parent\\pom.xml");
//        File destFile = new File("E:\\utilities_1\\mzlion-parent\\pom.xml.bak");
//        FileUtils.copyFile(srcFile, destFile, true);
    }

    @Test
    public void copyDirectory2() throws Exception {
        //nop
    }

    @Test
    public void moveFile() throws Exception {
        File srcFile = new File("E:\\utilities_1\\mzlion-parent\\pom.xml");
        File destFile = new File("E:\\utilities_1\\mzlion-parent\\pom.xml.bak");
        FileUtils.moveFile(srcFile, destFile);
    }

    @Test
    public void moveDirectory() throws Exception {
        File srcFile = new File("E:\\utilities_1");
        File destFile = new File("E:\\utilities_2");
        FileUtils.moveDirectory(srcFile, destFile);
    }

    @Test
    public void moveDirectory1() throws Exception {
        File srcFile = new File("E:\\utilities_1");
        File destFile = new File("E:\\utilities_2");
        FileUtils.moveDirectory(srcFile, destFile, true);
    }

    @Test
    public void delete() throws Exception {
        File srcFile = new File("E:\\utilities_1");
        FileUtils.delete(srcFile);
        assertFalse(srcFile.exists());
    }

    @Test
    public void cleanDirectory() throws Exception {
        File srcFile = new File("E:\\utilities_1");
        FileUtils.cleanDirectory(srcFile);
    }

    @Test
    public void openFileInputStream() throws Exception {
        //nop
    }

    @Test
    public void openFileOutputStream() throws Exception {
        //nop
    }


}