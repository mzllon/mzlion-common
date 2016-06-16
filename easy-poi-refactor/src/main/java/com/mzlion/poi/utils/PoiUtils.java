package com.mzlion.poi.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;

/**
 * Created by mzlion on 2016/6/8.
 */
public class PoiUtils {

    /**
     * 关闭Excel
     *
     * @param workbook Excel工作对象
     */
    public static void closeQuitely(Workbook workbook) {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }

    public static void copyCellStyle(Cell cell, CellStyle cellStyle) {
        cellStyle.setAlignment(cell.getCellStyle().getAlignment());
        cellStyle.setBorderBottom(cell.getCellStyle().getBorderBottom());
        cellStyle.setBorderLeft(cell.getCellStyle().getBorderLeft());
        cellStyle.setBorderRight(cell.getCellStyle().getBorderRight());
        cellStyle.setBorderTop(cell.getCellStyle().getBorderTop());
        cellStyle.setBottomBorderColor(cell.getCellStyle().getBottomBorderColor());
        cellStyle.setDataFormat(cell.getCellStyle().getDataFormat());
        cellStyle.setFillBackgroundColor(cell.getCellStyle().getFillBackgroundColor());
        cellStyle.setFillForegroundColor(cell.getCellStyle().getFillForegroundColor());
        cellStyle.setFillPattern(cell.getCellStyle().getFillPattern());
        cellStyle.setFont(cell.getSheet().getWorkbook().getFontAt(cell.getCellStyle().getFontIndex()));
        cellStyle.setHidden(cell.getCellStyle().getHidden());
        cellStyle.setIndention(cell.getCellStyle().getIndention());
        cellStyle.setLeftBorderColor(cell.getCellStyle().getLeftBorderColor());
        cellStyle.setLocked(cell.getCellStyle().getLocked());
        cellStyle.setRightBorderColor(cell.getCellStyle().getRightBorderColor());
        cellStyle.setRotation(cell.getCellStyle().getRotation());
        cellStyle.setShrinkToFit(cell.getCellStyle().getShrinkToFit());
        cellStyle.setTopBorderColor(cell.getCellStyle().getTopBorderColor());
        cellStyle.setVerticalAlignment(cell.getCellStyle().getVerticalAlignment());
        cellStyle.setWrapText(cell.getCellStyle().getWrapText());
    }
}
