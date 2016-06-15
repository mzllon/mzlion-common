package com.mzlion.poi.config;

import com.mzlion.core.lang.Assert;
import com.mzlion.core.lang.ClassUtils;
import com.mzlion.poi.annotation.ExcelEntity;
import com.mzlion.poi.exception.BeanNotConfigAnnotationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel导入配置选项
 *
 * @author mzlion
 * @date 2016-06-08
 */
public class ExcelReadConfig {

    /**
     * 列标题开始行
     */
    private int headerRowStart;

    /**
     * 列标题占用Excel行数
     */
    private int headerRowUsed;

    /**
     * 数据行和列标题行间隔行数
     */
    private int dataRowGap;

    /**
     * 默认读取的sheet索引
     */
    private int sheetIndex;

    /**
     * 读取sheet的数量
     */
    private int sheetNum;

    /**
     * 最后几行无效时读取结束
     */
    private int lastInvalidRow;

    /**
     * 原始的Bean类型
     */
    private Class<?> rawClass;

    private List<ExcelCellConfig> excelCellConfigList;

    public int getHeaderRowUsed() {
        return headerRowUsed;
    }

    public int getHeaderRowStart() {
        return headerRowStart;
    }

    public int getDataRowGap() {
        return dataRowGap;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public int getSheetNum() {
        return sheetNum;
    }

    public int getLastInvalidRow() {
        return lastInvalidRow;
    }

    public Class<?> getRawClass() {
        return rawClass;
    }

    public List<ExcelCellConfig> getExcelCellConfigList() {
        return excelCellConfigList;
    }

    private ExcelReadConfig(Builder builder) {
        this.headerRowStart = builder.headerRowStart;
        this.headerRowUsed = builder.headerRowUsed;
        this.dataRowGap = builder.dataRowGap;
        this.sheetIndex = builder.sheetIndex;
        this.sheetNum = builder.sheetNum;
        this.lastInvalidRow = builder.lastInvalidRow;
        this.rawClass = builder.beanClass;
        this.excelCellConfigList = builder.excelCellConfigList;

        //validate
        Assert.isTrue(this.headerRowStart >= 0, "HeaderRowStart must be great than 0.");
        Assert.isTrue(this.headerRowUsed > 0, "HeaderTitleRowUsed must be great than 0.");
        Assert.isTrue(this.dataRowGap >= 0, "DataRowGap must be great than 0.");
        Assert.isTrue(this.sheetIndex > 0, "SheetIndex must be great than 0.");
        Assert.isTrue(this.sheetNum > 0, "SheetNum must be great than 0.");
        Assert.isTrue(this.lastInvalidRow >= 0, "LastInvalidRow must be great than 0.");
        if (ClassUtils.isAssignable(Map.class, this.rawClass)) {
            Assert.notEmpty(this.excelCellConfigList, "ExcelCellConfigList must be not null or empty.");
        } else {
            if (!this.rawClass.isAnnotationPresent(ExcelEntity.class)) {
                throw new BeanNotConfigAnnotationException(String.format("The class [%s] must config annotation [%s]",
                        this.rawClass.getName(), ExcelEntity.class.getName()));
            }
        }
    }


    /**
     * {@code ExcelReadConfig}的构建对象
     *
     * @author mzlion
     * @see ExcelReadConfig
     */
    public static class Builder {

        /**
         * 列标题开始行
         */
        private int headerRowStart;

        /**
         * 列标题占用Excel行数
         */
        private int headerRowUsed;

        /**
         * 数据行和列标题行间隔行数
         */
        private int dataRowGap;

        /**
         * 从第几个sheet开始读取，默认是第一个
         */
        private int sheetIndex;

        /**
         * 读取sheet个数控制，默认读取1个
         */
        private int sheetNum;

        /**
         * 最后几行无效判定读取结束，默认0表示一直读取到结束。
         */
        private int lastInvalidRow;

        /**
         * JavaBean class
         */
        private Class<?> beanClass;

        /**
         * Excel的Cell配置
         */
        private List<ExcelCellConfig> excelCellConfigList;

        /**
         * default constructor
         */
        public Builder() {
            this.headerRowStart = 2;
            this.headerRowUsed = 1;
            this.dataRowGap = 0;
            this.sheetIndex = 1;
            this.sheetNum = 1;
            this.lastInvalidRow = 0;
            this.beanClass = Map.class;
        }

        /**
         * 列标题开始行
         *
         * @param headerRowStart head row start num.
         * @return {@link Builder}
         */
        public Builder headerRowStart(int headerRowStart) {
            this.headerRowStart = headerRowStart;
            return this;
        }

        /**
         * 列标题行占用行数
         *
         * @param headerRowUsed the title row use num.
         * @return {@link Builder}
         */
        public Builder headerRowUsed(int headerRowUsed) {
            this.headerRowUsed = headerRowUsed;
            return this;
        }

        /**
         * 列标题开始行
         *
         * @param dataRowGap header row start num
         * @return {@link Builder}
         */
        public Builder dataRowGap(int dataRowGap) {
            this.dataRowGap = dataRowGap;
            return this;
        }


        /**
         * 从第几个sheet开始读取
         *
         * @param sheetIndex start sheet index
         * @return {@link Builder}
         */
        public Builder sheetIndex(int sheetIndex) {
            this.sheetIndex = sheetIndex;
            return this;
        }

        /**
         * 读取sheet个数控制
         *
         * @param sheetNum sheet num.
         * @return {@link Builder}
         */
        public Builder sheetNum(int sheetNum) {
            this.sheetNum = sheetNum;
            return this;
        }

        /**
         * 最后几行无效判定读取结束
         *
         * @param lastInvalidRow the last invalid of row num.
         * @return {@link Builder}
         */
        public Builder lastInvalidRow(int lastInvalidRow) {
            this.lastInvalidRow = lastInvalidRow;
            return this;
        }

        /**
         * Set JavaBean class
         *
         * @param beanClass JavaBean class
         * @return {@link Builder}
         */
        public Builder beanClass(Class<?> beanClass) {
            this.beanClass = beanClass;
            return this;
        }

        public Builder excelCellConfig(ExcelCellConfig excelCellConfig) {
            if (this.excelCellConfigList == null) {
                this.excelCellConfigList = new ArrayList<>();
            }
            this.excelCellConfigList.add(excelCellConfig);
            return this;
        }

        public Builder excelCellConfig(List<ExcelCellConfig> excelCellConfigList) {
            if (this.excelCellConfigList == null) {
                this.excelCellConfigList = new ArrayList<>();
            }
            this.excelCellConfigList.addAll(excelCellConfigList);
            return this;
        }

        /**
         * 构建{@code ExcelReadConfig}对象
         *
         * @return {@link ExcelReadConfig}
         */
        public ExcelReadConfig build() {
            return new ExcelReadConfig(this);
        }
    }
}
