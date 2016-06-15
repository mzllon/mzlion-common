package com.mzlion.poi.excel;

import com.mzlion.core.io.FileUtils;
import com.mzlion.core.io.FilenameUtils;
import com.mzlion.core.io.IOUtils;
import com.mzlion.core.lang.Assert;
import com.mzlion.poi.config.ExcelReadConfig;
import com.mzlion.poi.config.ExcelWriteConfig;
import com.mzlion.poi.constant.ExcelType;
import com.mzlion.poi.exception.ExcelNotSupportedException;

import java.io.*;
import java.util.Collection;
import java.util.List;

/**
 * http://www.oschina.net/p/easy-xls
 * https://git.oschina.net/bingyulei007/bingExcel
 * <p>
 * Created by mzlion on 2016/6/7.
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
        ExcelReadConfig excelReadConfig = new ExcelReadConfig.Builder()
                .beanClass(beanClass).build();
        return read(excelFile, excelReadConfig);
    }

    /**
     * 读取Excel的内容，并且填充到集合中
     *
     * @param excelFile       Excel文件
     * @param <E>             泛型类型
     * @param excelReadConfig 导入的配置选项
     * @return 集合
     */
    public static <E> List<E> read(File excelFile, ExcelReadConfig excelReadConfig) {
        Assert.notNull(excelFile, "ExcelEntity file must not be null.");
        FileInputStream in = FileUtils.openFileInputStream(excelFile);
        try {
            return read(in, excelReadConfig);
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
        return read(excelInputStream, new ExcelReadConfig.Builder().beanClass(beanClass).build());
    }

    /**
     * 读取Excel的内容，并且填充到集合中
     *
     * @param excelInputStream 必须是Excel的
     * @param <E>              泛型类型
     * @param excelReadConfig  导入的配置选项
     * @return 集合
     */
    public static <E> List<E> read(InputStream excelInputStream, ExcelReadConfig excelReadConfig) {
        Assert.notNull(excelInputStream, "ExcelEntity inputStream must not be null.");
        Assert.notNull(excelReadConfig, "ExcelReadConfig must not be null.");
        ExcelReaderEngine<E> excelReaderEngine = new ExcelReaderEngine<>(excelReadConfig);
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
        ExcelWriteConfig excelWriteConfig = new ExcelWriteConfig.Builder()
                .beanClass(beanClass)
                .title(title)
                .build();
        write(dataSet, excelWriteConfig, output);
    }

    /**
     * 将数据写入到Excel中
     *
     * @param dataSet          待写入的数据
     * @param excelWriteConfig Excel导出配置参数对象
     * @param output           Excel文件保存
     * @param <E>              泛型类型
     */
    public static <E> void write(Collection<E> dataSet, ExcelWriteConfig excelWriteConfig, File output) {
        Assert.notNull(output, "Output file must not be null.");
        Assert.notNull(excelWriteConfig, "ExcelWriteConfig must not be null.");
        FileOutputStream outputStream = null;
        try {
            outputStream = FileUtils.openFileOutputStream(output);
            String suffix = FilenameUtils.getFilenameSuffix(output);
            ExcelType excelType = ExcelType.parse(suffix);
            if (excelType == null) {
                throw new ExcelNotSupportedException("The file [" + output + "] is not a xls or xlsx suffix");
            }
            excelWriteConfig = excelWriteConfig.newBuilder().excelType(excelType).build();
            write(dataSet, excelWriteConfig, outputStream);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * 将数据写入到Excel中
     *
     * @param dataSet          待写入的数据
     * @param excelWriteConfig Excel导出配置参数对象
     * @param output           Excel输出流
     * @param <E>              泛型类型
     */
    public static <E> void write(Collection<E> dataSet, ExcelWriteConfig excelWriteConfig, OutputStream output) {
        Assert.notEmpty(dataSet, "The dataset must not be null or empty.");
        Assert.notNull(excelWriteConfig, "ExcelWriteConfig must not be null.");
        Assert.notNull(output, "Output must not be null.");
        ExcelWriterEngine excelWriterEngine = new ExcelWriterEngine(excelWriteConfig);
        excelWriterEngine.write(dataSet, output);
    }
}
