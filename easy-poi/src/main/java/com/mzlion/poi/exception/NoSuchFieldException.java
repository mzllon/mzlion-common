package com.mzlion.poi.exception;

/**
 * Signals that the class doesn't have a field of a specified name.
 *
 * @author mzlion
 * @date 2016-06-13
 */
public class NoSuchFieldException extends ExcelException {

    /**
     * Construct a {@code NoSuchFieldException} with the specified detail message.
     *
     * @param fieldName the name of field.
     * @param clazz     the class
     */
    public NoSuchFieldException(String fieldName, Class<?> clazz) {
        super(String.format("the field [%s] not exists class [%s]", fieldName, clazz.getName()));
    }
}
