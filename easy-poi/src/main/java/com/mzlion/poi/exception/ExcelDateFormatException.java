package com.mzlion.poi.exception;

/**
 * Created by mzlion on 2016/6/8.
 */
public class ExcelDateFormatException extends ExcelImportException {

    public ExcelDateFormatException(String headerTitle, Object cellValue, int rowIndex, int cellIndex) {
        super(String.format("The cell header [%s], mapped cell value [%s] need convert java.util.Date at coordinate [%d,%d],but 'dateFormat is empty."
                , headerTitle, cellValue, rowIndex, cellIndex));
    }
}
