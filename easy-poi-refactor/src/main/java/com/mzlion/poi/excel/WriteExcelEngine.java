package com.mzlion.poi.excel;

import com.mzlion.core.lang.ArrayUtils;
import com.mzlion.core.lang.ClassUtils;
import com.mzlion.core.lang.CollectionUtils;
import com.mzlion.core.util.ReflectionUtils;
import com.mzlion.poi.annotation.ExcelCell;
import com.mzlion.poi.annotation.ExcelMappedEntity;
import com.mzlion.poi.config.ExcelCellHeaderConfig;
import com.mzlion.poi.config.WriteExcelConfig;
import com.mzlion.poi.handler.ExcelCellStyleHandler;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by mzlion on 2016/6/17.
 */
public class WriteExcelEngine<E> {
    //slf4j
    private final Logger logger = LoggerFactory.getLogger(WriteExcelEngine.class);
    //导出配置对象
    private final WriteExcelConfig writeExcelConfig;

    Workbook workbook;
    //Excel单元格样式接口
    ExcelCellStyleHandler styleHandler;

    private List<WriteExcelCellHeaderConfig> writeExcelCellHeaderConfigList;

    public WriteExcelEngine(WriteExcelConfig writeExcelConfig) {
        this.writeExcelConfig = writeExcelConfig;
        if (ClassUtils.isAssignable(Map.class, writeExcelConfig.getRawClass())) {
            this.writeExcelCellHeaderConfigList = this.convertByMapBean();
        } else {
            this.writeExcelCellHeaderConfigList = this.convertByJavaBean(this.writeExcelConfig.getRawClass(), null);
        }
        this.sortPropertyCellMappingList(new ArrayList<>(this.writeExcelCellHeaderConfigList));
        for (WriteExcelCellHeaderConfig writeExcelCellHeaderConfig : this.writeExcelCellHeaderConfigList) {
            System.out.println(writeExcelCellHeaderConfig);
        }
    }

