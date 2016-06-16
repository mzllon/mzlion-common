package com.mzlion.poi.config;

import com.mzlion.poi.constant.ExcelCellType;
import com.mzlion.poi.constant.ExcelHyperLinkType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mzlion on 2016/6/13.
 */
public class ExcelCellConfig implements Comparable<ExcelCellConfig> {

    /**
     * cell标题,必须唯一
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

    /**
     * 自动换行
     */
    private boolean autoWrap;

    private ExcelHyperLinkType excelHyperLinkType;

    private String hyperlinkName;

    private List<ExcelCellConfig> excelCellConfigChildren;

    private ExcelCellConfig(Builder builder) {
        this.title = builder.title;
        this.required = builder.required;
        this.type = builder.type;
        this.propertyName = builder.propertyName;
        this.cellIndex = builder.cellIndex;
        this.excelDateFormat = builder.excelDateFormat;
        this.javaDateFormat = builder.javaDateFormat;
        this.width = builder.width;
        this.autoWrap = builder.autoWrap;
        this.excelHyperLinkType = builder.excelHyperLinkType;
        this.hyperlinkName = builder.hyperlinkName;
        this.excelCellConfigChildren = builder.excelCellConfigChildren;
    }

    public String getTitle() {
        return title;
    }

    public boolean isRequired() {
        return required;
    }

    public ExcelCellType getType() {
        return type;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public String getExcelDateFormat() {
        return excelDateFormat;
    }

    public String getJavaDateFormat() {
        return javaDateFormat;
    }

    public float getWidth() {
        return width;
    }

    public boolean isAutoWrap() {
        return autoWrap;
    }

    public ExcelHyperLinkType getExcelHyperLinkType() {
        return excelHyperLinkType;
    }

    public String getHyperlinkName() {
        return hyperlinkName;
    }

    public List<ExcelCellConfig> getExcelCellConfigChildren() {
        return excelCellConfigChildren;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExcelCellConfig{");
        sb.append("title='").append(title).append('\'');
        sb.append(", required=").append(required);
        sb.append(", type=").append(type);
        sb.append(", propertyName='").append(propertyName).append('\'');
        sb.append(", cellIndex=").append(cellIndex);
        sb.append(", excelDateFormat='").append(excelDateFormat).append('\'');
        sb.append(", javaDateFormat='").append(javaDateFormat).append('\'');
        sb.append(", width=").append(width);
        sb.append(", autoWrap=").append(autoWrap);
        sb.append(", excelHyperLinkType=").append(excelHyperLinkType);
        sb.append(", hyperlinkName='").append(hyperlinkName).append('\'');
        sb.append(", excelCellConfigChildren=").append(excelCellConfigChildren);
        sb.append('}');
        return sb.toString();
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    @Override
    public int compareTo(ExcelCellConfig o) {
        return this.cellIndex - o.cellIndex;
    }

    public static class Builder {

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

        /**
         * 自动换行
         */
        private boolean autoWrap;

        private ExcelHyperLinkType excelHyperLinkType;

        private String hyperlinkName;

        private List<ExcelCellConfig> excelCellConfigChildren;

        public Builder() {
            this.required = false;
            this.cellIndex = 0;
            this.type = ExcelCellType.AUTO;
            this.width = 8.38f;
            this.autoWrap = false;
        }

        public Builder(ExcelCellConfig excelCellConfig) {
            this.title = excelCellConfig.title;
            this.required = excelCellConfig.required;
            this.type = excelCellConfig.type;
            this.propertyName = excelCellConfig.propertyName;
            this.cellIndex = excelCellConfig.cellIndex;
            this.excelDateFormat = excelCellConfig.excelDateFormat;
            this.javaDateFormat = excelCellConfig.javaDateFormat;
            this.width = excelCellConfig.width;
            this.autoWrap = excelCellConfig.autoWrap;
            this.excelHyperLinkType = excelCellConfig.excelHyperLinkType;
            this.hyperlinkName = excelCellConfig.hyperlinkName;
            this.excelCellConfigChildren = excelCellConfig.excelCellConfigChildren;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder required(boolean required) {
            this.required = required;
            return this;
        }

        public Builder type(ExcelCellType type) {
            this.type = type;
            return this;
        }

        public Builder propertyName(String propertyName) {
            this.propertyName = propertyName;
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

        public Builder excelHyperLinkType(ExcelHyperLinkType excelHyperLinkType) {
            this.excelHyperLinkType = excelHyperLinkType;
            return this;
        }

        public Builder hyperlinkName(String hyperlinkName) {
            this.hyperlinkName = hyperlinkName;
            return this;
        }

        public Builder child(ExcelCellConfig excelCellConfig) {
            if (this.excelCellConfigChildren == null) {
                this.excelCellConfigChildren = new ArrayList<>();
            }
            this.excelCellConfigChildren.add(excelCellConfig);
            return this;
        }

        public Builder child(List<ExcelCellConfig> excelCellConfigList) {
            if (this.excelCellConfigChildren == null) {
                this.excelCellConfigChildren = new ArrayList<>();
            }
            this.excelCellConfigChildren.addAll(excelCellConfigList);
            return this;
        }

        public Builder clearChild() {
            this.excelCellConfigChildren.clear();
            return this;
        }

        public ExcelCellConfig build() {
            return new ExcelCellConfig(this);
        }
    }
}
