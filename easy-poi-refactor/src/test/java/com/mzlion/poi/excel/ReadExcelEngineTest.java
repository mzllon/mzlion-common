package com.mzlion.poi.excel;

import com.mzlion.poi.config.ReadExcelConfig;
import com.mzlion.poi.excel.entity.Employee;
import org.junit.Test;

import java.util.List;

/**
 * Created by mzlion on 2016/6/16.
 */
public class ReadExcelEngineTest {

    @Test
    public void test() throws Exception {
        ReadExcelConfig readExcelConfig = new ReadExcelConfig.Builder()
                .rawClass(Employee.class)
                .headerRowStart(2)
                .headerRowUsed(2)
                .build();
        ReadExcelEngine<Employee> readExcelEngine = new ReadExcelEngine<>(readExcelConfig);
        List<Employee> employeeList = readExcelEngine.read(this.getClass().getClassLoader().getResourceAsStream("员工信息.xlsx"));

    }

}