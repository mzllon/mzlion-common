package com.mzlion.poi.annotation;

import com.mzlion.core.reflect.StaticFieldFilter;
import com.mzlion.core.util.ReflectionUtils;
import com.mzlion.poi.entity.EmployeeSimply;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by mzlion on 2016/6/7.
 */
public class ExcelCellTest {

    @Test
    public void testGen() throws Exception {
        List<Field> declaredFields = ReflectionUtils.getDeclaredFields(EmployeeSimply.class);
        declaredFields = ReflectionUtils.filter(declaredFields, new StaticFieldFilter());
        for (Field declaredField : declaredFields) {
            System.out.println(declaredField);
            ExcelCell excelCell = declaredField.getAnnotation(ExcelCell.class);
            if (excelCell != null) {
                System.out.println("title=" + excelCell.value() + ",width=" + excelCell.width() + ",autoWrap=" +
                        excelCell.autoWrap() + ",order=" + excelCell.order() + ",required=" + excelCell.required() +
                        ",type=" + excelCell.type());
            }
        }
    }
}
