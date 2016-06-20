package com.mzlion.poi.handler;

import com.mzlion.poi.config.ExcelCellHeaderConfig;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * Created by mzlion on 2016/6/10.
 */
public interface ExcelCellStyleHandler {

    CellStyle getTitleCellStyle();

    CellStyle getSecondTitleCellStyle();

    CellStyle getHeaderCellStyle(String title, int cellIndex);

    CellStyle getDataCellStyle(int rowIndex, Object value, Class<?> valueClass, ExcelCellHeaderConfig excelCellHeaderConfig, CellStyle cloneCellStyle);

    CellStyle getStatisticsCellStyle(int order);


}
