package com.mzlion.poi.exception;

/**
 * <p>
 * Importing Excel exception
 * </p>
 *
 * @author mzlion
 * @date 2016-06-09
 */
public class ExcelImportException extends ExcelException {

    /**
     * Construct for {@code ExcelImportException}.
     *
     * @param message the detail message.
     */
    public ExcelImportException(String message) {
        super(message);
    }

    /**
     * Construct for {@code ExcelImportException}.
     *
     * @param message the detail message.
     * @param cause   the nested exception
     */
    public ExcelImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
