package com.mzlion.poi.style;

import com.mzlion.poi.config.ExcelCellHeaderConfig;
import com.mzlion.poi.constant.ExcelCellType;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 框架默认实现的样式风格
 */
public class DefaultExcelCellStyleHandler implements ExcelCellStyleHandler {

    private Workbook workbook;

    public DefaultExcelCellStyleHandler(Workbook workbook) {
        this.workbook = workbook;
    }

    /**
     * 获取总标题的样式
     */
    @Override
    public CellStyle getTitleCellStyle() {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);

        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        font.setFontName("宋体");
        cellStyle.setFont(font);

        return cellStyle;
    }

    /**
     * 获取二级标题的样式
     */
    @Override
    public CellStyle getSecondTitleCellStyle() {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);

        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);

        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setFontName("宋体");
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 获取标题头的样式
     *
     * @param title     当前标题名称
     * @param cellIndex 所在位置
     * @param cellStyle 框架默认的样式
     * @return 返回样式
     */
    @Override
    public CellStyle getHeaderCellStyle(String title, int cellIndex, CellStyle cellStyle) {
        cellStyle = this.workbook.createCellStyle();
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);

        Font font = this.workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setFontName("宋体");
        cellStyle.setFont(font);
        return cellStyle;
    }

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
    @Override
    public CellStyle getDataCellStyle(int rowIndex, Object value, Class<?> valueClass, ExcelCellHeaderConfig excelCellHeaderConfig, CellStyle cellStyle) {
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);

        cellStyle.setWrapText(excelCellHeaderConfig.isAutoWrap());

        if (excelCellHeaderConfig.getExcelCellType().equals(ExcelCellType.HYPER_LINK)) {
            if (valueClass.equals(String.class)) {
                Font hyperlinkFont = this.workbook.createFont();
                hyperlinkFont.setUnderline(Font.U_SINGLE);
                hyperlinkFont.setColor(HSSFColor.BLUE.index);
                cellStyle.setFont(hyperlinkFont);
            }
        } else {
            Font font = this.workbook.createFont();
            font.setFontHeightInPoints((short) 10);
            font.setFontName("宋体");
            cellStyle.setFont(font);
        }

        return cellStyle;
    }

    @Override
    public CellStyle getStatisticsCellStyle(int order) {
        return null;
    }
}
