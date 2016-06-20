package com.mzlion.poi.excel;

import com.mzlion.poi.config.ExcelCellHeaderConfig;
import com.mzlion.poi.config.ReadExcelConfig;
import com.mzlion.poi.entity.DebitCard;
import com.mzlion.poi.entity.Employee;
import com.mzlion.poi.entity.EmployeeSimply;
import com.mzlion.poi.entity.Position;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

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
        List<Map> employeeList = ExcelUtils.read(this.getClass().getClassLoader()
                .getResourceAsStream("imports/employeeList.xlsx"), readExcelConfig);
        assertNotNull(employeeList);
        assertEquals(100, employeeList.size());
    }

    @Test
    public void testRead2BeanSimply() throws Exception {
        List<EmployeeSimply> employeeSimplyList = ExcelUtils.read(this.getClass().getClassLoader()
                .getResourceAsStream("imports/employeeList.xlsx"), EmployeeSimply.class);
        assertNotNull(employeeSimplyList);
        assertEquals(100, employeeSimplyList.size());
        //091231301	施鸿朗	1990/1/20	男	2016-09-05	2016/11/6	17018321652	上海市长宁区倏肮路 18号 彻小区 34号楼 1单元 406室
//        EmployeeSimply employeeSimply = new EmployeeSimply();
//        employeeSimply.setNo("091231301");
//        employeeSimply.setName("施鸿朗");
//        employeeSimply.setBirthDay(DateUtils.parseDate("1990/1/20", "yyyy/M/dd"));
//        employeeSimply.setSex("男");
//        employeeSimply.setRegDate(DateUtils.parseDate("2016-09-05", "yyyy-MM-dd"));
//        employeeSimply.setObtainedDate("2016/11/6");
//        employeeSimply.setMobile("17018321652");
//        employeeSimply.setAddress("上海市长宁区倏肮路 18号 彻小区 34号楼 1单元 406室");
//        assertSame(employeeSimply, employeeSimplyList.get(0));
    }

    @Test
    public void testRead2BeanComplex() throws Exception {
        ReadExcelConfig readExcelConfig = new ReadExcelConfig.Builder()
                .rawClass(Employee.class)
                .headerRowStart(2)
                .headerRowUsed(2)
                .build();
        ReadExcelEngine<Employee> readExcelEngine = new ReadExcelEngine<>(readExcelConfig);
        List<Employee> employeeList = readExcelEngine.read(this.getClass().getClassLoader().getResourceAsStream("imports/complexEmployList.xlsx"));
        assertTrue(employeeList != null);
        assertEquals(3, employeeList.size());
        for (Employee employee : employeeList) {
            System.out.println(employee);
        }
    }

    @Test
    public void testWrite() throws Exception {
        File output = new File("d:/emp.xlsx");
        ExcelUtils.write(this.genEmployeeSimplyList(), "测试导出", EmployeeSimply.class, output);
    }

    @Test
    public void testWrite2BeanComplex() throws Exception {
        File output = new File("d:/emp.xlsx");
        ExcelUtils.write(this.getEmployeeList(), "公司员工信息列表", Employee.class, output);
    }

    private List<Employee> getEmployeeList() {
        List<Employee> employeeList = new ArrayList<>();
        Employee employee = new Employee();
        employee.setEmpNo("1001");
        employee.setEmpName("张三");
        List<DebitCard> debitCardList = new ArrayList<>();
        DebitCard debitCard = new DebitCard();
        debitCard.setCardId("d01");
        debitCard.setCardNo("6228480402564890000");
        debitCard.setCardType("招商银行");
        debitCardList.add(debitCard);
        employee.setDebitCardList(debitCardList);
        employeeList.add(employee);

        employee = new Employee();
        employee.setEmpNo("1002");
        employee.setEmpName("李四");
        debitCardList = new ArrayList<>();
        debitCard = new DebitCard();
        debitCard.setCardId("d02");
        debitCard.setCardNo("6228480402564890001");
        debitCard.setCardType("招商银行");
        debitCardList.add(debitCard);
        debitCard = new DebitCard();
        debitCard.setCardId("d03");
        debitCard.setCardNo("6227480402564890000");
        debitCard.setCardType("建设银行");
        debitCardList.add(debitCard);
        employee.setDebitCardList(debitCardList);
        List<Position> positionList = new ArrayList<>();
        Position position = new Position();
        position.setId("p01");
        position.setName("高级项目经理");
        position.setCreateTime("2015/9/6");
        positionList.add(position);
        employee.setPositionList(positionList);
        employeeList.add(employee);

        employee = new Employee();
        employee.setEmpNo("1003");
        employee.setEmpName("小明");
        debitCardList = new ArrayList<>();
        debitCard = new DebitCard();
        debitCard.setCardId("d04");
        debitCard.setCardNo("6228480402564890002");
        debitCard.setCardType("招商银行");
        debitCardList.add(debitCard);
        debitCard = new DebitCard();
        debitCard.setCardId("d05");
        debitCard.setCardNo("6227480402564890001");
        debitCard.setCardType("建设银行");
        debitCardList.add(debitCard);
        debitCard = new DebitCard();
        debitCard.setCardId("d065");
        debitCard.setCardNo("6227480402564890002");
        debitCard.setCardType("浦发银行");
        debitCardList.add(debitCard);
        employee.setDebitCardList(debitCardList);
        positionList = new ArrayList<>();
        position = new Position();
        position.setId("p02");
        position.setName("项目总监");
        position.setCreateTime("2015/9/6");
        positionList.add(position);
        position = new Position();
        position.setId("p03");
        position.setName("技术顾问");
        position.setCreateTime("2015/2/1");
        positionList.add(position);
        employee.setPositionList(positionList);
        employeeList.add(employee);

        return employeeList;
    }

    private List<EmployeeSimply> genEmployeeSimplyList() {
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