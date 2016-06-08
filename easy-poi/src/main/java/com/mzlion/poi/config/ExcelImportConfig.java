package com.mzlion.poi.config;

/**
 * Created by mzlion on 2016/6/7.
 */
public class ExcelImportConfig {

    private int titleRowUsed;
    private int headerTitleRowUsed;
    private int dataRowStart;

    private int sheetIndex;
    private int sheetNum;

    private int lastInvalidRow;

    private boolean strict;

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

    private ExcelImportConfig(Builder builder) {
        this.titleRowUsed = builder.titleRowUsed;
        this.headerTitleRowUsed = builder.headerTitleRowUsed;
        this.dataRowStart = builder.dataRowStart;
        this.sheetIndex = builder.sheetIndex;
        this.sheetNum = builder.sheetNum;
        this.lastInvalidRow = builder.lastInvalidRow;
        this.strict = builder.strict;
    }

    public static class Builder {

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

        /**
         * @param titleRowUsed
         * @return
         */
        public Builder titleRowUsed(int titleRowUsed) {
            this.titleRowUsed = titleRowUsed;
            return this;
        }

        /**
         * @param headerTitleRowUsed
         * @return
         */
        public Builder headerTitleRowUsed(int headerTitleRowUsed) {
            this.headerTitleRowUsed = headerTitleRowUsed;
            return this;
        }

        /**
         * @param dataRowStart
         * @return
         */
        public Builder dataRowStart(int dataRowStart) {
            this.dataRowStart = dataRowStart;
            return this;
        }

        /**
         * @param sheetIndex
         * @return
         */
        public Builder sheetIndex(int sheetIndex) {
            this.sheetIndex = sheetIndex;
            return this;
        }

        /**
         * @param sheetNum
         * @return
         */
        public Builder sheetNum(int sheetNum) {
            this.sheetNum = sheetNum;
            return this;
        }

        /**
         * @param lastInvalidRow
         * @return
         */
        public Builder lastInvalidRow(int lastInvalidRow) {
            this.lastInvalidRow = lastInvalidRow;
            return this;
        }

        /**
         * @param strict
         * @return
         */
        public Builder strict(boolean strict) {
            this.strict = strict;
            return this;
        }

        public ExcelImportConfig build() {
            return new ExcelImportConfig(this);
        }
    }
}
