package com.mzlion.poi;

import com.mzlion.poi.beans.PropertyCellMapping;
import com.mzlion.poi.config.ExcelWriteConfig;
import com.mzlion.poi.entity.Employee;
import org.junit.Test;

import java.util.List;

/**
 * Created by mzlion on 2016/6/13.
 */
public class ExcelMappedEntityLockTest {

    @Test
    public void test() throws Exception {
        ExcelWriteConfig excelWriteConfig = new ExcelWriteConfig.Builder()
                .beanClass(Employee.class)
                .build();
        List<PropertyCellMapping> propertyCellMappingList = excelWriteConfig.getPropertyCellMappingList();
        for (PropertyCellMapping propertyCellMapping : propertyCellMappingList) {
            System.out.println(propertyCellMapping);
        }
    }
}
