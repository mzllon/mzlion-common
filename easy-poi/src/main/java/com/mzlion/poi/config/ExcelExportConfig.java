package com.mzlion.poi.config;

import com.mzlion.poi.constant.ExcelVersion;

/**
 * Created by mzlion on 2016/6/7.
 */
public class ExcelExportConfig {

    private String title;

    private String secondTitle;

    private ExcelVersion excelVersion;

    private double titleHeight = 10;

    private double secondTitleHeight = 8;

    private String sheetName;

    private boolean headerRowCreate;

    private ExcelExportConfig(Builder builder) {
        this.title = builder.title;
        this.secondTitle = builder.secondTitle;
        this.excelVersion = builder.excelVersion;
        this.titleHeight = builder.titleHeight;
        this.secondTitleHeight = builder.secondTitleHeight;
        this.sheetName = builder.sheetName;
        this.headerRowCreate = builder.headerRowCreate;
    }

    public String getTitle() {
        return title;
    }

    public String getSecondTitle() {
        return secondTitle;
    }

    public ExcelVersion getExcelVersion() {
        return excelVersion;
    }

    public double getTitleHeight() {
        return titleHeight;
    }

    public double getSecondTitleHeight() {
        return secondTitleHeight;
    }

    public String getSheetName() {
        return sheetName;
    }

    public boolean isHeaderRowCreate() {
        return headerRowCreate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExcelExportConfig{");
        sb.append("title='").append(title).append('\'');
        sb.append(", secondTitle='").append(secondTitle).append('\'');
        sb.append(", excelVersion=").append(excelVersion);
        sb.append(", titleHeight=").append(titleHeight);
        sb.append(", secondTitleHeight=").append(secondTitleHeight);
        sb.append(", sheetName='").append(sheetName).append('\'');
        sb.append(", headerRowCreate=").append(headerRowCreate);
        sb.append('}');
        return sb.toString();
    }

    public static class Builder {

        private String title;

        private String secondTitle;

        private ExcelVersion excelVersion;

        private short titleHeight = 10;

        private short secondTitleHeight = 8;

        private String sheetName;

        private boolean headerRowCreate;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder secondTitle(String secondTitle) {
            this.secondTitle = secondTitle;
            return this;
        }

        public Builder excelVersion(ExcelVersion excelVersion) {
            this.excelVersion = excelVersion;
            return this;
        }

        public Builder titleHeight(short titleHeight) {
            this.titleHeight = titleHeight;
            return this;
        }

        public Builder secondTitleHeight(short secondTitleHeight) {
            this.secondTitleHeight = secondTitleHeight;
            return this;
        }

        public Builder sheetName(String sheetName) {
            this.sheetName = sheetName;
            return this;
        }

        public Builder headerRowCreate(boolean headerRowCreate) {
            this.headerRowCreate = headerRowCreate;
            return this;
        }

        public ExcelExportConfig build() {
            return new ExcelExportConfig(this);
        }
    }

}
