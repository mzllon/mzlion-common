package com.mzlion.poi.excel;

import com.mzlion.core.lang.ArrayUtils;
import com.mzlion.core.lang.ClassUtils;
import com.mzlion.core.lang.CollectionUtils;
import com.mzlion.core.lang.StringUtils;
import com.mzlion.core.util.ReflectionUtils;
import com.mzlion.poi.annotation.ExcelCell;
import com.mzlion.poi.annotation.ExcelMappedEntity;
import com.mzlion.poi.config.ExcelCellHeaderConfig;
import com.mzlion.poi.config.WriteExcelConfig;
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
class WriteExcelEngine {
    //slf4j
    private final Logger logger = LoggerFactory.getLogger(WriteExcelEngine.class);
    //导出配置对象
    final WriteExcelConfig writeExcelConfig;

    Workbook workbook;
    //Excel单元格样式接口
    ExcelCellStyleHandler styleHandler;

    List<WriteExcelCellHeaderConfig> writeExcelCellHeaderConfigList;
    private int cellCount = 0;

    WriteExcelEngine(WriteExcelConfig writeExcelConfig) {
        this.writeExcelConfig = writeExcelConfig;
        if (ClassUtils.isAssignable(Map.class, writeExcelConfig.getRawClass())) {
            this.writeExcelCellHeaderConfigList = this.convertByMapBean();
        } else {
            this.writeExcelCellHeaderConfigList = this.convertByJavaBean(this.writeExcelConfig.getRawClass(), null);
        }
        this.sortPropertyCellMappingList(new ArrayList<>(this.writeExcelCellHeaderConfigList));
    }

