package com.mzlion.poi.excel.write;

import com.mzlion.core.lang.StringUtils;
import com.mzlion.core.reflect.StaticFieldFilter;
import com.mzlion.core.util.ReflectionUtils;
import com.mzlion.poi.annotation.ExcelCell;
import com.mzlion.poi.beans.BeanPropertyCellDescriptor;
import com.mzlion.poi.config.ExcelWriteConfig;
import com.mzlion.poi.constant.ExcelType;
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

import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 2016-06-10 Excel的导出引擎
 * </p>
 *
 * @param <E> 泛型类型
 * @author mzlion
 */
public class ExcelWriterEngine<E> {
    //slf4j
    private final Logger logger = LoggerFactory.getLogger(ExcelWriterEngine.class);
    //导出配置对象
    private final ExcelWriteConfig excelWriteConfig;

    private Workbook workbook;
    private List<BeanPropertyCellDescriptor> beanPropertyCellDescriptorList = new ArrayList<>();
    //Excel单元格样式接口
    private ExcelCellStyle excelCellStyle;

    public ExcelWriterEngine(ExcelWriteConfig excelWriteConfig) {
        this.excelWriteConfig = excelWriteConfig;
        this.parseBeanPropertyCellDescriptor();
    }

    public void write(Collection<E> dataSet, OutputStream outputStream) {
        try {
            if (this.excelWriteConfig.getExcelType().equals(ExcelType.XLS)) {
                this.workbook = new HSSFWorkbook();
            } else {
                if (dataSet.size() > 1000) {
                    this.workbook = new SXSSFWorkbook();
                } else {
                    this.workbook = new XSSFWorkbook();
                }
            }
            if (this.excelWriteConfig.getExcelCellStyleClass() != null) {
                Class<? extends ExcelCellStyle> excelCellStyleClass = this.excelWriteConfig.getExcelCellStyleClass();
                Constructor<? extends ExcelCellStyle> excelCellStyleClassConstructor = excelCellStyleClass.getConstructor(Workbook.class);
                this.excelCellStyle = excelCellStyleClassConstructor.newInstance(workbook);
            }
//            long start = System.currentTimeMillis();
            logger.debug(" ===> Excel export is starting,bean class is {},excel version is {}",
                    this.excelWriteConfig.getRawClass(), this.excelWriteConfig.getExcelType().toString());
            Sheet sheet;
            if (StringUtils.hasText(this.excelWriteConfig.getSheetName())) {
                sheet = this.workbook.createSheet(this.excelWriteConfig.getSheetName());
            } else {
                sheet = this.workbook.createSheet();
            }
            this.setColumnIndex(sheet);
            int row = 0;
            row += this.createTitleRow(sheet);
            row += this.createSecondTitleRow(sheet);
            row += this.createHeaderTitleRow(sheet, row);
            this.createDataRows(sheet, row, dataSet);

            workbook.write(outputStream);
//            System.out.println("create sheet cost " + (System.currentTimeMillis() - start));
        } catch (Exception e) {
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
        for (BeanPropertyCellDescriptor beanPropertyCellDescriptor : this.beanPropertyCellDescriptorList) {
            sheet.setColumnWidth(beanPropertyCellDescriptor.getCellIndex(), (int) (256 * beanPropertyCellDescriptor.getWidth()));
        }
    }

    private int createTitleRow(Sheet sheet) {
        Row row = sheet.createRow(0);
        row.setHeightInPoints(this.excelWriteConfig.getTitleRowHeight());

        Cell cell = row.createCell(0);
        cell.setCellValue(this.excelWriteConfig.getTitle());
        if (this.excelCellStyle != null) cell.setCellStyle(this.excelCellStyle.getTitleCellStyle());

        int size = this.beanPropertyCellDescriptorList.size();
        for (int i = 1; i < size; i++) {
            cell = row.createCell(i);
            cell.setCellValue("");
            if (this.excelCellStyle != null) cell.setCellStyle(this.excelCellStyle.getTitleCellStyle());
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, size - 1));
        return 1;
    }

