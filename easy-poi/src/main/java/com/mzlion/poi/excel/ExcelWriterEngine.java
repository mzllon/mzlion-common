package com.mzlion.poi.excel;

import com.mzlion.core.lang.ArrayUtils;
import com.mzlion.core.lang.ClassUtils;
import com.mzlion.core.lang.CollectionUtils;
import com.mzlion.core.lang.StringUtils;
import com.mzlion.core.util.ReflectionUtils;
import com.mzlion.poi.annotation.ExcelCell;
import com.mzlion.poi.annotation.ExcelMappedEntity;
import com.mzlion.poi.config.ExcelCellHeaderConfig;
import com.mzlion.poi.config.ExcelWriterConfig;
import com.mzlion.poi.constant.ExcelType;
import com.mzlion.poi.style.ExcelCellStyleHandler;
import com.mzlion.poi.utils.PoiUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by mzlion on 2016/6/17.
 */
class ExcelWriterEngine {
    //slf4j
    private final Logger logger = LoggerFactory.getLogger(ExcelWriterEngine.class);
    //导出配置对象
    final ExcelWriterConfig excelWriterConfig;

    Workbook workbook;
    //Excel单元格样式接口
    ExcelCellStyleHandler styleHandler;

    List<ExcelWriterCellHeaderConfig> excelWriterCellHeaderConfigList;
    private int cellCount = 0;

    ExcelWriterEngine(ExcelWriterConfig excelWriterConfig) {
        this.excelWriterConfig = excelWriterConfig;
        if (ClassUtils.isAssignable(Map.class, excelWriterConfig.getRawClass())) {
            this.excelWriterCellHeaderConfigList = this.convertByMapBean();
        } else {
            this.excelWriterCellHeaderConfigList = this.convertByJavaBean(this.excelWriterConfig.getRawClass(), null);
        }
        this.sortPropertyCellMappingList(new ArrayList<>(this.excelWriterCellHeaderConfigList));
    }

    <E> void write(Collection<E> dataSet, OutputStream out) {
        if (this.excelWriterConfig.getExcelType().equals(ExcelType.XLS)) {
            this.workbook = new HSSFWorkbook();
        } else {
            if (dataSet.size() > 1000) {
                this.workbook = new SXSSFWorkbook();
            } else {
                this.workbook = new XSSFWorkbook();
            }
        }
        try {
            if (this.excelWriterConfig.getStyleHandler() != null) {
                Class<? extends ExcelCellStyleHandler> styleHandler = this.excelWriterConfig.getStyleHandler();
                Constructor<? extends ExcelCellStyleHandler> excelCellStyleClassConstructor = styleHandler.getConstructor(Workbook.class);
                this.styleHandler = excelCellStyleClassConstructor.newInstance(workbook);
            }
            long start = System.currentTimeMillis();
            logger.debug(" ===> ExcelEntity export is starting,bean class is {},excel version is {}",
                    this.excelWriterConfig.getRawClass(), this.excelWriterConfig.getExcelType().toString());
            Sheet sheet;
            if (StringUtils.hasText(this.excelWriterConfig.getSheetName())) {
                sheet = this.workbook.createSheet(this.excelWriterConfig.getSheetName());
            } else {
                sheet = this.workbook.createSheet();
            }
            this.setColumnIndex(sheet);
            int row = 0;
            row += this.createTitleRow(sheet);
            row += this.createSecondTitleRow(sheet, row);
            row += this.createHeaderTitleRow(sheet, row);
            DataRowWriter<E> dataRowWriter = new DataRowWriter<>(this);
            dataRowWriter.write(dataSet, sheet, row);

            workbook.write(out);
        } catch (IOException | ReflectiveOperationException e) {
            e.printStackTrace();
        } finally {
            PoiUtils.closeQuitely(workbook);
        }
    }

    /**
     * 设置sheet列宽
     *
     * @param sheet sheet对象
     */
    private void setColumnIndex(Sheet sheet) {
        for (ExcelWriterCellHeaderConfig writeExcelCellHeaderConfig : this.excelWriterCellHeaderConfigList) {
            sheet.setColumnWidth(writeExcelCellHeaderConfig.cellIndex, (int) (256 * writeExcelCellHeaderConfig.width));
            cellCount++;
            List<ExcelWriterCellHeaderConfig> children = writeExcelCellHeaderConfig.children;
            if (CollectionUtils.isNotEmpty(children)) {
                for (int i = 0, size = children.size(); i < size; i++) {
                    if (i != 0) cellCount++;
                    sheet.setColumnWidth(children.get(i).cellIndex, (int) (256 * children.get(i).width));
                }
            }
        }
        this.cellCount--;
    }

