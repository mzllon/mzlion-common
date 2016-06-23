package com.mzlion.poi.style;

import com.mzlion.poi.config.ExcelCellHeaderConfig;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * Excel单元格样式处理器
 *
 * @author mzlion
 * @date 2016-06-20
 */
public interface ExcelCellStyleHandler {

    /**
     * 获取总标题的样式
     */
    CellStyle getTitleCellStyle();

    /**
     * 获取二级标题的样式
     */
    CellStyle getSecondTitleCellStyle();

    /**
     * 获取标题头的样式
     *
     * @param title     当前标题名称
     * @param cellIndex 所在位置
     * @param cellStyle 框架默认的样式
     * @return 返回样式
     */
    CellStyle getHeaderCellStyle(String title, int cellIndex, CellStyle cellStyle);

    /**
     * 获取数据单元格的样式
     *
     * @param rowIndex              当前行
     * @param value                 填充的值
     * @param valueClass            填充值的类型
     * @param excelCellHeaderConfig 单元格的配置属性
     * @param cellStyle             框架默认的样式
     * @return 返回样式
     */
    CellStyle getDataCellStyle(int rowIndex, Object originalBean, Object value, Class<?> valueClass, ExcelCellHeaderConfig excelCellHeaderConfig, CellStyle cellStyle);

    CellStyle getStatisticsCellStyle(int order);


}