    <E> void write(Collection<E> dataSet, OutputStream out) {
//        if (this.writeExcelConfig.getExcelType().equals(ExcelType.XLS)) {
//            this.workbook = new HSSFWorkbook();
//        } else {
//            if (dataSet.size() > 1000) {
//                this.workbook = new SXSSFWorkbook();
//            } else {
//                this.workbook = new XSSFWorkbook();
//            }
//        }
//        try {
//            if (this.writeExcelConfig.getStyleHandler() != null) {
//                Class<? extends ExcelCellStyleHandler> styleHandler = this.writeExcelConfig.getStyleHandler();
//                Constructor<? extends ExcelCellStyleHandler> excelCellStyleClassConstructor = styleHandler.getConstructor(Workbook.class);
//                this.styleHandler = excelCellStyleClassConstructor.newInstance(workbook);
//            }
//            long start = System.currentTimeMillis();
//            logger.debug(" ===> ExcelEntity export is starting,bean class is {},excel version is {}",
//                    this.writeExcelConfig.getRawClass(), this.writeExcelConfig.getExcelType().toString());
//            Sheet sheet;
//            if (StringUtils.hasText(this.writeExcelConfig.getSheetName())) {
//                sheet = this.workbook.createSheet(this.writeExcelConfig.getSheetName());
//            } else {
//                sheet = this.workbook.createSheet();
//            }
//            this.setColumnIndex(sheet);
//            int row = 0;
//            row += this.createTitleRow(sheet);
//            row += this.createSecondTitleRow(sheet, row);
//            row += this.createHeaderTitleRow(sheet, row);
//            this.createDataRows(sheet, row, dataSet, this.writeExcelConfig.getPropertyCellMappingList());
//
//            workbook.write(out);
//        } catch (IOException | ReflectiveOperationException e) {
//            e.printStackTrace();
//        } finally {
//            PoiUtils.closeQuitely(workbook);
//        }
    }
//
//    /**
//     * 设置sheet列宽
//     *
//     * @param sheet sheet对象
//     */
//    private void setColumnIndex(Sheet sheet) {
//        for (PropertyCellMapping propertyCellMapping : this.writeExcelConfig.getPropertyCellMappingList()) {
//            sheet.setColumnWidth(propertyCellMapping.getCellIndex(), (int) (256 * propertyCellMapping.getWidth()));
//        }
//    }
//
//    private int createTitleRow(Sheet sheet) {
//        if (StringUtils.isEmpty(this.writeExcelConfig.getTitle())) return 0;
//        Row row = sheet.createRow(0);
//        row.setHeightInPoints(this.writeExcelConfig.getTitleRowHeight());
//
//        Cell cell = row.createCell(0);
//        cell.setCellValue(this.writeExcelConfig.getTitle());
//        if (this.styleHandler != null) cell.setCellStyle(this.styleHandler.getTitleCellStyle());
//
//        for (int i = 1; i < this.writeExcelConfig.getRealColCount(); i++) {
//            cell = row.createCell(i);
//            cell.setCellValue("");
//            if (this.writeExcelConfig != null) cell.setCellStyle(this.writeExcelConfig.getTitleCellStyle());
//        }
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, this.writeExcelConfig.getRealColCount() - 1));
//        return 1;
//    }
//
//    private int createSecondTitleRow(Sheet sheet, int rowIndex) {
//        if (StringUtils.isEmpty(this.writeExcelConfig.getSecondTitle())) return 0;
//        Row row = sheet.createRow(rowIndex);
//        row.setHeightInPoints(this.writeExcelConfig.getSecondTitleRowHeight());
//
//        Cell cell = row.createCell(0);
//        cell.setCellValue(this.writeExcelConfig.getSecondTitle());
//        if (this.styleHandler != null) cell.setCellStyle(this.styleHandler.getSecondTitleCellStyle());
//
//        for (int i = 1; i < this.writeExcelConfig.getRealColCount(); i++) {
//            cell = row.createCell(i);
//            if (this.styleHandler != null) cell.setCellStyle(this.styleHandler.getSecondTitleCellStyle());
//        }
//
//        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, this.writeExcelConfig.getRealColCount() - 1));
//        return 1;
//
//    }
//
//    private int createHeaderTitleRow(Sheet sheet, int startRow) {
//        if (!this.writeExcelConfig.isHeaderRowCreate()) return 0;
//        Row row = sheet.createRow(startRow), subRow = null;
//
//        int size = this.writeExcelConfig.getPropertyCellMappingList().size();
//        Cell cell;
//        PropertyCellMapping propertyCellMapping;
//        int startColIndex = 0, returnRow = 1;
//        List<CellRangeAddress> cellRangeAddressList = new ArrayList<>();
//        for (int i = 0; i < size; i++) {
//            cell = row.createCell(i);
//            propertyCellMapping = this.writeExcelConfig.getPropertyCellMappingList().get(i);
//            cell.setCellValue(propertyCellMapping.getTitle());
//            List<PropertyCellMapping> childrenMapping = propertyCellMapping.getChildrenMapping();
//            if (CollectionUtils.isNotEmpty(childrenMapping)) {
//                if (childrenMapping.size() > 1) {
//                    if (subRow == null) {
//                        subRow = sheet.createRow(startRow + 1);
//                        this.createHeaderTitleEmptyCells(subRow, i);
//                        startColIndex = i;
//                        returnRow = 2;
//
//
//                    }
//                    for (PropertyCellMapping cellMapping : childrenMapping) {
//                        this.createHeaderTitleCell(subRow, cellMapping, startColIndex);
//                    }
//                    cellRangeAddressList.add(new CellRangeAddress(startRow, startRow, startColIndex, startColIndex + childrenMapping.size() - 1));
//                }
//            }
//            if (this.styleHandler != null)
//                cell.setCellStyle(this.styleHandler.getHeaderCellStyle(propertyCellMapping.getTitle(), i));
//            if (CollectionUtils.isNotEmpty(cellRangeAddressList)) {
//                for (CellRangeAddress cellRangeAddress : cellRangeAddressList) {
//                    sheet.addMergedRegion(cellRangeAddress);
//                }
//            }
//        }
//        return returnRow;
//    }
//
//    int createDataRows(Sheet sheet, int startRow, Collection<?> dataSet, List<PropertyCellMapping> propertyCellMappingList) {
//        Row row;
//        int index = 0;
//        for (Object entity : dataSet) {
//            row = sheet.createRow(index + startRow);
//            for (PropertyCellMapping propertyCellMapping : propertyCellMappingList) {
//                ExcelCellGenerator excelCellGenerator = new ExcelCellGenerator(this);
//                excelCellGenerator.generate(sheet, row, entity, propertyCellMapping);
//            }
//            index++;
//        }
//        return 0;
//    }

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
    }

}
