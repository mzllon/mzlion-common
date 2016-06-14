package com.mzlion.poi.exception;

import java.lang.annotation.Annotation;

/**
 * Signals that the field doesn't have a specified annotation.
 *
 * @author mzlion
 * @date 2016-06-13
 */
public class FieldNoAnnotationException extends ExcelException {

    /**
     * Construct a {@code FieldNoAnnotationException} with the specified detail message.
     *
     * @param fieldName       the name of field.
     * @param annotationClass the annotation class
     */
    public FieldNoAnnotationException(String fieldName, Class<? extends Annotation> annotationClass) {
        super(String.format("the field [%s] doesn't config annotation [%s]", fieldName, annotationClass.getName()));
    }
}
