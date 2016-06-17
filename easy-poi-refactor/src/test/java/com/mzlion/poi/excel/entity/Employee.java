package com.mzlion.poi.excel.entity;

import com.mzlion.poi.annotation.ExcelCell;
import com.mzlion.poi.annotation.ExcelEntity;
import com.mzlion.poi.annotation.ExcelId;
import com.mzlion.poi.annotation.ExcelMappedEntity;
import com.mzlion.poi.constant.ExcelCellType;

import java.util.List;

/**
 * Created by mzlion on 2016/6/15.
 */
@ExcelEntity
public class Employee {

    @ExcelId
    @ExcelCell(value = "员工号", excelCellType = ExcelCellType.TEXT)
    private String empNo;

    @ExcelCell("员工姓名")
    private String empName;

    @ExcelCell("工资卡列表")
    @ExcelMappedEntity(propertyNames = {"cardNo", "cardType", "employee"}, targetClass = DebitCard.class)
    private List<DebitCard> debitCardList;

    @ExcelCell("职称列表")
    @ExcelMappedEntity(propertyNames = {"name", "createTime"}, targetClass = Position.class)
    private List<Position> positionList;

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public List<DebitCard> getDebitCardList() {
        return debitCardList;
    }

    public void setDebitCardList(List<DebitCard> debitCardList) {
        this.debitCardList = debitCardList;
    }

    public List<Position> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<Position> positionList) {
        this.positionList = positionList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Employee{");
        sb.append("empNo='").append(empNo).append('\'');
        sb.append(", empName='").append(empName).append('\'');
        sb.append(", debitCardList=").append(debitCardList);
        sb.append(", positionList=").append(positionList);
        sb.append('}');
        return sb.toString();
    }
}