    <E> void write(Collection<E> dataSet, OutputStream out) {
        if (this.writeExcelConfig.getExcelType().equals(ExcelType.XLS)) {
            this.workbook = new HSSFWorkbook();
        } else {
            if (dataSet.size() > 1000) {
                this.workbook = new SXSSFWorkbook();
            } else {
                this.workbook = new XSSFWorkbook();
            }
        }
        try {
            if (this.writeExcelConfig.getStyleHandler() != null) {
                Class<? extends ExcelCellStyleHandler> styleHandler = this.writeExcelConfig.getStyleHandler();
                Constructor<? extends ExcelCellStyleHandler> excelCellStyleClassConstructor = styleHandler.getConstructor(Workbook.class);
                this.styleHandler = excelCellStyleClassConstructor.newInstance(workbook);
            }
            long start = System.currentTimeMillis();
            logger.debug(" ===> ExcelEntity export is starting,bean class is {},excel version is {}",
                    this.writeExcelConfig.getRawClass(), this.writeExcelConfig.getExcelType().toString());
            Sheet sheet;
            if (StringUtils.hasText(this.writeExcelConfig.getSheetName())) {
                sheet = this.workbook.createSheet(this.writeExcelConfig.getSheetName());
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
        for (WriteExcelCellHeaderConfig writeExcelCellHeaderConfig : this.writeExcelCellHeaderConfigList) {
            sheet.setColumnWidth(writeExcelCellHeaderConfig.cellIndex, (int) (256 * writeExcelCellHeaderConfig.width));
            cellCount++;
            List<WriteExcelCellHeaderConfig> children = writeExcelCellHeaderConfig.children;
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
        if (StringUtils.isEmpty(this.writeExcelConfig.getTitle())) return 0;
        Row row = sheet.createRow(0);
        row.setHeightInPoints(this.writeExcelConfig.getTitleRowHeight());

        Cell cell = row.createCell(0);
        cell.setCellValue(this.writeExcelConfig.getTitle());
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
        if (StringUtils.isEmpty(this.writeExcelConfig.getSecondTitle())) return 0;
        Row row = sheet.createRow(rowIndex);
        row.setHeightInPoints(this.writeExcelConfig.getSecondTitleRowHeight());

        Cell cell = row.createCell(0);
        cell.setCellValue(this.writeExcelConfig.getSecondTitle());
        if (this.styleHandler != null) cell.setCellStyle(this.styleHandler.getSecondTitleCellStyle());

        for (int i = 1; i < cellCount; i++) {
            cell = row.createCell(i);
            if (this.styleHandler != null) cell.setCellStyle(this.styleHandler.getSecondTitleCellStyle());
        }

        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, cellCount));
        return 1;

    }

    private int createHeaderTitleRow(Sheet sheet, int startRow) {
        if (!this.writeExcelConfig.isHeaderRowCreate()) return 0;
        int returnRow = 1;
        Row row = sheet.createRow(startRow), subRow;
        Cell cell, subCell;
        for (WriteExcelCellHeaderConfig writeExcelCellHeaderConfig : this.writeExcelCellHeaderConfigList) {
            List<WriteExcelCellHeaderConfig> children = writeExcelCellHeaderConfig.children;
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
                        cell.setCellStyle(this.styleHandler.getHeaderCellStyle(children.get(i).title, children.get(i).cellIndex));
                    if (i == 0) {
                        cell.setCellValue(writeExcelCellHeaderConfig.title);
                    }
                    subCell = subRow.createCell(children.get(i).cellIndex);
                    subCell.setCellValue(children.get(i).title);
                    if (this.styleHandler != null)
                        subCell.setCellStyle(this.styleHandler.getHeaderCellStyle(children.get(i).title, children.get(i).cellIndex));
                }
                sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, writeExcelCellHeaderConfig.cellIndex,
                        writeExcelCellHeaderConfig.cellIndex + size - 1));
            } else {
                cell = row.createCell(writeExcelCellHeaderConfig.cellIndex);
                cell.setCellValue(writeExcelCellHeaderConfig.title);
                if (this.styleHandler != null)
                    cell.setCellStyle(this.styleHandler.getHeaderCellStyle(writeExcelCellHeaderConfig.title, writeExcelCellHeaderConfig.cellIndex));
            }
        }
        return returnRow;
    }

    private List<WriteExcelCellHeaderConfig> convertByMapBean() {
        List<WriteExcelCellHeaderConfig> configs = new ArrayList<>(this.writeExcelConfig.getExcelCellHeaderConfigList().size());
        for (ExcelCellHeaderConfig excelCellHeaderConfig : this.writeExcelConfig.getExcelCellHeaderConfigList()) {
            configs.add(new WriteExcelCellHeaderConfig(excelCellHeaderConfig));
        }
        return configs;
    }

    private List<WriteExcelCellHeaderConfig> convertByJavaBean(Class<?> beanClass, String[] includePropertyNames) {
        List<WriteExcelCellHeaderConfig> configs = new ArrayList<>();
        List<Field> fieldList = ReflectionUtils.getDeclaredFields(beanClass);
        for (Field field : fieldList) {
            if (includePropertyNames != null && !ArrayUtils.containsElement(includePropertyNames, field.getName())) {
                continue;
            }

            ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
            if (excelCell == null) continue;

            WriteExcelCellHeaderConfig writeExcelCellHeaderConfig = new WriteExcelCellHeaderConfig();
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

    private void sortPropertyCellMappingList(List<WriteExcelCellHeaderConfig> writeExcelCellHeaderConfigList) {
        List<WriteExcelCellHeaderConfig> noOrderList = new ArrayList<>(writeExcelCellHeaderConfigList.size() >> 1);
        List<WriteExcelCellHeaderConfig> orderList = new ArrayList<>(writeExcelCellHeaderConfigList.size() >> 1);
        for (WriteExcelCellHeaderConfig writeExcelCellHeaderConfig : writeExcelCellHeaderConfigList) {
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
        this.writeExcelCellHeaderConfigList = new ArrayList<>(writeExcelCellHeaderConfigList.size());
        for (WriteExcelCellHeaderConfig writeExcelCellHeaderConfig : orderList) {
            cellIndex = writeExcelCellHeaderConfig.cellIndex;
            for (i = noOrderIndex; count < cellIndex; i++) {
                WriteExcelCellHeaderConfig config = noOrderList.get(i);
                config.cellIndex = count++;
                this.writeExcelCellHeaderConfigList.add(config);
                noOrderIndex++;
            }
            this.writeExcelCellHeaderConfigList.add(writeExcelCellHeaderConfig);
            count++;
        }
        int size = noOrderList.size();
        if (noOrderIndex < size) {
            for (; noOrderIndex < size; noOrderIndex++) {
                WriteExcelCellHeaderConfig writeExcelCellHeaderConfig = noOrderList.get(noOrderIndex);
                writeExcelCellHeaderConfig.cellIndex = count++;
                this.writeExcelCellHeaderConfigList.add(writeExcelCellHeaderConfig);
            }
        }

        boolean breakSort = false;
        for (WriteExcelCellHeaderConfig writeExcelCellHeaderConfig : this.writeExcelCellHeaderConfigList) {
            if (CollectionUtils.isNotEmpty(writeExcelCellHeaderConfig.children)) {
                breakSort = true;
                break;
            }
        }
        if (breakSort) {
            int autoCellIndex = 0;
            for (WriteExcelCellHeaderConfig writeExcelCellHeaderConfig : this.writeExcelCellHeaderConfigList) {
                writeExcelCellHeaderConfig.cellIndex = autoCellIndex++;
                List<WriteExcelCellHeaderConfig> children = writeExcelCellHeaderConfig.children;
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
