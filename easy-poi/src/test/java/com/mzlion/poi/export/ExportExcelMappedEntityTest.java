package com.mzlion.poi.export;

import com.mzlion.core.date.DateUtils;
import com.mzlion.poi.ExcelUtils;
import com.mzlion.poi.config.ExcelWriteConfig;
import com.mzlion.poi.entity.Account;
import com.mzlion.poi.entity.EmployeeMapped;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by mzlion on 2016/6/13.
 */
public class ExportExcelMappedEntityTest {
    private static List<EmployeeMapped> employeeMappedList;

    @BeforeClass
    public static void setup() throws Exception {
        employeeMappedList = new ArrayList<>();
        EmployeeMapped employeeMapped = new EmployeeMapped("1001", "安德鲁", "男",
                DateUtils.parseDate("19900202", "yyyyMMdd"), DateUtils.parseDate("20160510", "yyyyMMdd"),
                "这是一个地址", "18321884705", null, Arrays.asList(
                new Account("A001", "公积金账户", "10000.0"),
                new Account("S010", "社保账户", "369.02")
        ));

        employeeMappedList.add(new EmployeeMapped(
                "1002",
                "华胥引",
                "男",
                DateUtils.parseDate("19900322", "yyyyMMdd"),
                DateUtils.parseDate("19920322", "yyyyMMdd"),
                "华山路湖南路交叉口",
                "15567091310",
                "20160903",
                Arrays.asList(
                        new Account("A002", "公积金账户", "12000.0"),
                        new Account("S009", "社保账户", "355.2")
                )
        ));
        employeeMappedList.add(employeeMapped);
    }

    @Test
    public void write() throws Exception {
        URL resource = this.getClass().getClassLoader().getResource(".");
        String resourceFile = resource.getFile();
        File output = new File(resourceFile, "export/employee_map_entity.xlsx");
        output.delete();

        ExcelWriteConfig excelWriteConfig = new ExcelWriteConfig.Builder()
                .beanClass(EmployeeMapped.class)
                .title("员工及其账户列表")
                .build();
        ExcelUtils.write(employeeMappedList, excelWriteConfig, output);
        assertTrue(output.exists());
    }

}
