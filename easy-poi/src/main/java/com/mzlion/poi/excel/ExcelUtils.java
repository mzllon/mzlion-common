package com.mzlion.poi.excel;

import com.mzlion.core.io.FileUtils;
import com.mzlion.core.io.FilenameUtils;
import com.mzlion.core.io.IOUtils;
import com.mzlion.core.lang.Assert;
import com.mzlion.poi.config.ReadExcelConfig;
import com.mzlion.poi.config.WriteExcelConfig;
import com.mzlion.poi.constant.ExcelType;
import com.mzlion.poi.exception.WriteExcelException;

import java.io.*;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * Excel的处理工具类
 * </p>
 *
 * @author mzlion
 * @date 2016-06-07
 */
public class ExcelUtils {

    /**
     * 读取Excel的内容，并且填充到集合中
     *
     * @param excelFile Excel文件
     * @param beanClass Java对象类型
     * @param <E>       泛型类型
     * @return 集合
     */
    public static <E> List<E> read(File excelFile, Class<E> beanClass) {
        //https://git.oschina.net/bingyulei007/bingExcel
        Assert.isTrue(beanClass != null, "The bean class must not be null.");
        ReadExcelConfig readExcelConfig = new ReadExcelConfig.Builder()
                .rawClass(beanClass).build();
        return read(excelFile, readExcelConfig);
    }

    /**
     * 读取Excel的内容，并且填充到集合中
     *
     * @param excelFile       Excel文件
     * @param <E>             泛型类型
     * @param readExcelConfig Excel读取配置选项
     * @return 集合
     */
    public static <E> List<E> read(File excelFile, ReadExcelConfig readExcelConfig) {
        Assert.notNull(excelFile, "ExcelEntity file must not be null.");
        FileInputStream in = FileUtils.openFileInputStream(excelFile);
        try {
            return read(in, readExcelConfig);
        } finally {
            //close file stream
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 读取Excel的内容，并且填充到集合中
     *
     * @param excelInputStream 必须是Excel的
     * @param beanClass        Java对象类型
     * @param <E>              泛型类型
     * @return 集合
     */
    public static <E> List<E> read(InputStream excelInputStream, Class<E> beanClass) {
        return read(excelInputStream, new ReadExcelConfig.Builder().rawClass(beanClass).build());
    }

    /**
     * 读取Excel的内容，并且填充到集合中
     *
     * @param excelInputStream 必须是Excel的
     * @param <E>              泛型类型
     * @param readExcelConfig  Excel读取配置选项
     * @return 集合
     */
    public static <E> List<E> read(InputStream excelInputStream, ReadExcelConfig readExcelConfig) {
        Assert.notNull(excelInputStream, "ExcelEntity inputStream must not be null.");
        Assert.notNull(readExcelConfig, "ExcelReadConfig must not be null.");
        ReadExcelEngine<E> excelReaderEngine = new ReadExcelEngine<>(readExcelConfig);
        return excelReaderEngine.read(excelInputStream);
    }

    /**
     * 将数据写入到Excel中
     *
     * @param dataSet   待写入的数据
     * @param title     Excel标题
     * @param beanClass JavaBean类型
     * @param output    Excel文件保存
     * @param <E>       泛型类型
     */
    public static <E> void write(Collection<E> dataSet, String title, Class<E> beanClass, File output) {
        WriteExcelConfig writeExcelConfig = new WriteExcelConfig.Builder()
                .rawClass(beanClass)
                .title(title)
                .build();
        write(dataSet, writeExcelConfig, output);
    }

    /**
     * 将数据写入到Excel中
     *
     * @param dataSet          待写入的数据
     * @param writeExcelConfig Excel导出配置参数对象
     * @param output           Excel文件保存
     * @param <E>              泛型类型
     */
    public static <E> void write(Collection<E> dataSet, WriteExcelConfig writeExcelConfig, File output) {
        Assert.notNull(output, "Output file must not be null.");
        Assert.notNull(writeExcelConfig, "WriteExcelConfig must not be null.");
        FileOutputStream outputStream = null;
        try {
            outputStream = FileUtils.openFileOutputStream(output);
            String suffix = FilenameUtils.getFilenameSuffix(output);
            ExcelType excelType = ExcelType.parse(suffix);
            if (excelType == null) {
                throw new WriteExcelException("The file [" + output + "] is not a xls or xlsx suffix");
            }
            writeExcelConfig = writeExcelConfig.newBuilder().excelType(excelType).build();
            write(dataSet, writeExcelConfig, outputStream);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * 将数据写入到Excel中
     *
     * @param dataSet          待写入的数据
     * @param writeExcelConfig Excel导出配置参数对象
     * @param output           Excel输出流
     * @param <E>              泛型类型
     */
    public static <E> void write(Collection<E> dataSet, WriteExcelConfig writeExcelConfig, OutputStream output) {
        Assert.notEmpty(dataSet, "The dataset must not be null or empty.");
        Assert.notNull(writeExcelConfig, "WriteExcelConfig must not be null.");
        Assert.notNull(output, "Output must not be null.");
        WriteExcelEngine writeExcelEngine = new WriteExcelEngine(writeExcelConfig);
        writeExcelEngine.write(dataSet, output);
    }
}
