package com.mzlion.poi.entity;

import com.mzlion.poi.annotation.ExcelCell;
import com.mzlion.poi.constant.ExcelCellType;

import java.util.Date;

/**
 * 员工信息
 *
 * @author mzlion
 * @version V1.0
 */
public class Employee {
    /**
     * 工号
     */
    @ExcelCell(value = "工号", required = true)
    private String no;

    /**
     * 姓名
     */
    @ExcelCell(value = "姓名", height = 20, width = 30, required = true, order = 2)
    private String name;

    /**
     * 性别
     */
    @ExcelCell("性别")
    private String sex;

    @ExcelCell(value = "出生日期", width = 20, type = ExcelCellType.DATE)
    private Date birthDay;

    @ExcelCell(value = "入职日期", type = ExcelCellType.DATE, order = 4, excelDateFormat = "yyyy-MM-dd")
    private Date regDate;

    @ExcelCell("家庭住址")
    private String address;

    @ExcelCell("联系电话")
    private String mobile;

    /**
     * 转正日期
     */
    @ExcelCell(value = "转正日期", javaDateFormat = "yyyyMMdd")
    private String obtainedDate;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getObtainedDate() {
        return obtainedDate;
    }

    public void setObtainedDate(String obtainedDate) {
        this.obtainedDate = obtainedDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Employee{");
        sb.append("no='").append(no).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", sex='").append(sex).append('\'');
        sb.append(", birthDay=").append(birthDay);
        sb.append(", regDate=").append(regDate);
        sb.append(", address='").append(address).append('\'');
        sb.append(", mobile='").append(mobile).append('\'');
        sb.append(", obtainedDate='").append(obtainedDate).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
