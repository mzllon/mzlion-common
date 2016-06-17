package com.mzlion.poi.config;

import com.mzlion.core.lang.CollectionUtils;
import com.mzlion.core.lang.StringUtils;
import com.mzlion.core.util.ReflectionUtils;
import com.mzlion.poi.annotation.ExcelCell;
import com.mzlion.poi.annotation.ExcelEntity;
import com.mzlion.poi.annotation.ExcelId;
import com.mzlion.poi.annotation.ExcelMappedEntity;
import com.mzlion.poi.exception.AnnotationConfigException;
import com.mzlion.poi.exception.ExcelCellHeaderTitleException;
import com.mzlion.poi.exception.ReflectionException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mzlion on 2016/6/17.
 */
public abstract class ExcelEntityValidator {

    void validateJavaBean(Class<?> beanClass) {
        if (!beanClass.isAnnotationPresent(ExcelEntity.class)) {
            throw new AnnotationConfigException(String.format("The class [%s] must config annotation [%s]",
                    beanClass.getName(), ExcelEntity.class.getName()));
        }
        List<Field> declaredFields = ReflectionUtils.getDeclaredFields(beanClass);
        if (CollectionUtils.isEmpty(declaredFields)) {
            throw new ReflectionException("The class [" + beanClass.getName() + "] has no any property.");
        }

        List<String> titleTempList = new ArrayList<>(declaredFields.size());
        ExcelCell excelCell;
        ExcelMappedEntity excelMappedEntity;
        boolean searchExcelId = false;
        int excelIdCount = 0;
        for (Field declaredField : declaredFields) {
            excelCell = declaredField.getAnnotation(ExcelCell.class);
            if (excelCell != null) {
                if (StringUtils.isEmpty(excelCell.value())) {
                    throw new ExcelCellHeaderTitleException("The value is empty at property [" + declaredField.getType() + "]");
                }
                if (titleTempList.contains(excelCell.value())) {
                    throw new ExcelCellHeaderTitleException("The value [" + excelCell.value() + "] is repetitive.");
                }
                titleTempList.add(excelCell.value());
                excelMappedEntity = declaredField.getAnnotation(ExcelMappedEntity.class);
                if (excelMappedEntity != null) {
                    if (declaredField.isAnnotationPresent(ExcelId.class)) {
                        throw new AnnotationConfigException("between @ExcelId and @ExcelMappedEntity config at the same property");
                    }
                    searchExcelId = true;
                    Class<?> targetClass = excelMappedEntity.targetClass();
                    this.validateJavaBean(targetClass);
                }
            }
        }
        if (searchExcelId) {
            for (Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(ExcelId.class)) {
                    excelIdCount++;
                }
            }
            if (excelIdCount == 0) {
                throw new AnnotationConfigException(String.format("The class [%s] must config annotation [%s]", beanClass.getName(), ExcelId.class.getName()));
            }
            if (excelIdCount > 1) {
                throw new AnnotationConfigException(String.format("The one property of class [%s] can config @ExcelId", beanClass.getName()));
            }
        }
    }
}
