package com.mzlion.poi.exception;

/**
 * Handy class for wrapping runtime {@code Exceptions} with a root cause.
 * <p>
 * 2016-06-09 22:49 This class is {@code abstract} to force the programmer to extend the class.
 * </p>
 *
 * @author mzlion
 * @date 2016-06-16
 * @since 1.7
 */
public abstract class ExcelException extends RuntimeException {

    /**
     * Construct a {@code ExcelException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public ExcelException(String message) {
        super(message);
    }

    /**
     * Construct a {@code ExcelException} with the specified detail message and nested exception.
     *
     * @param message the detail message.
     * @param cause   the nested exception
     */
    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construct a {@code ExcelException} with the  nested exception.
     *
     * @param cause the nested exception
     */
    public ExcelException(Throwable cause) {
        super(cause);
    }
}
