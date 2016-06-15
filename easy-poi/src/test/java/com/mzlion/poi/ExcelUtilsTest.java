package com.mzlion.poi;

import com.mzlion.poi.config.ExcelWriteConfig;
import com.mzlion.poi.entity.EmployeeSimply;
import com.mzlion.poi.excel.ExcelUtils;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by mzlion on 2016/6/8.
 */
public class ExcelUtilsTest {

    @Test
    public void write() throws Exception {
        long start = System.currentTimeMillis();
        File output = new File("d:/emp.xlsx");
        ExcelUtils.write(this.genEmployeeList(), "测试导出", EmployeeSimply.class, output);
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void write1() throws Exception {
        long start = System.currentTimeMillis();
        File output = new File("d:/emp2.xlsx");
        ExcelWriteConfig.Builder builder = new ExcelWriteConfig.Builder();
        builder.title("测试导出标题")
                .beanClass(EmployeeSimply.class)
                .excelCellStyleClass(null);
        ExcelUtils.write(this.genEmployeeList(), builder.build(), output);
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void write2() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ExcelWriteConfig excelWriteConfig = new ExcelWriteConfig.Builder()
                .beanClass(Map.class)
                .build();
        ExcelUtils.write(this.genEmployeeList(), excelWriteConfig, baos);
    }

    @Test
    public void load() throws Exception {

    }

    @Test
    public void load1() throws Exception {

    }

    @Test
    public void load2() throws Exception {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("imports/employeeList.xlsx");
        long startTime = System.currentTimeMillis();
        List<EmployeeSimply> employeeSimplyList = ExcelUtils.read(inputStream, EmployeeSimply.class);
        long end = System.currentTimeMillis();
        System.out.println("Reading excel cost " + (end - startTime) + " milliseconds");
        assertNotNull(employeeSimplyList);
        assertEquals(100, employeeSimplyList.size());
        for (EmployeeSimply employeeSimply : employeeSimplyList) {
            System.out.println(employeeSimply);
        }
    }

    @Test
    public void load3() throws Exception {

    }

    private List<EmployeeSimply> genEmployeeList() {
        List<EmployeeSimply> list = new ArrayList<>();
        EmployeeSimply employeeSimply;

        employeeSimply = new EmployeeSimply();
        employeeSimply.setNo("091231301");
        employeeSimply.setName("施鸿朗");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1990);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 19);
        employeeSimply.setBirthDay(calendar.getTime());
        employeeSimply.setSex("男");
        employeeSimply.setObtainedDate("2016-09-05");
        employeeSimply.setMobile("17018321652");
        employeeSimply.setAddress("上海市长宁区倏肮路 18号 彻小区 34号楼 1单元 406室");
        list.add(employeeSimply);

        employeeSimply = new EmployeeSimply();
        employeeSimply.setNo("091231302");
        employeeSimply.setName("袁高谊");
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1990);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 6);
        employeeSimply.setBirthDay(calendar.getTime());
        employeeSimply.setSex("男");
        employeeSimply.setObtainedDate("2016-09-05");
        employeeSimply.setMobile("17018321653");
        employeeSimply.setAddress("上海市长宁区月押路 04号 约小区 25号楼 9单元 629室\n");
        list.add(employeeSimply);

        employeeSimply = new EmployeeSimply();
        employeeSimply.setNo("091231303\n");
        employeeSimply.setName("老芳洲\n");
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1990);
        calendar.set(Calendar.MONTH, 3);
        calendar.set(Calendar.DATE, 19);
        employeeSimply.setBirthDay(calendar.getTime());
        employeeSimply.setSex("女\n");
        employeeSimply.setObtainedDate("2016-09-05");
        employeeSimply.setMobile("17018321654\n");
        employeeSimply.setAddress("上海市长宁区敛焙路 08号 航小区 60号楼 8单元 223室\n");
        list.add(employeeSimply);

        employeeSimply = new EmployeeSimply();
        employeeSimply.setNo("091231304");
        employeeSimply.setName("覃维运\n");
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1990);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 0);
        employeeSimply.setBirthDay(calendar.getTime());
        employeeSimply.setSex("男");
        employeeSimply.setObtainedDate("2016-09-05");
        employeeSimply.setMobile("17018321655");
        employeeSimply.setAddress("上海市长宁区缓钥路 15号 厥小区 47号楼 5单元 811室");
        list.add(employeeSimply);
        return list;
    }
}