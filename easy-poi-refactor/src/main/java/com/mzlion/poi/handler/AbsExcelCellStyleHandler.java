package com.mzlion.poi.handler;

import com.mzlion.poi.config.ExcelCellHeaderConfig;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * Created by mzlion on 2016/6/10.
 */
public abstract class AbsExcelCellStyleHandler implements ExcelCellStyleHandler {
    @Override
    public CellStyle getTitleCellStyle() {
        return null;
    }

    @Override
    public CellStyle getSecondTitleCellStyle() {
        return null;
    }

    @Override
    public CellStyle getHeaderCellStyle(String title, int cellIndex) {
        return null;
    }

    @Override
    public CellStyle getDataCellStyle(int rowIndex, Object value, Class<?> valueClass, ExcelCellHeaderConfig excelCellHeaderConfig) {
        return null;
    }

    @Override
    public CellStyle getStatisticsCellStyle(int order) {
        return null;
    }
}
