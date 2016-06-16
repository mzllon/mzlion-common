package com.mzlion.poi.exception;

/**
 * Created by mzlion on 2016/6/16.
 */
public class ReadExcelException extends ExcelException {

    /**
     * Construct a {@code ReadExcelException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public ReadExcelException(String message) {
        super(message);
    }

    /**
     * Construct a {@code ReadExcelException} with the specified detail message and nested exception.
     *
     * @param message the detail message.
     * @param cause   the nested exception
     */
    public ReadExcelException(String message, Throwable cause) {
        super(message, cause);
    }
}
