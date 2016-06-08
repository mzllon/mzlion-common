package com.mzlion.poi;

import com.mzlion.core.date.DateUtils;
import com.mzlion.core.io.FileUtils;
import com.mzlion.core.io.IOUtils;
import com.mzlion.core.lang.Assert;
import com.mzlion.core.lang.StringUtils;
import com.mzlion.core.reflect.StaticFieldFilter;
import com.mzlion.core.util.ReflectionUtils;
import com.mzlion.poi.annotation.ExcelCell;
import com.mzlion.poi.beans.ExcelCellDescriptor;
import com.mzlion.poi.config.ExcelImportConfig;
import com.mzlion.poi.exception.ExcelCellConfigException;
import com.mzlion.poi.exception.ExcelParseException;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * http://www.oschina.net/p/easy-xls
 * https://git.oschina.net/bingyulei007/bingExcel
 * <p>
 * Created by mzlion on 2016/6/7.
 */
public class ExcelUtils {

    //slf4j
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

    public static <T> List<T> load(File excelFile, Class<T> targetClass) {
        return load(excelFile, targetClass, new ExcelImportConfig.Builder());
    }

    public static <T> List<T> load(File excelFile, Class<T> targetClass, ExcelImportConfig.Builder builder) {
        Assert.assertNotNull(excelFile, "Excel file must not be null.");
        FileInputStream in = FileUtils.openFileInputStream(excelFile);
        try {
            return load(in, targetClass, builder);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static <T> List<T> load(InputStream excelInputStream, Class<T> targetClass) {
        return load(excelInputStream, targetClass, new ExcelImportConfig.Builder());
    }

    public static <T> List<T> load(InputStream excelInputStream, Class<T> targetClass, ExcelImportConfig.Builder builder) {
        Assert.assertNotNull(excelInputStream, "Excel inputStream must not be null.");
        Assert.assertNotNull(targetClass, "Target class must not be null.");
        Assert.assertNotNull(builder, "ExcelImportConfig.Builder must not be null.");

        LOGGER.debug(" ===> Excel import is processing,target class->{}", targetClass);
        if (!excelInputStream.markSupported()) {
            excelInputStream = new PushbackInputStream(excelInputStream, 8);
        }

        try {
            boolean isXlsx = false;
            Workbook workbook;
            if (POIFSFileSystem.hasPOIFSHeader(excelInputStream)) {
                workbook = new HSSFWorkbook(excelInputStream);
            } else if (POIXMLDocument.hasOOXMLHeader(excelInputStream)) {
                isXlsx = true;
                workbook = new XSSFWorkbook(OPCPackage.open(excelInputStream));
            } else {
                throw new ExcelParseException("Your InputStream was neither an OLE2 stream, nor an OOXML stream");
            }

            LOGGER.debug(" ===> Reading excel data ,start time is {}", DateUtils.formatDate());
            List<T> dataset = new ArrayList<>();
            ExcelImportConfig excelImportConfig = builder.build();
            //按照sheet读取
            for (int i = excelImportConfig.getSheetIndex() - 1; i < excelImportConfig.getSheetNum(); i++) {
                dataset.addAll(parseDataRows(workbook.getSheetAt(i), targetClass, excelImportConfig));
            }
            return dataset;
        } catch (IOException | InvalidFormatException e) {
            throw new ExcelParseException(e);
        }
    }

    private static <T> List<? extends T> parseDataRows(Sheet sheet, Class<T> targetClass, ExcelImportConfig importConfig) {
        List<T> result = new ArrayList<>();

        if (Map.class.equals(targetClass)) {
//            parseDataRowsByMap(sheet, importConfig, result);
        } else {
            parseDataRowsByJavaBean(sheet, importConfig, targetClass, result);
        }
        return result;
    }

    private static <T> void parseDataRowsByJavaBean(Sheet sheet, ExcelImportConfig importConfig, Class<T> targetClass, List<T> result) {
        List<Field> fieldList = ReflectionUtils.getDeclaredFields(targetClass);
        fieldList = ReflectionUtils.filter(fieldList, new StaticFieldFilter());
        LOGGER.debug(" ===> Getting field list by bean->{}", fieldList);
        List<ExcelCellDescriptor> excelCellDescriptorList = getExcelCellDescriptorList(fieldList);

        Iterator<Row> rowIterator = sheet.rowIterator();

        //过滤标题占用rows
        for (int i = 0; i < importConfig.getTitleRowUsed(); i++) {
            rowIterator.next();
        }

        //获取标题头信息
        Map<String, Integer> headerTitleMap = getExcelHeaderTitles(rowIterator, importConfig);
        if (importConfig.isStrict()) {
            //严格模式,Bean定义中的列并且是required的需要出现在Excel的header title中
            for (ExcelCellDescriptor excelCellDescriptor : excelCellDescriptorList) {
                if (!headerTitleMap.containsKey(excelCellDescriptor.getTitle())) {
                    throw new ExcelCellConfigException("The excel header cells can not find '" + excelCellDescriptor.getTitle() + "'");
                }
            }
        }

        //将title在Excel的位置缓存
        List<ExcelCellDescriptor> canReadExcelCellDescriptorList = new ArrayList<>(excelCellDescriptorList.size());
        for (String headerTitle : headerTitleMap.keySet()) {
            for (ExcelCellDescriptor excelCellDescriptor : excelCellDescriptorList) {
                if (headerTitle.equals(excelCellDescriptor.getTitle())) {
                    excelCellDescriptor.setCellIndex(headerTitleMap.get(headerTitle));
                    canReadExcelCellDescriptorList.add(excelCellDescriptor);
                    break;
                }
            }

        }

        //真正处理数据内容了
        try {
            parseDataRowsInternal(sheet, rowIterator, result, targetClass, canReadExcelCellDescriptorList, importConfig);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

    }

    private static <T> void parseDataRowsInternal(Sheet sheet, Iterator<Row> rowIterator, List<T> result, Class<T> targetClass, List<ExcelCellDescriptor> excelCellDescriptorList,
                                                  ExcelImportConfig importConfig) throws ReflectiveOperationException {
        Collections.sort(excelCellDescriptorList);
        Row row = null;
        while (rowIterator.hasNext() &&
                (row == null || sheet.getLastRowNum() - row.getRowNum() > importConfig.getLastInvalidRow())) {
            row = rowIterator.next();
            T instance = targetClass.newInstance();
            for (ExcelCellDescriptor excelCellDescriptor : excelCellDescriptorList) {
                Cell cell = row.getCell(excelCellDescriptor.getCellIndex());
                reflectFieldByCell(instance, cell, excelCellDescriptor);
            }
        }
    }

    private static <T> void reflectFieldByCell(T instance, Cell cell, ExcelCellDescriptor excelCellDescriptor) {

    }

    private static Map<String, Integer> getExcelHeaderTitles(Iterator<Row> rowIterator, ExcelImportConfig importConfig) {
        Map<String, Integer> headerTitleMap = new HashMap<>();
        for (int i = 0; i < importConfig.getHeaderTitleRowUsed(); i++) {
            Row nextRow = rowIterator.next();
            if (nextRow == null) {
                LOGGER.debug(" ===> The excel header row" + (i + 1) + "is empty");
                continue;
            }
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String value = cell.getStringCellValue();
                int columnIndex = cell.getColumnIndex();
                if (StringUtils.isEmpty(value)) {
                    LOGGER.debug(" ===> The header cell" + (columnIndex + 1) + " is empty,ignore.");
                    continue;
                }
                headerTitleMap.put(value, columnIndex);
            }
        }
        return headerTitleMap;
    }

    private static List<ExcelCellDescriptor> getExcelCellDescriptorList(List<Field> fieldList) {
        List<ExcelCellDescriptor> list = new ArrayList<>(fieldList.size());
        for (Field field : fieldList) {
            String fieldName = field.getName();
            ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
            if (excelCell != null) {
                if (StringUtils.isEmpty(excelCell.title())) {
                    throw new ExcelCellConfigException(String.format("The property[%s] of annotation title is null", fieldName));
                }
                ExcelCellDescriptor excelCellDescriptor = new ExcelCellDescriptor();
                excelCellDescriptor.setTitle(excelCell.title());
                excelCellDescriptor.setRequired(excelCell.required());
                excelCellDescriptor.setPropertyName(fieldName);
                excelCellDescriptor.setType(excelCell.type());
                list.add(excelCellDescriptor);
            }
        }
        return list;
    }
}
