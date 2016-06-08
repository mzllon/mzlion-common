package com.mzlion.poi.beans;

import com.mzlion.poi.constant.ExcelCellType;

/**
 * Created by mzlion on 2016/6/8.
 */
public class ExcelCellDescriptor implements Comparable<ExcelCellDescriptor> {

    private String title;

    private boolean required;

    private ExcelCellType type;

    private String propertyName;

    private Integer cellIndex;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public ExcelCellType getType() {
        return type;
    }

    public void setType(ExcelCellType type) {
        this.type = type;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Integer getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(Integer cellIndex) {
        this.cellIndex = cellIndex;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExcelCellDescriptor{");
        sb.append("title='").append(title).append('\'');
        sb.append(", required=").append(required);
        sb.append(", type=").append(type);
        sb.append(", propertyName='").append(propertyName).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(ExcelCellDescriptor o) {
        return this.cellIndex.compareTo(o.cellIndex);
    }
}
