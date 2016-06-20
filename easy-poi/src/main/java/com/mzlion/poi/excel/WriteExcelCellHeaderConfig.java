package com.mzlion.poi.excel;

import com.mzlion.poi.config.ExcelCellHeaderConfig;
import com.mzlion.poi.constant.ExcelCellType;

import java.util.List;

/**
 * Created by mzlion on 2016/6/17.
 */
class WriteExcelCellHeaderConfig implements Comparable<WriteExcelCellHeaderConfig> {


    /**
     * cell标题,必须唯一
     */
    String title;

    /**
     * cell类型
     */
    ExcelCellType excelCellType;

    /**
     * 所处Excel的cell位置
     */
    int cellIndex;

    /**
     * Excel的日期格式化风格
     */
    String excelDateFormat;

    /**
     * Java中的日期格式化风格
     */
    String javaDateFormat;

    /**
     * cell的宽度
     */
    float width;

    /**
     * 自动换行
     */
    boolean autoWrap;

    /**
     * cell对应JavaBean属性名
     */
    String propertyName;

    List<WriteExcelCellHeaderConfig> children;

    Class<?> targetClass;

    WriteExcelCellHeaderConfig() {
    }

    WriteExcelCellHeaderConfig(ExcelCellHeaderConfig excelCellHeaderConfig) {
        this.title = excelCellHeaderConfig.getTitle();
        this.excelCellType = excelCellHeaderConfig.getExcelCellType();
        this.cellIndex = excelCellHeaderConfig.getCellIndex();
        this.excelDateFormat = excelCellHeaderConfig.getExcelDateFormat();
        this.javaDateFormat = excelCellHeaderConfig.getJavaDateFormat();
        this.width = excelCellHeaderConfig.getWidth();
        this.autoWrap = excelCellHeaderConfig.isAutoWrap();
        this.propertyName = excelCellHeaderConfig.getPropertyName();
    }

    @Override
    public int compareTo(WriteExcelCellHeaderConfig o) {
        return this.cellIndex = o.cellIndex;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WriteExcelCellHeaderConfig{");
        sb.append("title='").append(title).append('\'');
        sb.append(", excelCellType=").append(excelCellType);
        sb.append(", cellIndex=").append(cellIndex);
        sb.append(", excelDateFormat='").append(excelDateFormat).append('\'');
        sb.append(", javaDateFormat='").append(javaDateFormat).append('\'');
        sb.append(", width=").append(width);
        sb.append(", autoWrap=").append(autoWrap);
        sb.append(", propertyName='").append(propertyName).append('\'');
        sb.append(", children=").append(children);
        sb.append(", targetClass=").append(targetClass);
        sb.append('}');
        return sb.toString();
    }

    ExcelCellHeaderConfig convertToExcelCellHeaderConfig() {
        return new ExcelCellHeaderConfig.Builder()
                .title(this.title).excelCellType(this.excelCellType).cellIndex(this.cellIndex)
                .excelDateFormat(this.excelDateFormat).javaDateFormat(this.javaDateFormat)
                .width(this.width).autoWrap(this.autoWrap).propertyName(this.propertyName).build();
    }
}
