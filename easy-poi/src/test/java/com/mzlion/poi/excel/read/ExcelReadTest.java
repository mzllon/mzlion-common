package com.mzlion.poi.excel.read;

import com.mzlion.poi.config.ExcelReadConfig;
import com.mzlion.poi.excel.ExcelUtils;
import com.mzlion.poi.excel.entity.Employee;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by mzlion on 2016/6/15.
 */
public class ExcelReadTest {

    @Test
    public void readExcel() throws Exception {
        File file = new File("d:/员工信息.xlsx");
        ExcelReadConfig excelReadConfig = new ExcelReadConfig.Builder()
                .beanClass(Employee.class)
                .headerRowStart(0)
                .headerRowUsed(2)
                .build();
        List<Employee> employeeList = ExcelUtils.read(file, excelReadConfig);
        for (Employee employee : employeeList) {
            System.out.println(employee);
        }
    }
}
