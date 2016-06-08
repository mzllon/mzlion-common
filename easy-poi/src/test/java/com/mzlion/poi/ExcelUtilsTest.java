package com.mzlion.poi;

import com.mzlion.poi.config.ExcelImportConfig;
import com.mzlion.poi.entity.Employee;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * Created by mzlion on 2016/6/8.
 */
public class ExcelUtilsTest {
    @Test
    public void load() throws Exception {

    }

    @Test
    public void load1() throws Exception {

    }

    @Test
    public void load2() throws Exception {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("imports/studentList.xlsx");
        List<Employee> employeeList = ExcelUtils.load(inputStream, Employee.class, new ExcelImportConfig.Builder().strict(true));
        for (Employee employee : employeeList) {
            System.out.println(employee);
        }
    }

    @Test
    public void load3() throws Exception {

    }

}