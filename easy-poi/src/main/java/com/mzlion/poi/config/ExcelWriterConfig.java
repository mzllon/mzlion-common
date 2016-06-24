package com.mzlion.poi.config;

import com.mzlion.core.lang.Assert;
import com.mzlion.core.lang.ClassUtils;
import com.mzlion.core.lang.CollectionUtils;
import com.mzlion.poi.constant.ExcelType;
import com.mzlion.poi.style.DefaultExcelCellStyleHandler;
import com.mzlion.poi.style.ExcelCellStyleHandler;

import java.util.*;

/**
 * Excel的导出引擎
 *
 * @author mzlion
 * @date 2016-06-17
 */
public class ExcelWriterConfig extends ExcelEntityValidator {

    /**
     * Excel标题
     */
    private String title;

    private float titleRowHeight;

    /**
     * Excel副标题
     */
    private String secondTitle;

    private float secondTitleRowHeight;

    /**
     *
     */
    private ExcelType excelType;

    private float dataRowHeight;

    private String sheetName;

    private boolean headerRowCreate;

    private Class<?> rawClass;

    private Class<? extends ExcelCellStyleHandler> styleHandler;

    private List<ExcelCellHeaderConfig> excelCellHeaderConfigList;

    /**
     * 配置排除属性列表
     */
    private List<String> excludePropertyNames;

    public String getTitle() {
        return title;
    }

    public float getTitleRowHeight() {
        return titleRowHeight;
    }

    public String getSecondTitle() {
        return secondTitle;
    }

    public float getSecondTitleRowHeight() {
        return secondTitleRowHeight;
    }

    public ExcelType getExcelType() {
        return excelType;
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

    public Class<?> getRawClass() {
        return rawClass;
    }

    public Class<? extends ExcelCellStyleHandler> getStyleHandler() {
        return styleHandler;
    }

    public List<ExcelCellHeaderConfig> getExcelCellHeaderConfigList() {
        return excelCellHeaderConfigList;
    }

    public List<String> getExcludePropertyNames() {
        return excludePropertyNames;
    }

    private ExcelWriterConfig(Builder builder) {
        this.title = builder.title;
        this.secondTitle = builder.secondTitle;
        this.excelType = builder.excelType;
        this.titleRowHeight = builder.titleRowHeight;
        this.secondTitleRowHeight = builder.secondTitleRowHeight;
        this.dataRowHeight = builder.dataRowHeight;
        this.sheetName = builder.sheetName;
        this.headerRowCreate = builder.headerRowCreate;
        this.styleHandler = builder.styleHandler;
        this.rawClass = builder.rawClass;
        this.excelCellHeaderConfigList = builder.excelCellHeaderConfigList;
        this.excludePropertyNames = new ArrayList<>(builder.excludePropertyNames);

        Assert.isTrue(this.rawClass != null, "The bean class is null.");

        if (ClassUtils.isAssignable(Map.class, this.rawClass)) {
            Assert.notEmpty(this.excelCellHeaderConfigList, "The excelCellHeaderConfig list must not be null or empty when the bean type is Map class.");
        } else {
            this.validateJavaBean(this.rawClass);
        }
    }

    public Builder newBuilder() {
        return new Builder(this);
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


        private Class<?> rawClass;


        private Class<? extends ExcelCellStyleHandler> styleHandler;

        private List<ExcelCellHeaderConfig> excelCellHeaderConfigList;

        private Set<String> excludePropertyNames;

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

        public Builder rawClass(Class<?> rawClass) {
            this.rawClass = rawClass;
            return this;
        }

        public Builder styleHandler(Class<? extends ExcelCellStyleHandler> styleHandler) {
            this.styleHandler = styleHandler;
            return this;
        }

        public Builder excelCellHeaderConfig(ExcelCellHeaderConfig excelCellHeaderConfig) {
            Assert.notNull(excelCellHeaderConfig, "ExcelCellConfig is null.");
            this.excelCellHeaderConfigList.add(excelCellHeaderConfig);
            return this;
        }

        public Builder excelCellHeaderConfig(List<ExcelCellHeaderConfig> excelCellHeaderConfigList) {
            Assert.notEmpty(excelCellHeaderConfigList, "The ExcelCellConfig list is null.");
            this.excelCellHeaderConfigList.addAll(excelCellHeaderConfigList);
            return this;
        }

        public Builder excludePropertyName(String propertyName) {
            this.excludePropertyNames.add(propertyName);
            return this;
        }


        public Builder() {
            this.excelType = ExcelType.XLSX;
            this.sheetName = "Sheet1";
            this.titleRowHeight = 20;
            this.secondTitleRowHeight = 16;
            this.dataRowHeight = 13.5f;
            this.styleHandler = DefaultExcelCellStyleHandler.class;
            this.rawClass = Map.class;
            this.excelCellHeaderConfigList = new ArrayList<>();
            this.headerRowCreate = true;
            this.excludePropertyNames = new HashSet<>();
        }

        Builder(ExcelWriterConfig writeExcelConfig) {
            this.title = writeExcelConfig.title;
            this.secondTitle = writeExcelConfig.secondTitle;
            this.excelType = writeExcelConfig.excelType;
            this.titleRowHeight = writeExcelConfig.titleRowHeight;
            this.secondTitleRowHeight = writeExcelConfig.secondTitleRowHeight;
            this.dataRowHeight = writeExcelConfig.dataRowHeight;
            this.sheetName = writeExcelConfig.sheetName;
            this.headerRowCreate = writeExcelConfig.headerRowCreate;
            this.rawClass = writeExcelConfig.rawClass;
            this.styleHandler = writeExcelConfig.styleHandler;
            this.excelCellHeaderConfigList = writeExcelConfig.excelCellHeaderConfigList;
            this.excludePropertyNames = new HashSet<>();
            if (CollectionUtils.isNotEmpty(writeExcelConfig.excludePropertyNames))
                this.excludePropertyNames.addAll(writeExcelConfig.excludePropertyNames);
        }

        public ExcelWriterConfig build() {
            return new ExcelWriterConfig(this);
        }

    }
}
