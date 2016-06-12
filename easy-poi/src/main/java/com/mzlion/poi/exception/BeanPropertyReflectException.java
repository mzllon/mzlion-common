package com.mzlion.poi.exception;

/**
 * Getting or setting property value by Reflect happened exception.
 *
 * @author mzlion
 * @date 2016-06-12
 */
public class BeanPropertyReflectException extends ExcelException {

    /**
     * Construct a {@code BeanPropertyReflectException} with the specified detail message and nested exception.
     *
     * @param message the detail message.
     * @param cause   the nested exception
     */
    public BeanPropertyReflectException(String message, Throwable cause) {
        super(message, cause);
    }
}
