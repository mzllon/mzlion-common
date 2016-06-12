package com.mzlion.poi.config;

/**
 * <p>
 * Excel导入配置选项
 * </p>
 *
 * @author mzlion
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
        private int titleRowUsed = 1;

        /**
         * 标题行占用行数，默认占用1行
         */
        private int headerTitleRowUsed = 1;

        /**
         * 数据行和标题行相差行数，默认0行
         */
        private int dataRowStart = 0;

        /**
         * 读取第一个sheet
         */
        private int sheetIndex = 1;

        /**
         * 读取sheet个数控制，默认读取1个
         */
        private int sheetNum = 1;

        /**
         * 最后几行无效判定读取结束，默认0表示一直读取到结束。
         */
        private int lastInvalidRow = 0;

        /**
         * 是否严格校验列标题
         */
        private boolean strict = false;

        private Class<E> beanClass;

        /**
         * @param titleRowUsed
         * @return
         */
        public Builder<E> titleRowUsed(int titleRowUsed) {
            this.titleRowUsed = titleRowUsed;
            return this;
        }

        /**
         * @param headerTitleRowUsed
         * @return
         */
        public Builder<E> headerTitleRowUsed(int headerTitleRowUsed) {
            this.headerTitleRowUsed = headerTitleRowUsed;
            return this;
        }

        /**
         * @param dataRowStart
         * @return
         */
        public Builder<E> dataRowStart(int dataRowStart) {
            this.dataRowStart = dataRowStart;
            return this;
        }

        /**
         * @param sheetIndex
         * @return
         */
        public Builder<E> sheetIndex(int sheetIndex) {
            this.sheetIndex = sheetIndex;
            return this;
        }

        /**
         * @param sheetNum
         * @return
         */
        public Builder<E> sheetNum(int sheetNum) {
            this.sheetNum = sheetNum;
            return this;
        }

        /**
         * @param lastInvalidRow
         * @return
         */
        public Builder<E> lastInvalidRow(int lastInvalidRow) {
            this.lastInvalidRow = lastInvalidRow;
            return this;
        }

        /**
         * @param strict
         * @return
         */
        public Builder<E> strict(boolean strict) {
            this.strict = strict;
            return this;
        }

        public Builder<E> beanClass(Class<E> beanClass) {
            this.beanClass = beanClass;
            return this;
        }

        public ExcelReadConfig<E> build() {
            return new ExcelReadConfig<>(this);
        }
    }
}
