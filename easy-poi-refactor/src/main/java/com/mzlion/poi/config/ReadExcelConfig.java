package com.mzlion.poi.config;

import com.mzlion.core.lang.Assert;
import com.mzlion.core.lang.ClassUtils;
import com.mzlion.core.lang.StringUtils;
import com.mzlion.poi.exception.ReadExcelException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel导入配置选项
 *
 * @author mzlion
 * @date 2016-06-16
 */
public class ReadExcelConfig extends ExcelEntityValidator {

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

    private List<ExcelCellHeaderConfig> cellHeaderConfigList;

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

    public List<ExcelCellHeaderConfig> getCellHeaderConfigList() {
        return cellHeaderConfigList;
    }

    private ReadExcelConfig(Builder builder) {
        this.headerRowStart = builder.headerRowStart;
        this.headerRowUsed = builder.headerRowUsed;
        this.dataRowGap = builder.dataRowGap;
        this.sheetIndex = builder.sheetIndex;
        this.sheetNum = builder.sheetNum;
        this.lastInvalidRow = builder.lastInvalidRow;
        this.rawClass = builder.rawClass;
        this.cellHeaderConfigList = builder.cellHeaderConfigList;

        //validate
        Assert.isTrue(this.headerRowStart >= 0, "Property [headerRowStart] must be great than 0.");
        Assert.isTrue(this.headerRowUsed > 0, "Property [headerTitleRowUsed] must be great than 0.");
        Assert.isTrue(this.dataRowGap >= 0, "Property [DataRowGap] must be great than 0.");
        Assert.isTrue(this.sheetIndex > 0, "Property [SheetIndex] must be great than 0.");
        Assert.isTrue(this.sheetNum > 0, "Property [SheetNum] must be great than 0.");
        Assert.isTrue(this.lastInvalidRow >= 0, "LastInvalidRow must be great than 0.");
        if (ClassUtils.isAssignable(Map.class, this.rawClass)) {
            Assert.notEmpty(this.cellHeaderConfigList, "Property [cellHeaderConfigList] must be not null or empty if raw class is Map.");
        } else {
            this.validateJavaBean(this.rawClass);
        }
    }



    /**
     * {@code ExcelReadConfig}的构建对象
     *
     * @author mzlion
     * @see ReadExcelConfig
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
        private Class<?> rawClass;

        /**
         * Excel的Cell配置
         */
        private List<ExcelCellHeaderConfig> cellHeaderConfigList;

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
            this.rawClass = Map.class;
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
         * @param rawClass JavaBean or Map class
         * @return {@link Builder}
         */
        public Builder rawClass(Class<?> rawClass) {
            this.rawClass = rawClass;
            return this;
        }

        public Builder excelCellHeaderConfig(ExcelCellHeaderConfig excelCellHeaderConfig) {
            if (this.cellHeaderConfigList == null) {
                this.cellHeaderConfigList = new ArrayList<>();
            }
            if (StringUtils.isEmpty(excelCellHeaderConfig.getTitle())) {
                throw new ReadExcelException("ExcelCellHeaderConfig title is null.");
            }
            if (StringUtils.isEmpty(excelCellHeaderConfig.getPropertyName())) {
                throw new ReadExcelException("ExcelCellHeaderConfig propertyName is null.");
            }
            this.cellHeaderConfigList.add(excelCellHeaderConfig);
            return this;
        }

        public Builder excelCellHeaderConfig(List<ExcelCellHeaderConfig> excelCellHeaderConfigList) {
            if (this.cellHeaderConfigList == null) {
                this.cellHeaderConfigList = new ArrayList<>();
            }
            this.cellHeaderConfigList.addAll(excelCellHeaderConfigList);
            return this;
        }

        /**
         * 构建{@code ExcelReadConfig}对象
         *
         * @return {@link ReadExcelConfig}
         */
        public ReadExcelConfig build() {
            return new ReadExcelConfig(this);
        }
    }


}
