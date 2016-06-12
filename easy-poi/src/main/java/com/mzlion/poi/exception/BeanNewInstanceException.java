package com.mzlion.poi.exception;

/**
 * <p>
 * Failed create new object by reflect exception.
 * </p>
 *
 * @author mzlion
 * @date 2016-06-09
 */
public class BeanNewInstanceException extends ExcelImportException {

    /**
     * Construct for {@code ExcelImportException}.
     *
     * @param beanClass JavaBean class
     * @param cause     the nested exception
     */
    public BeanNewInstanceException(Class<?> beanClass, Throwable cause) {
        super("Failed to create new object for class [" + beanClass.getName() + "]", cause);
    }
}
