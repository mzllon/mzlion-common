package com.mzlion.poi.config;

import com.mzlion.poi.constant.ExcelType;
import com.mzlion.poi.excel.write.ExcelCellStyle;

/**
 * Created by mzlion on 2016/6/7.
 */
public class ExcelWriteConfig<E> {

    /**
     * Excel标题
     */
    private String title;

    /**
     * Excel副标题
     */
    private String secondTitle;
    /**
     *
     */
    private ExcelType excelType;

    private float titleRowHeight;

    private float secondTitleRowHeight;

    private float dataRowHeight;

    private String sheetName;

    private boolean headerRowCreate;

    private Class<E> beanClass;

    private Class<? extends ExcelCellStyle> excelCellStyleClass;

    private ExcelWriteConfig(Builder<E> builder) {
        this.title = builder.title;
        this.secondTitle = builder.secondTitle;
        this.excelType = builder.excelType;
        this.titleRowHeight = builder.titleRowHeight;
        this.secondTitleRowHeight = builder.secondTitleRowHeight;
        this.dataRowHeight = builder.dataRowHeight;
        this.sheetName = builder.sheetName;
        this.headerRowCreate = builder.headerRowCreate;
        this.beanClass = builder.beanClass;
        this.excelCellStyleClass = builder.excelCellStyleClass;
    }

    public String getTitle() {
        return title;
    }

    public String getSecondTitle() {
        return secondTitle;
    }

    public ExcelType getExcelType() {
        return excelType;
    }

    public float getTitleRowHeight() {
        return titleRowHeight;
    }

    public float getSecondTitleRowHeight() {
        return secondTitleRowHeight;
    }

    public float getDataRowHeight() {
        return dataRowHeight;
    }

    public String getSheetName() {
        return sheetName;
    }

    public boolean isHeaderRowCreate() {
        return headerRowCreate;
    }

    public Class<E> getBeanClass() {
        return beanClass;
    }

    public Class<? extends ExcelCellStyle> getExcelCellStyleClass() {
        return excelCellStyleClass;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExcelWriteConfig{");
        sb.append("title='").append(title).append('\'');
        sb.append(", secondTitle='").append(secondTitle).append('\'');
        sb.append(", excelType=").append(excelType);
        sb.append(", titleRowHeight=").append(titleRowHeight);
        sb.append(", secondTitleRowHeight=").append(secondTitleRowHeight);
        sb.append(", dataRowHeight=").append(dataRowHeight);
        sb.append(", sheetName='").append(sheetName).append('\'');
        sb.append(", headerRowCreate=").append(headerRowCreate);
        sb.append(", beanClass=").append(beanClass);
        sb.append(", excelCellStyleClass=").append(excelCellStyleClass);
        sb.append('}');
        return sb.toString();
    }

    public static class Builder<E> {

        private String title;

        private String secondTitle;

        private ExcelType excelType;

        private float titleRowHeight;

        private float secondTitleRowHeight;

        private float dataRowHeight;

        private String sheetName;

        private boolean headerRowCreate;

        private Class<E> beanClass;

        private Class<? extends ExcelCellStyle> excelCellStyleClass;

        public Builder<E> title(String title) {
            this.title = title;
            return this;
        }

        public Builder<E> secondTitle(String secondTitle) {
            this.secondTitle = secondTitle;
            return this;
        }

        public Builder<E> excelType(ExcelType excelType) {
            this.excelType = excelType;
            return this;
        }

        public Builder<E> titleRowHeight(float titleRowHeight) {
            this.titleRowHeight = titleRowHeight;
            return this;
        }

        public Builder<E> secondTitleRowHeight(float secondTitleRowHeight) {
            this.secondTitleRowHeight = secondTitleRowHeight;
            return this;
        }

        public Builder<E> dataRowHeight(float dataRowHeight) {
            this.dataRowHeight = dataRowHeight;
            return this;
        }

        public Builder<E> sheetName(String sheetName) {
            this.sheetName = sheetName;
            return this;
        }

        public Builder<E> headerRowCreate(boolean headerRowCreate) {
            this.headerRowCreate = headerRowCreate;
            return this;
        }

        public Builder<E> beanClass(Class<E> beanClass) {
            this.beanClass = beanClass;
            return this;
        }

        public Builder<E> excelCellStyleClass(Class<? extends ExcelCellStyle> excelCellStyleClass) {
            this.excelCellStyleClass = excelCellStyleClass;
            return this;
        }

        public Builder() {
            this.excelType = ExcelType.XLSX;
            this.sheetName = "Sheet1";
            this.titleRowHeight = 20;
            this.secondTitleRowHeight = 16;
            this.dataRowHeight = 13.5f;
        }

        Builder(ExcelWriteConfig<E> excelWriteConfig) {
            this.title = excelWriteConfig.title;
            this.secondTitle = excelWriteConfig.secondTitle;
            this.excelType = excelWriteConfig.excelType;
            this.titleRowHeight = excelWriteConfig.titleRowHeight;
            this.secondTitleRowHeight = excelWriteConfig.secondTitleRowHeight;
            this.dataRowHeight = excelWriteConfig.dataRowHeight;
            this.sheetName = excelWriteConfig.sheetName;
            this.headerRowCreate = excelWriteConfig.headerRowCreate;
            this.beanClass = excelWriteConfig.beanClass;
            this.excelCellStyleClass = excelWriteConfig.excelCellStyleClass;
        }

        public ExcelWriteConfig<E> build() {
            return new ExcelWriteConfig<>(this);
        }

    }

    public Builder<E> newBuilder() {
        return new Builder<>(this);
    }
}
