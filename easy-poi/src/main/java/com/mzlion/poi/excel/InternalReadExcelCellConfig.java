package com.mzlion.poi.excel;

import com.mzlion.poi.constant.ExcelCellType;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 * Created by mzlion on 2016/6/16.
 */
class InternalReadExcelCellConfig {

    /**
     * cell类型
     */
    ExcelCellType type;

    /**
     * cell对应JavaBean属性名
     */
    String propertyName;

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

    List<InternalReadExcelCellConfig> children;

    CellRangeAddress cellRangeAddress;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InternalReadExcelCellConfig{");
        sb.append("type=").append(type);
        sb.append(", propertyName='").append(propertyName).append('\'');
        sb.append(", cellIndex=").append(cellIndex);
        sb.append(", excelDateFormat='").append(excelDateFormat).append('\'');
        sb.append(", javaDateFormat='").append(javaDateFormat).append('\'');
        sb.append(", children=").append(children);
        sb.append(", cellRangeAddress=").append(cellRangeAddress);
        sb.append('}');
        return sb.toString();
    }
}