    private int createTitleRow(Sheet sheet) {
        if (StringUtils.isEmpty(this.excelWriterConfig.getTitle())) return 0;
        Row row = sheet.createRow(0);
        row.setHeightInPoints(this.excelWriterConfig.getTitleRowHeight());

        Cell cell = row.createCell(0);
        cell.setCellValue(this.excelWriterConfig.getTitle());
        if (this.styleHandler != null) cell.setCellStyle(this.styleHandler.getTitleCellStyle());

        for (int i = 1; i < cellCount; i++) {
            cell = row.createCell(i);
            cell.setCellValue("");
            if (this.styleHandler != null) cell.setCellStyle(this.styleHandler.getTitleCellStyle());
        }

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cellCount));
        return 1;
    }

    private int createSecondTitleRow(Sheet sheet, int rowIndex) {
        if (StringUtils.isEmpty(this.excelWriterConfig.getSecondTitle())) return 0;
        Row row = sheet.createRow(rowIndex);
        row.setHeightInPoints(this.excelWriterConfig.getSecondTitleRowHeight());

        Cell cell = row.createCell(0);
        cell.setCellValue(this.excelWriterConfig.getSecondTitle());
        if (this.styleHandler != null) cell.setCellStyle(this.styleHandler.getSecondTitleCellStyle());

        for (int i = 1; i < cellCount; i++) {
            cell = row.createCell(i);
            if (this.styleHandler != null) cell.setCellStyle(this.styleHandler.getSecondTitleCellStyle());
        }

        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, cellCount));
        return 1;

    }

    private int createHeaderTitleRow(Sheet sheet, int startRow) {
        if (!this.excelWriterConfig.isHeaderRowCreate()) return 0;
        int returnRow = 1;
        Row row = sheet.createRow(startRow), subRow;
        Cell cell, subCell;
        for (ExcelWriterCellHeaderConfig writeExcelCellHeaderConfig : this.excelWriterCellHeaderConfigList) {
            List<ExcelWriterCellHeaderConfig> children = writeExcelCellHeaderConfig.children;
            if (CollectionUtils.isNotEmpty(children)) {
                subRow = sheet.getRow(startRow + 1);
                if (subRow == null) {
                    subRow = sheet.createRow(startRow + 1);
                    returnRow = 2;
                }
                int size = children.size();
                for (int i = 0; i < size; i++) {
                    cell = row.createCell(children.get(i).cellIndex);
                    if (this.styleHandler != null)
                        cell.setCellStyle(this.styleHandler.getHeaderCellStyle(children.get(i).title, children.get(i).cellIndex, cell.getCellStyle()));
                    if (i == 0) {
                        cell.setCellValue(writeExcelCellHeaderConfig.title);
                    }
                    subCell = subRow.createCell(children.get(i).cellIndex);
                    subCell.setCellValue(children.get(i).title);
                    if (this.styleHandler != null)
                        subCell.setCellStyle(this.styleHandler.getHeaderCellStyle(children.get(i).title, children.get(i).cellIndex, cell.getCellStyle()));
                }
                sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, writeExcelCellHeaderConfig.cellIndex,
                        writeExcelCellHeaderConfig.cellIndex + size - 1));
            } else {
                cell = row.createCell(writeExcelCellHeaderConfig.cellIndex);
                cell.setCellValue(writeExcelCellHeaderConfig.title);
                if (this.styleHandler != null)
                    cell.setCellStyle(this.styleHandler.getHeaderCellStyle(writeExcelCellHeaderConfig.title, writeExcelCellHeaderConfig.cellIndex, cell.getCellStyle()));
            }
        }
        return returnRow;
    }

    private List<ExcelWriterCellHeaderConfig> convertByMapBean() {
        List<ExcelWriterCellHeaderConfig> configs = new ArrayList<>(this.excelWriterConfig.getExcelCellHeaderConfigList().size());
        for (ExcelCellHeaderConfig excelCellHeaderConfig : this.excelWriterConfig.getExcelCellHeaderConfigList()) {
            configs.add(new ExcelWriterCellHeaderConfig(excelCellHeaderConfig));
        }
        return configs;
    }

    private List<ExcelWriterCellHeaderConfig> convertByJavaBean(Class<?> beanClass, String[] includePropertyNames) {
        List<ExcelWriterCellHeaderConfig> configs = new ArrayList<>();
        List<Field> fieldList = ReflectionUtils.getDeclaredFields(beanClass);
        if (includePropertyNames != null && includePropertyNames.length == 1 && includePropertyNames[0].equals("*")) {
            includePropertyNames = null;
        }

        for (Field field : fieldList) {
            if (includePropertyNames != null && !ArrayUtils.containsElement(includePropertyNames, field.getName())) {
                continue;
            }

            ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
            if (excelCell == null) continue;
            if (this.excelWriterConfig.getExcludePropertyNames().contains(field.getName())) continue;

            ExcelWriterCellHeaderConfig writeExcelCellHeaderConfig = new ExcelWriterCellHeaderConfig();
            writeExcelCellHeaderConfig.title = excelCell.value();
            writeExcelCellHeaderConfig.excelCellType = excelCell.excelCellType();
            writeExcelCellHeaderConfig.excelDateFormat = excelCell.excelDateFormat();
            writeExcelCellHeaderConfig.javaDateFormat = excelCell.javaDateFormat();
            writeExcelCellHeaderConfig.width = excelCell.width();
            writeExcelCellHeaderConfig.autoWrap = excelCell.autoWrap();
            writeExcelCellHeaderConfig.propertyName = field.getName();
            ExcelMappedEntity excelMappedEntity = field.getAnnotation(ExcelMappedEntity.class);
            if (excelMappedEntity != null) {
                Class<?> targetClass = excelMappedEntity.targetClass();
                writeExcelCellHeaderConfig.children = this.convertByJavaBean(targetClass, excelMappedEntity.propertyNames());
                writeExcelCellHeaderConfig.targetClass = targetClass;
            }
            configs.add(writeExcelCellHeaderConfig);
        }
        return configs;
    }

    private void sortPropertyCellMappingList(List<ExcelWriterCellHeaderConfig> writeExcelCellHeaderConfigList) {
        List<ExcelWriterCellHeaderConfig> noOrderList = new ArrayList<>(writeExcelCellHeaderConfigList.size() >> 1);
        List<ExcelWriterCellHeaderConfig> orderList = new ArrayList<>(writeExcelCellHeaderConfigList.size() >> 1);
        for (ExcelWriterCellHeaderConfig writeExcelCellHeaderConfig : writeExcelCellHeaderConfigList) {
            if (writeExcelCellHeaderConfig.cellIndex == 0) {
                noOrderList.add(writeExcelCellHeaderConfig);
            } else {
                writeExcelCellHeaderConfig.cellIndex = writeExcelCellHeaderConfig.cellIndex - 1;
                orderList.add(writeExcelCellHeaderConfig);
            }
            if (CollectionUtils.isNotEmpty(writeExcelCellHeaderConfig.children)) {
                sortPropertyCellMappingList(writeExcelCellHeaderConfig.children);
            }
        }

        //sort order
        Collections.sort(orderList);

        int noOrderIndex = 0, count = 0, cellIndex, i;
        this.excelWriterCellHeaderConfigList = new ArrayList<>(writeExcelCellHeaderConfigList.size());
        for (ExcelWriterCellHeaderConfig writeExcelCellHeaderConfig : orderList) {
            cellIndex = writeExcelCellHeaderConfig.cellIndex;
            for (i = noOrderIndex; count < cellIndex; i++) {
                ExcelWriterCellHeaderConfig config = noOrderList.get(i);
                config.cellIndex = count++;
                this.excelWriterCellHeaderConfigList.add(config);
                noOrderIndex++;
            }
            this.excelWriterCellHeaderConfigList.add(writeExcelCellHeaderConfig);
            count++;
        }
        int size = noOrderList.size();
        if (noOrderIndex < size) {
            for (; noOrderIndex < size; noOrderIndex++) {
                ExcelWriterCellHeaderConfig writeExcelCellHeaderConfig = noOrderList.get(noOrderIndex);
                writeExcelCellHeaderConfig.cellIndex = count++;
                this.excelWriterCellHeaderConfigList.add(writeExcelCellHeaderConfig);
            }
        }

        boolean breakSort = false;
        for (ExcelWriterCellHeaderConfig writeExcelCellHeaderConfig : this.excelWriterCellHeaderConfigList) {
            if (CollectionUtils.isNotEmpty(writeExcelCellHeaderConfig.children)) {
                breakSort = true;
                break;
            }
        }
        if (breakSort) {
            int autoCellIndex = 0;
            for (ExcelWriterCellHeaderConfig writeExcelCellHeaderConfig : this.excelWriterCellHeaderConfigList) {
                writeExcelCellHeaderConfig.cellIndex = autoCellIndex++;
                List<ExcelWriterCellHeaderConfig> children = writeExcelCellHeaderConfig.children;
                if (CollectionUtils.isNotEmpty(children)) {
                    for (int j = 0, length = children.size(); j < length; j++) {
                        if (j == 0) children.get(j).cellIndex = autoCellIndex - 1;
                        else children.get(j).cellIndex = autoCellIndex++;
                    }
                }
            }
        }

    }

}
