package com.mzlion.poi.excel.write;

import com.mzlion.poi.beans.PropertyCellMapping;
import com.mzlion.poi.constant.ExcelCellType;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mzlion on 2016/6/10.
 */
public class DefaultExcelCellStyle extends AbsExcelCellStyle {

    private Workbook workbook;
    private CellStyle headerStyle;
    private Map<Integer, CellStyle> dataCellStyle;

    public DefaultExcelCellStyle(Workbook workbook) {
        this.workbook = workbook;
        this.genHeaderCellStyle();
        this.dataCellStyle = new HashMap<>();
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
    public CellStyle getDataCellStyle(int rowIndex, Object value, Class<?> valueClass, PropertyCellMapping propertyCellMapping) {
        CellStyle cellStyle = this.dataCellStyle.get(propertyCellMapping.getCellIndex());
        if (cellStyle == null) {
            cellStyle = this.workbook.createCellStyle();
            cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

            cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            cellStyle.setBorderTop(CellStyle.BORDER_THIN);
            cellStyle.setBorderRight(CellStyle.BORDER_THIN);
            cellStyle.setBorderBottom(CellStyle.BORDER_THIN);

            cellStyle.setWrapText(propertyCellMapping.isAutoWrap());

            if (propertyCellMapping.getType().equals(ExcelCellType.HYPER_LINK)) {
                if (valueClass.equals(String.class)) {
                    Font hyperlinkFont = this.workbook.createFont();
                    hyperlinkFont.setUnderline(Font.U_SINGLE);
                    hyperlinkFont.setColor(HSSFColor.BLUE.index);
                    cellStyle.setFont(hyperlinkFont);
                }
            } else {
                Font font = this.workbook.createFont();
                font.setFontHeightInPoints((short) 10);
                font.setFontName("宋体");
                cellStyle.setFont(font);
            }
            this.dataCellStyle.put(propertyCellMapping.getCellIndex(), cellStyle);
        }

        return cellStyle;
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
}