    private int createSecondTitleRow(Sheet sheet) {
        if (StringUtils.hasText(this.excelWriteConfig.getSecondTitle())) {
            Row row = sheet.createRow(1);
            row.setHeightInPoints(this.excelWriteConfig.getSecondTitleRowHeight());

            Cell cell = row.createCell(0);
            cell.setCellValue(this.excelWriteConfig.getSecondTitle());
            if (this.excelCellStyle != null) cell.setCellStyle(this.excelCellStyle.getSecondTitleCellStyle());

            int size = this.beanPropertyCellDescriptorList.size();
            for (int i = 1; i < size; i++) {
                cell = row.createCell(i);
                if (this.excelCellStyle != null) cell.setCellStyle(this.excelCellStyle.getSecondTitleCellStyle());
            }

            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, size - 1));
            return 1;
        }
        return 0;
    }

    private int createHeaderTitleRow(Sheet sheet, int startRow) {
        Row row = sheet.createRow(startRow);
        int size = this.beanPropertyCellDescriptorList.size();
        Cell cell;
        BeanPropertyCellDescriptor beanPropertyCellDescriptor;
        for (int i = 0; i < size; i++) {
            cell = row.createCell(i);
            beanPropertyCellDescriptor = this.beanPropertyCellDescriptorList.get(i);
            cell.setCellValue(beanPropertyCellDescriptor.getTitle());
            if (this.excelCellStyle != null)
                cell.setCellStyle(this.excelCellStyle.getHeaderCellStyle(beanPropertyCellDescriptor.getTitle(), i));
        }
        return 1;
    }

    private int createDataRows(Sheet sheet, int startRow, Collection<E> dataSet) {
        Row row;
        int index = 0;
        for (E entity : dataSet) {
            row = sheet.createRow(index + startRow);
            for (BeanPropertyCellDescriptor beanPropertyCellDescriptor : this.beanPropertyCellDescriptorList) {
                CellGenerator<E> cellGenerator = new CellGenerator<>(row, entity, beanPropertyCellDescriptor, this.excelCellStyle);
                cellGenerator.generate();
            }
            index++;
        }
        return 0;
    }

    private void parseBeanPropertyCellDescriptor() {
        List<Field> fieldList = ReflectionUtils.getDeclaredFields(this.excelWriteConfig.getRawClass());
        fieldList = ReflectionUtils.filter(fieldList, new StaticFieldFilter());
        List<BeanPropertyCellDescriptor> noOrderList = new ArrayList<>(fieldList.size());
        List<BeanPropertyCellDescriptor> orderList = new ArrayList<>(fieldList.size());
        for (Field field : fieldList) {
            String fieldName = field.getName();
            ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
            if (excelCell != null) {
                BeanPropertyCellDescriptor beanPropertyCellDescriptor = new BeanPropertyCellDescriptor();
                beanPropertyCellDescriptor.setTitle(excelCell.value());
                beanPropertyCellDescriptor.setRequired(excelCell.required());
                beanPropertyCellDescriptor.setPropertyName(fieldName);
                beanPropertyCellDescriptor.setType(excelCell.type());
                beanPropertyCellDescriptor.setExcelDateFormat(excelCell.excelDateFormat());
                beanPropertyCellDescriptor.setJavaDateFormat(excelCell.javaDateFormat());
                beanPropertyCellDescriptor.setWidth(excelCell.width());
                beanPropertyCellDescriptor.setAutoWrap(excelCell.autoWrap());
                if (excelCell.order() == 0) {
                    noOrderList.add(beanPropertyCellDescriptor);
                } else {
                    beanPropertyCellDescriptor.setCellIndex(excelCell.order() - 1);
                    orderList.add(beanPropertyCellDescriptor);
                }
            }
        }

        //sort order
        Collections.sort(orderList);

        int noOrderIndex = 0, count = 0, cellIndex, i;
        BeanPropertyCellDescriptor bpcd;
        for (BeanPropertyCellDescriptor beanPropertyCellDescriptor : orderList) {
            cellIndex = beanPropertyCellDescriptor.getCellIndex();
            for (i = noOrderIndex; count < cellIndex; i++) {
                bpcd = noOrderList.get(i);
                bpcd.setCellIndex(count++);
                this.beanPropertyCellDescriptorList.add(bpcd);
                noOrderIndex++;
            }
            this.beanPropertyCellDescriptorList.add(beanPropertyCellDescriptor);
            count++;
        }
        int size = noOrderList.size();
        if (noOrderIndex < size) {
            for (; noOrderIndex < size; noOrderIndex++) {
                bpcd = noOrderList.get(noOrderIndex);
                bpcd.setCellIndex(count++);
                this.beanPropertyCellDescriptorList.add(bpcd);
            }
        }
    }
}
