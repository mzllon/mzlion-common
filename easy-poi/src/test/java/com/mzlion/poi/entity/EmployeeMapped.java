package com.mzlion.poi.entity;

import com.mzlion.poi.annotation.ExcelCell;
import com.mzlion.poi.annotation.ExcelEntity;
import com.mzlion.poi.annotation.ExcelHyperLink;
import com.mzlion.poi.annotation.ExcelMappedEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by mzlion on 2016/6/13.
 */
@ExcelEntity
public class EmployeeMapped {

    @ExcelCell(value = "工号", required = true, width = 12)
    private String no;

    @ExcelCell(value = "姓名", required = true, order = 2)
    private String name;

    @ExcelCell("性别")
    private String sex;

    @ExcelCell(value = "出生日期", width = 10, excelDateFormat = "yyyy/MM/dd")
    private Date birthDay;

    @ExcelCell(value = "入职日期", order = 4, excelDateFormat = "yyyy-MM-dd")
    private Date regDate;

    @ExcelCell(value = "家庭住址", width = 30, autoWrap = true)
    private String address;

    @ExcelCell(value = "联系电话", width = 12)
    private String mobile;

    @ExcelCell(value = "转正日期", javaDateFormat = "yyyyMMdd", width = 12)
    @ExcelHyperLink
    private String obtainedDate;

    @ExcelCell(value = "账户列表")
    @ExcelMappedEntity(propertyNames = {"name", "balance"}, mappedBy = Object.class)
    private List<Account> accountList;

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

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public EmployeeMapped() {
    }

    public EmployeeMapped(String no, String name, String sex, Date birthDay, Date regDate, String address, String mobile,
                          String obtainedDate, List<Account> accountList) {
        this.no = no;
        this.name = name;
        this.sex = sex;
        this.birthDay = birthDay;
        this.regDate = regDate;
        this.address = address;
        this.mobile = mobile;
        this.obtainedDate = obtainedDate;
        this.accountList = accountList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EmployeeMapped{");
        sb.append("no='").append(no).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", sex='").append(sex).append('\'');
        sb.append(", birthDay=").append(birthDay);
        sb.append(", regDate=").append(regDate);
        sb.append(", address='").append(address).append('\'');
        sb.append(", mobile='").append(mobile).append('\'');
        sb.append(", obtainedDate='").append(obtainedDate).append('\'');
        sb.append(", accountList=").append(accountList);
        sb.append('}');
        return sb.toString();
    }
}
