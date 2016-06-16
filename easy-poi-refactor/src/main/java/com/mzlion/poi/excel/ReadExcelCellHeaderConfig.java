package com.mzlion.poi.excel;

import com.mzlion.poi.config.ExcelCellHeaderConfig;
import com.mzlion.poi.constant.ExcelCellType;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 * Created by mzlion on 2016/6/16.
 */
class ReadExcelCellHeaderConfig {

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
     * cell对应JavaBean属性名
     */
    String propertyName;

    boolean isExcelId;

    CellRangeAddress cellRangeAddress;

    List<ReadExcelCellHeaderConfig> children;

    ReadExcelCellHeaderConfig() {
    }

    ReadExcelCellHeaderConfig(ExcelCellHeaderConfig excelCellHeaderConfig) {
        this.title = excelCellHeaderConfig.getTitle();
        this.excelCellType = excelCellHeaderConfig.getExcelCellType();
        this.propertyName = excelCellHeaderConfig.getPropertyName();
        this.cellIndex = excelCellHeaderConfig.getCellIndex();
        this.excelDateFormat = excelCellHeaderConfig.getExcelDateFormat();
        this.javaDateFormat = excelCellHeaderConfig.getJavaDateFormat();
    }

    public ReadExcelCellHeaderConfig(ReadExcelCellHeaderConfig parent) {
        this.title = parent.title;
        this.excelCellType = parent.excelCellType;
        this.cellIndex = parent.cellIndex;
        this.excelDateFormat = parent.excelDateFormat;
        this.javaDateFormat = parent.javaDateFormat;
        this.propertyName = parent.propertyName;
        this.isExcelId = parent.isExcelId;
        this.cellRangeAddress = parent.cellRangeAddress;
        this.children = parent.children;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ReadExcelCellHeaderConfig{");
        sb.append("title='").append(title).append('\'');
        sb.append(", excelCellType=").append(excelCellType);
        sb.append(", cellIndex=").append(cellIndex);
        sb.append(", excelDateFormat='").append(excelDateFormat).append('\'');
        sb.append(", javaDateFormat='").append(javaDateFormat).append('\'');
        sb.append(", propertyName='").append(propertyName).append('\'');
        sb.append(", isExcelId=").append(isExcelId);
        sb.append(", cellRangeAddress=").append(cellRangeAddress);
        sb.append(", children=").append(children);
        sb.append('}');
        return sb.toString();
    }
}
