package com.mzlion.poi.excel.write;

import com.mzlion.core.lang.StringUtils;
import com.mzlion.poi.beans.PropertyCellMapping;
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

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.Collection;

/**
 * <p>
 * 2016-06-10 Excel的导出引擎
 * </p>
 *
 * @author mzlion
 */
public class ExcelWriterEngine {
    //slf4j
    private final Logger logger = LoggerFactory.getLogger(ExcelWriterEngine.class);
    //导出配置对象
    private final ExcelWriteConfig excelWriteConfig;

    Workbook workbook;
    //Excel单元格样式接口
    ExcelCellStyle excelCellStyle;

    public ExcelWriterEngine(ExcelWriteConfig excelWriteConfig) {
        this.excelWriteConfig = excelWriteConfig;
    }

    public <E> void write(Collection<E> dataSet, OutputStream outputStream) {
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

            long start = System.currentTimeMillis();
            logger.debug(" ===> ExcelEntity export is starting,bean class is {},excel version is {}",
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
            System.out.println("create sheet cost " + (System.currentTimeMillis() - start));
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
        for (PropertyCellMapping propertyCellMapping : this.excelWriteConfig.getPropertyCellMappingList()) {
            sheet.setColumnWidth(propertyCellMapping.getCellIndex(), (int) (256 * propertyCellMapping.getWidth()));
        }
    }

    private int createTitleRow(Sheet sheet) {
        if (StringUtils.hasText(this.excelWriteConfig.getTitle())) {
            Row row = sheet.createRow(0);
            row.setHeightInPoints(this.excelWriteConfig.getTitleRowHeight());

            Cell cell = row.createCell(0);
            cell.setCellValue(this.excelWriteConfig.getTitle());
            if (this.excelCellStyle != null) cell.setCellStyle(this.excelCellStyle.getTitleCellStyle());

            int size = this.excelWriteConfig.getPropertyCellMappingList().size();
            for (int i = 1; i < size; i++) {
                cell = row.createCell(i);
                cell.setCellValue("");
                if (this.excelCellStyle != null) cell.setCellStyle(this.excelCellStyle.getTitleCellStyle());
            }
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, size - 1));
            return 1;
        }
        return 0;
    }

    private int createSecondTitleRow(Sheet sheet) {
        if (StringUtils.hasText(this.excelWriteConfig.getSecondTitle())) {
            Row row = sheet.createRow(1);
            row.setHeightInPoints(this.excelWriteConfig.getSecondTitleRowHeight());

            Cell cell = row.createCell(0);
            cell.setCellValue(this.excelWriteConfig.getSecondTitle());
            if (this.excelCellStyle != null) cell.setCellStyle(this.excelCellStyle.getSecondTitleCellStyle());

            int size = this.excelWriteConfig.getPropertyCellMappingList().size();
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
        if (this.excelWriteConfig.isHeaderRowCreate()) {
            Row row = sheet.createRow(startRow);
            int size = this.excelWriteConfig.getPropertyCellMappingList().size();
            Cell cell;
            PropertyCellMapping propertyCellMapping;
            for (int i = 0; i < size; i++) {
                cell = row.createCell(i);
                propertyCellMapping = this.excelWriteConfig.getPropertyCellMappingList().get(i);
                cell.setCellValue(propertyCellMapping.getTitle());
                if (this.excelCellStyle != null)
                    cell.setCellStyle(this.excelCellStyle.getHeaderCellStyle(propertyCellMapping.getTitle(), i));
            }
            return 1;
        }
        return 0;
    }

    private <E> int createDataRows(Sheet sheet, int startRow, Collection<E> dataSet) {
        Row row;
        int index = 0;
        for (E entity : dataSet) {
            row = sheet.createRow(index + startRow);
            for (PropertyCellMapping propertyCellMapping : this.excelWriteConfig.getPropertyCellMappingList()) {
                CellGenerator<E> cellGenerator = new CellGenerator<>(this);
                cellGenerator.generate(row, entity, propertyCellMapping);
            }
            index++;
        }
        return 0;
    }

}
