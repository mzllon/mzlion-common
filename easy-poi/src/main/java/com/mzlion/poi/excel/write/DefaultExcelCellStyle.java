package com.mzlion.poi.excel.write;

import com.mzlion.poi.beans.BeanPropertyCellDescriptor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Created by mzlion on 2016/6/10.
 */
public class DefaultExcelCellStyle extends AbsExcelCellStyle {

    private Workbook workbook;
    private CellStyle headerStyle;
    private CellStyle dataCellStyle;

    public DefaultExcelCellStyle(Workbook workbook) {
        this.workbook = workbook;
        this.genHeaderCellStyle();
        this.genDataCellStyle();
    }

    @Override
    public CellStyle getTitleCellStyle() {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);

        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        font.setFontName("宋体");
        cellStyle.setFont(font);

        return cellStyle;
    }

    @Override
    public CellStyle getSecondTitleCellStyle() {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);

        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);

        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setFontName("宋体");
        cellStyle.setFont(font);
        return cellStyle;
    }

    @Override
    public CellStyle getHeaderCellStyle(String title, int cellIndex) {
        return this.headerStyle;
    }

    @Override
    public CellStyle getDataCellStyle(int rowIndex, Object entity, BeanPropertyCellDescriptor beanPropertyCellDescriptor) {
        return this.dataCellStyle;
    }

    @Override
    public CellStyle getStatisticsCellStyle(int order) {
        return null;
    }

    private void genHeaderCellStyle() {
        this.headerStyle = this.workbook.createCellStyle();
        this.headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        this.headerStyle.setAlignment(CellStyle.ALIGN_CENTER);

        this.headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
        this.headerStyle.setBorderTop(CellStyle.BORDER_THIN);
        this.headerStyle.setBorderRight(CellStyle.BORDER_THIN);
        this.headerStyle.setBorderBottom(CellStyle.BORDER_THIN);

        Font font = this.workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setFontName("宋体");
        headerStyle.setFont(font);
    }

    private void genDataCellStyle() {
        this.dataCellStyle = this.workbook.createCellStyle();
        this.dataCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        this.dataCellStyle.setAlignment(CellStyle.ALIGN_CENTER);

        this.dataCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        this.dataCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        this.dataCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        this.dataCellStyle.setBorderBottom(CellStyle.BORDER_THIN);

        Font font = this.workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("宋体");
        this.dataCellStyle.setFont(font);
    }
}
