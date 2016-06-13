package com.mzlion.poi.excel.write;

import com.mzlion.poi.beans.PropertyCellMapping;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * Created by mzlion on 2016/6/10.
 */
public interface ExcelCellStyle {

    CellStyle getTitleCellStyle();

    CellStyle getSecondTitleCellStyle();

    CellStyle getHeaderCellStyle(String title, int cellIndex);

    CellStyle getDataCellStyle(int rowIndex, Object value, Class<?> valueClass, PropertyCellMapping propertyCellMapping);

    CellStyle getStatisticsCellStyle(int order);


}
