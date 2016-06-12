package com.mzlion.poi.config;

import java.util.Map;

/**
 * Excel导入配置选项
 *
 * @author mzlion
 * @date 2016-06-08
 */
public class ExcelReadConfig<E> {

    private int titleRowUsed;
    private int headerTitleRowUsed;
    private int dataRowStart;

    private int sheetIndex;
    private int sheetNum;

    private int lastInvalidRow;

    private boolean strict;

    private Class<E> beanClass;

    public int getTitleRowUsed() {
        return titleRowUsed;
    }

    public int getHeaderTitleRowUsed() {
        return headerTitleRowUsed;
    }

    public int getDataRowStart() {
        return dataRowStart;
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

    public boolean isStrict() {
        return strict;
    }

    public Class<E> getBeanClass() {
        return beanClass;
    }

    private ExcelReadConfig(Builder<E> builder) {
        this.titleRowUsed = builder.titleRowUsed;
        this.headerTitleRowUsed = builder.headerTitleRowUsed;
        this.dataRowStart = builder.dataRowStart;
        this.sheetIndex = builder.sheetIndex;
        this.sheetNum = builder.sheetNum;
        this.lastInvalidRow = builder.lastInvalidRow;
        this.strict = builder.strict;
        this.beanClass = builder.beanClass;
    }

    public static class Builder<E> {

        /**
         * 标题行占用行数，默认占用1行
         */
        private int titleRowUsed;

        /**
         * 列标题行占用行数，默认占用1行
         */
        private int headerTitleRowUsed;

        /**
         * 数据行和列标题行相差行数，默认0行
         */
        private int dataRowStart;

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
         * 是否严格校验列标题
         */
        private boolean strict;

        /**
         * JavaBean class
         */
        private Class<E> beanClass;

        public Builder() {
            this.titleRowUsed = 1;
            this.headerTitleRowUsed = 1;
            this.dataRowStart = 0;
            this.sheetIndex = 1;
            this.sheetNum = 1;
            this.lastInvalidRow = 0;
            this.strict = false;
            this.beanClass = (Class<E>) Map.class;
        }

        /**
         * 标题行占用行数
         *
         * @param titleRowUsed 标题行占用行数
         * @return {@link Builder}
         */
        public Builder<E> titleRowUsed(int titleRowUsed) {
            this.titleRowUsed = titleRowUsed;
            return this;
        }

        /**
         * 列标题行占用行数
         *
         * @param headerTitleRowUsed the title row use num.
         * @return {@link Builder}
         */
        public Builder<E> headerTitleRowUsed(int headerTitleRowUsed) {
            this.headerTitleRowUsed = headerTitleRowUsed;
            return this;
        }

        /**
         * 数据行和列标题行相差行数
         *
         * @param dataRowStart data row start num
         * @return {@link Builder}
         */
        public Builder<E> dataRowStart(int dataRowStart) {
            this.dataRowStart = dataRowStart;
            return this;
        }

        /**
         * 从第几个sheet开始读取
         *
         * @param sheetIndex start sheet index
         * @return {@link Builder}
         */
        public Builder<E> sheetIndex(int sheetIndex) {
            this.sheetIndex = sheetIndex;
            return this;
        }

        /**
         * 读取sheet个数控制
         *
         * @param sheetNum sheet num.
         * @return {@link Builder}
         */
        public Builder<E> sheetNum(int sheetNum) {
            this.sheetNum = sheetNum;
            return this;
        }

        /**
         * 最后几行无效判定读取结束
         *
         * @param lastInvalidRow the last invalid of row num.
         * @return {@link Builder}
         */
        public Builder<E> lastInvalidRow(int lastInvalidRow) {
            this.lastInvalidRow = lastInvalidRow;
            return this;
        }

        /**
         * 是否严格校验列标题
         *
         * @param strict strict to valid cell
         * @return {@link Builder}
         */
        public Builder<E> strict(boolean strict) {
            this.strict = strict;
            return this;
        }

        /**
         * Set JavaBean class
         *
         * @param beanClass JavaBean class
         * @return {@link Builder}
         */
        public Builder<E> beanClass(Class<E> beanClass) {
            this.beanClass = beanClass;
            return this;
        }

        /**
         * 构建{@code ExcelReadConfig}对象
         *
         * @return {@link ExcelReadConfig}
         */
        public ExcelReadConfig<E> build() {
            return new ExcelReadConfig<>(this);
        }
    }
}
