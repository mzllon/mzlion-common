package com.mzlion.poi.exception;

/**
 * <p>
 * Open or write excel exception
 * </p>
 *
 * @author mzlion
 * @date 2016-06-10
 */
public class ExcelNotSupportedException extends ExcelException {

    /**
     * Construct a {@code ExcelNotSupportedException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public ExcelNotSupportedException(String message) {
        super(message);
    }
}
