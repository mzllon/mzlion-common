package com.mzlion.poi.entity;

import com.mzlion.poi.annotation.ExcelCell;
import com.mzlion.poi.constant.ExcelCellType;

import java.util.Date;

/**
 * @author mzlion
 * @version V1.0
 */
public class StudentEntity implements java.io.Serializable {
    /**
     * id
     */
    private String id;

    /**
     * 学生姓名
     */
    @ExcelCell(title = "学生姓名", height = 20, width = 30, required = true)
    private String name;

    /**
     * 学生性别
     */
    @ExcelCell(title = "学生性别")
    private int sex;

    @ExcelCell(title = "出生日期", width = 20, type = ExcelCellType.DATE)
    private Date birthday;

    @ExcelCell(title = "进校日期", type = ExcelCellType.DATE, order = 2)
    private Date registrationDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}
