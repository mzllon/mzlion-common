package com.mzlion.poi.beans;

import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Created by mzlion on 2016/6/13.
 */
public class ExcelMerge {

    private int startRowIndex;

    private int endRowIndex;

    private int startColIndex;

    private int endColIndex;


    public int getStartRowIndex() {
        return startRowIndex;
    }

    public void setStartRowIndex(int startRowIndex) {
        this.startRowIndex = startRowIndex;
    }

    public int getEndRowIndex() {
        return endRowIndex;
    }

    public void setEndRowIndex(int endRowIndex) {
        this.endRowIndex = endRowIndex;
    }

    public int getStartColIndex() {
        return startColIndex;
    }

    public void setStartColIndex(int startColIndex) {
        this.startColIndex = startColIndex;
    }

    public int getEndColIndex() {
        return endColIndex;
    }

    public void setEndColIndex(int endColIndex) {
        this.endColIndex = endColIndex;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExcelMerge{");
        sb.append("startRowIndex=").append(startRowIndex);
        sb.append(", endRowIndex=").append(endRowIndex);
        sb.append(", startColIndex=").append(startColIndex);
        sb.append(", endColIndex=").append(endColIndex);
        sb.append('}');
        return sb.toString();
    }

    public CellRangeAddress convert() {
        return new CellRangeAddress(startRowIndex, endRowIndex, startColIndex, endColIndex);
    }
}
