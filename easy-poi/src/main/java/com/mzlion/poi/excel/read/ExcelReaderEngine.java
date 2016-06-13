package com.mzlion.poi.excel.read;

import com.mzlion.core.lang.StringUtils;
import com.mzlion.core.reflect.StaticFieldFilter;
import com.mzlion.core.util.ReflectionUtils;
import com.mzlion.poi.annotation.ExcelCell;
import com.mzlion.poi.beans.PropertyCellMapping;
import com.mzlion.poi.config.ExcelReadConfig;
import com.mzlion.poi.constant.ExcelType;
import com.mzlion.poi.exception.ExcelCellConfigException;
import com.mzlion.poi.exception.ExcelImportException;
import com.mzlion.poi.exception.ExcelReadException;
import com.mzlion.poi.utils.PoiUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by mzlion on 2016/6/8.
 */
public class ExcelReaderEngine<E> {
    private Logger logger = LoggerFactory.getLogger(ExcelReaderEngine.class);

    /**
     * 导入的配置选项
     */
    private final ExcelReadConfig<E> excelReadConfig;

    private ExcelType excelType = ExcelType.XLSX;
    private List<PropertyCellMapping> propertyCellMappingList = new ArrayList<>();

    public ExcelReaderEngine(ExcelReadConfig<E> excelReadConfig) {
        this.excelReadConfig = excelReadConfig;
    }

    public List<E> read(InputStream excelInputStream) {
        Workbook workbook = null;
        try {
            if (!excelInputStream.markSupported()) excelInputStream = new PushbackInputStream(excelInputStream, 8);
            if (POIFSFileSystem.hasPOIFSHeader(excelInputStream)) {
                excelType = ExcelType.XLS;
                workbook = new HSSFWorkbook(excelInputStream);
            } else if (POIXMLDocument.hasOOXMLHeader(excelInputStream)) {
                workbook = new XSSFWorkbook(OPCPackage.open(excelInputStream));
            } else {
                throw new ExcelReadException("InputStream was not MS ExcelEntity stream.");
            }

            //解析beanClass的属性列表
            this.parseBeanPropertyCellDescriptor();
            //获取公式解析器
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            //记录解析开始时间
            long startTime = System.currentTimeMillis();
            List<E> dataSet = new ArrayList<>();
            //按照sheet读取
            for (int i = this.excelReadConfig.getSheetIndex() - 1; i < this.excelReadConfig.getSheetNum(); i++) {
                logger.debug(" ===> Reading sheet at index[" + i + "]");
                dataSet.addAll(this.parseSheet(workbook.getSheetAt(i), formulaEvaluator));
            }
            logger.debug(" ===> ExcelEntity import process finish,it cost time is {} milliseconds", (System.currentTimeMillis() - startTime));
            return dataSet;
        } catch (IOException | InvalidFormatException e) {
            throw new ExcelImportException("Reading excel failed.", e);
        } finally {
            PoiUtils.closeQuitely(workbook);
        }
    }

    private List<E> parseSheet(Sheet sheet, FormulaEvaluator formulaEvaluator) {
        Iterator<Row> rowIterator = sheet.rowIterator();

        //1.过滤标题占用rows
        for (int i = 0; i < this.excelReadConfig.getTitleRowUsed(); i++) {
            rowIterator.next();
        }
        //2.获取标题头信息
        Map<String, Integer> headerTitleMap = getExcelHeaderTitles(rowIterator);
        if (this.excelReadConfig.isStrict()) {
            //严格模式,Bean定义中的列并且是required的需要出现在Excel的header title中
            for (PropertyCellMapping propertyCellMapping : this.propertyCellMappingList) {
                if (!headerTitleMap.containsKey(propertyCellMapping.getTitle())) {
                    throw new ExcelCellConfigException("The excel header cells can not find '" + propertyCellMapping.getTitle() + "'");
                }
            }
        }
        //将title在Excel的位置缓存
        for (String headerTitle : headerTitleMap.keySet()) {
            for (PropertyCellMapping propertyCellMapping : propertyCellMappingList) {
                if (headerTitle.equals(propertyCellMapping.getTitle())) {
                    propertyCellMapping.setCellIndex(headerTitleMap.get(headerTitle));
                    break;
                }
            }

        }

        List<E> list = new ArrayList<>();
        Row row = null;
        while (rowIterator.hasNext() && (row == null || sheet.getLastRowNum() > row.getRowNum() + this.excelReadConfig.getLastInvalidRow())) {
            row = rowIterator.next();
            DataRowParser<E> dataRowParser = new DataRowParser<>(this.excelReadConfig, this.propertyCellMappingList, formulaEvaluator);
            list.add(dataRowParser.process(row));
        }
        return list;
    }


    private Map<String, Integer> getExcelHeaderTitles(Iterator<Row> rowIterator) {
        Map<String, Integer> headerTitleMap = new HashMap<>();
        for (int i = 0; i < this.excelReadConfig.getHeaderTitleRowUsed(); i++) {
            Row nextRow = rowIterator.next();
            if (nextRow == null) {
                logger.debug(" ===> The excel header row" + (i + 1) + "is empty");
                continue;
            }
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String value = cell.getStringCellValue();
                int columnIndex = cell.getColumnIndex();
                if (StringUtils.isEmpty(value)) {
                    logger.debug(" ===> The header cell" + (columnIndex + 1) + " is empty,ignore.");
                    continue;
                }
                headerTitleMap.put(value, columnIndex);
            }
        }
        return headerTitleMap;
    }

    /**
     * 解析JavaBean中的带有{@code ExcelCell}属性
     */
    private void parseBeanPropertyCellDescriptor() {
        List<Field> fieldList = ReflectionUtils.getDeclaredFields(this.excelReadConfig.getBeanClass());
        fieldList = ReflectionUtils.filter(fieldList, new StaticFieldFilter());
        for (Field field : fieldList) {
            String fieldName = field.getName();
            ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
            if (excelCell != null) {
                PropertyCellMapping propertyCellMapping = new PropertyCellMapping();
                propertyCellMapping.setTitle(excelCell.value());
                propertyCellMapping.setRequired(excelCell.required());
                propertyCellMapping.setPropertyName(fieldName);
                propertyCellMapping.setType(excelCell.type());
                propertyCellMapping.setExcelDateFormat(excelCell.excelDateFormat());
                propertyCellMappingList.add(propertyCellMapping);
            }
        }
    }
}
