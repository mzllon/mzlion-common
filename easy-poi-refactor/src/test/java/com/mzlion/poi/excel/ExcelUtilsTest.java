package com.mzlion.poi.excel;

import com.mzlion.poi.config.ExcelCellHeaderConfig;
import com.mzlion.poi.config.ReadExcelConfig;
import com.mzlion.poi.excel.entity.Employee;
import com.mzlion.poi.excel.entity.EmployeeSimply;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * Created by mzlion on 2016/6/16.
 */
public class ExcelUtilsTest {

    @Test
    public void testRead2Map() throws Exception {
        ReadExcelConfig readExcelConfig = new ReadExcelConfig.Builder()
                .rawClass(Map.class)
                .excelCellHeaderConfig(new ExcelCellHeaderConfig.Builder().title("工号").propertyName("empNo").build())
                .excelCellHeaderConfig(new ExcelCellHeaderConfig.Builder().title("姓名").propertyName("name").build())
                .excelCellHeaderConfig(new ExcelCellHeaderConfig.Builder().title("出生日期").propertyName("birthDay").build())
                .excelCellHeaderConfig(new ExcelCellHeaderConfig.Builder().title("性别").propertyName("sex").build())
                .excelCellHeaderConfig(new ExcelCellHeaderConfig.Builder().title("入职日期").propertyName("regDate").build())
                .excelCellHeaderConfig(new ExcelCellHeaderConfig.Builder().title("转正日期").propertyName("obtainedDate").build())
                .excelCellHeaderConfig(new ExcelCellHeaderConfig.Builder().title("联系电话").propertyName("mobile").build())
                .excelCellHeaderConfig(new ExcelCellHeaderConfig.Builder().title("家庭住址").propertyName("address").build())
                .build();
        List<Map> mapList = ExcelUtils.read(this.getClass().getClassLoader().getResourceAsStream("employeeList.xlsx"), readExcelConfig);
        for (Map map : mapList) {
            System.out.println(map);
        }
    }

    @Test
    public void testRead2BeanSimply() throws Exception {
        List<EmployeeSimply> employeeSimplyList = ExcelUtils.read(this.getClass().getClassLoader().getResourceAsStream("employeeList.xlsx"), EmployeeSimply.class);
        for (EmployeeSimply employeeSimply : employeeSimplyList) {
            System.out.println(employeeSimply);
        }
    }

    @Test
    public void testRead2BeanComplex() throws Exception {
        ReadExcelConfig readExcelConfig = new ReadExcelConfig.Builder()
                .rawClass(Employee.class)
                .headerRowStart(2)
                .headerRowUsed(2)
                .build();
        ReadExcelEngine<Employee> readExcelEngine = new ReadExcelEngine<>(readExcelConfig);
        List<Employee> employeeList = readExcelEngine.read(this.getClass().getClassLoader().getResourceAsStream("员工信息.xlsx"));
        assertTrue(employeeList != null);
        for (Employee employee : employeeList) {
            System.out.println(employee);
        }
    }

    @Test
    public void testWrite() throws Exception {
        File output = new File("d:/emp.xlsx");
        ExcelUtils.write(this.genEmployeeList(), "测试导出", EmployeeSimply.class, output);
    }

    @Test
    public void testWrite2BeanComplex() throws Exception {
        File output = new File("d:/emp.xlsx");
        Employee employee = new Employee();

        ExcelUtils.write(Arrays.asList(employee), "测试导出复合型标题", Employee.class, output);
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