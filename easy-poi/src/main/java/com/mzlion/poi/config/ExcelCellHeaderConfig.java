package com.mzlion.poi.config;

import com.mzlion.poi.constant.ExcelCellType;

/**
 * Excel单元格标题头和Java属性名映射配置关系
 *
 * @author mzlion
 * @date 2016-06-16
 */
public class ExcelCellHeaderConfig {

    /**
     * cell标题,必须唯一
     */
    private String title;

    /**
     * cell类型
     */
    private ExcelCellType excelCellType;

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

    /**
     * 自动换行
     */
    private boolean autoWrap;

    /**
     * cell对应JavaBean属性名
     */
    private String propertyName;

    /**
     * Excel的单元格
     */
    public String getTitle() {
        return title;
    }

    /**
     * Excel的单元格类型，默认值{@link ExcelCellType#AUTO}
     */
    public ExcelCellType getExcelCellType() {
        return excelCellType;
    }

    /**
     * 单元格位置
     */
    public int getCellIndex() {
        return cellIndex;
    }

    /**
     * Excel中的日期格式类型
     */
    public String getExcelDateFormat() {
        return excelDateFormat;
    }

    /**
     * Java中的日期格式类型
     */
    public String getJavaDateFormat() {
        return javaDateFormat;
    }

    /**
     * 单元格宽度
     */
    public float getWidth() {
        return width;
    }

    /**
     * 单元格是否自动换行
     */
    public boolean isAutoWrap() {
        return autoWrap;
    }

    /**
     * 单元格列对应的JavaBean属性名称
     */
    public String getPropertyName() {
        return propertyName;
    }

    private ExcelCellHeaderConfig(Builder builder) {
        this.title = builder.title;
        this.excelCellType = builder.excelCellType;
        this.cellIndex = builder.cellIndex;
        this.excelDateFormat = builder.excelDateFormat;
        this.javaDateFormat = builder.javaDateFormat;
        this.width = builder.width;
        this.autoWrap = builder.autoWrap;
        this.propertyName = builder.propertyName;
    }

    /**
     * {@code ExcelCellHeaderConfig}对象构造
     *
     * @author mzlion
     */
    public static class Builder {

        /**
         * cell标题
         */
        private String title;

//        private boolean required;

        /**
         * cell类型
         */
        private ExcelCellType excelCellType;

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

        /**
         * 自动换行
         */
        private boolean autoWrap;

        /**
         * cell对应JavaBean属性名
         */
        private String propertyName;

        public Builder() {
            this.cellIndex = 0;
            this.excelCellType = ExcelCellType.AUTO;
            this.width = 8.38f;
            this.autoWrap = false;
        }

        public Builder(ExcelCellHeaderConfig excelCellHeaderConfig) {
            this.title = excelCellHeaderConfig.title;
            this.excelCellType = excelCellHeaderConfig.excelCellType;
            this.propertyName = excelCellHeaderConfig.propertyName;
            this.cellIndex = excelCellHeaderConfig.cellIndex;
            this.excelDateFormat = excelCellHeaderConfig.excelDateFormat;
            this.javaDateFormat = excelCellHeaderConfig.javaDateFormat;
            this.width = excelCellHeaderConfig.width;
            this.autoWrap = excelCellHeaderConfig.autoWrap;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

//        public Builder required(boolean required) {
//            this.required = required;
//            return this;
//        }

        public Builder excelCellType(ExcelCellType excelCellType) {
            this.excelCellType = excelCellType;
            return this;
        }

        public Builder cellIndex(int cellIndex) {
            this.cellIndex = cellIndex;
            return this;
        }

        public Builder excelDateFormat(String excelDateFormat) {
            this.excelDateFormat = excelDateFormat;
            return this;
        }

        public Builder javaDateFormat(String javaDateFormat) {
            this.javaDateFormat = javaDateFormat;
            return this;
        }

        public Builder width(float width) {
            this.width = width;
            return this;
        }

        public Builder autoWrap(boolean autoWrap) {
            this.autoWrap = autoWrap;
            return this;
        }

        public Builder propertyName(String propertyName) {
            this.propertyName = propertyName;
            return this;
        }

        public ExcelCellHeaderConfig build() {
            return new ExcelCellHeaderConfig(this);
        }
    }
}
