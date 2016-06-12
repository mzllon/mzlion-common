package com.mzlion.poi.config;

import com.mzlion.poi.beans.TypeReference;
import com.mzlion.poi.constant.ExcelType;
import com.mzlion.poi.excel.write.DefaultExcelCellStyle;
import com.mzlion.poi.excel.write.ExcelCellStyle;
import net.jodah.typetools.TypeResolver;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Excel导出配置选项
 *
 * @author mzlion
 * @date 2016-06-07
 */
public class ExcelWriteConfig {

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

    private Type type;

    private Class<?> rawClass;

    private Class<? extends ExcelCellStyle> excelCellStyleClass;

    private ExcelWriteConfig(Builder builder) {
        this.title = builder.title;
        this.secondTitle = builder.secondTitle;
        this.excelType = builder.excelType;
        this.titleRowHeight = builder.titleRowHeight;
        this.secondTitleRowHeight = builder.secondTitleRowHeight;
        this.dataRowHeight = builder.dataRowHeight;
        this.sheetName = builder.sheetName;
        this.headerRowCreate = builder.headerRowCreate;
        this.type = builder.type;
        this.excelCellStyleClass = builder.excelCellStyleClass;
        this.rawClass = TypeResolver.resolveRawClass(this.type, null);
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

    public Type getType() {
        return type;
    }

    public Class<?> getRawClass() {
        return rawClass;
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
        sb.append(", type=").append(type);
        sb.append(", excelCellStyleClass=").append(excelCellStyleClass);
        sb.append('}');
        return sb.toString();
    }

    public static class Builder {

        private String title;

        private String secondTitle;

        private ExcelType excelType;

        private float titleRowHeight;

        private float secondTitleRowHeight;

        private float dataRowHeight;

        private String sheetName;

        private boolean headerRowCreate;

        private Type type;

        private Class<? extends ExcelCellStyle> excelCellStyleClass;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder secondTitle(String secondTitle) {
            this.secondTitle = secondTitle;
            return this;
        }

        public Builder excelType(ExcelType excelType) {
            this.excelType = excelType;
            return this;
        }

        public Builder titleRowHeight(float titleRowHeight) {
            this.titleRowHeight = titleRowHeight;
            return this;
        }

        public Builder secondTitleRowHeight(float secondTitleRowHeight) {
            this.secondTitleRowHeight = secondTitleRowHeight;
            return this;
        }

        public Builder dataRowHeight(float dataRowHeight) {
            this.dataRowHeight = dataRowHeight;
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

        public Builder beanClass(Class<?> beanClass) {
            this.type = beanClass;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public <T> Builder type(TypeReference<T> typeReference) {
            this.type = typeReference.getType();
            return this;
        }

        public Builder excelCellStyleClass(Class<? extends ExcelCellStyle> excelCellStyleClass) {
            this.excelCellStyleClass = excelCellStyleClass;
            return this;
        }

        public Builder() {
            this.excelType = ExcelType.XLSX;
            this.sheetName = "Sheet1";
            this.titleRowHeight = 20;
            this.secondTitleRowHeight = 16;
            this.dataRowHeight = 13.5f;
            this.excelCellStyleClass = DefaultExcelCellStyle.class;
            this.type(Map.class);
        }

        Builder(ExcelWriteConfig excelWriteConfig) {
            this.title = excelWriteConfig.title;
            this.secondTitle = excelWriteConfig.secondTitle;
            this.excelType = excelWriteConfig.excelType;
            this.titleRowHeight = excelWriteConfig.titleRowHeight;
            this.secondTitleRowHeight = excelWriteConfig.secondTitleRowHeight;
            this.dataRowHeight = excelWriteConfig.dataRowHeight;
            this.sheetName = excelWriteConfig.sheetName;
            this.headerRowCreate = excelWriteConfig.headerRowCreate;
            this.type = excelWriteConfig.type;
            this.excelCellStyleClass = excelWriteConfig.excelCellStyleClass;
        }

        public ExcelWriteConfig build() {
            return new ExcelWriteConfig(this);
        }

    }

    public Builder newBuilder() {
        return new Builder(this);
    }
}
