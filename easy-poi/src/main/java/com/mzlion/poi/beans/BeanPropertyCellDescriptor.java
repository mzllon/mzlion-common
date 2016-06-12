package com.mzlion.poi.beans;

import com.mzlion.poi.constant.ExcelCellType;

/**
 * Created by mzlion on 2016/6/8.
 */
public class BeanPropertyCellDescriptor implements Comparable<BeanPropertyCellDescriptor> {

    /**
     * cell标题
     */
    private String title;

    /**
     * cell列是否必须
     */
    private boolean required;

    /**
     * cell类型
     */
    private ExcelCellType type;

    /**
     * cell对应JavaBean属性名
     */
    private String propertyName;

    /**
     * 所处Excel的cell位置
     */
    private int cellIndex;

    /**
     * Excel的日期格式化风格
     */
    private String excelDateFormat;

    /**
     * Java中的日期格式化风格
     */
    private String javaDateFormat;

    /**
     * cell的宽度
     */
    private float width;

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

    public int getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    public String getExcelDateFormat() {
        return excelDateFormat;
    }

    public void setExcelDateFormat(String excelDateFormat) {
        this.excelDateFormat = excelDateFormat;
    }

    public String getJavaDateFormat() {
        return javaDateFormat;
    }

    public void setJavaDateFormat(String javaDateFormat) {
        this.javaDateFormat = javaDateFormat;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BeanPropertyCellDescriptor{");
        sb.append("title='").append(title).append('\'');
        sb.append(", required=").append(required);
        sb.append(", type=").append(type);
        sb.append(", propertyName='").append(propertyName).append('\'');
        sb.append(", cellIndex=").append(cellIndex);
        sb.append(", excelDateFormat='").append(excelDateFormat).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(BeanPropertyCellDescriptor o) {
        return this.cellIndex - o.cellIndex;
    }
}
