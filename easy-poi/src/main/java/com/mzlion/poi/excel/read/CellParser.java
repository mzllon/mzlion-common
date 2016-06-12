package com.mzlion.poi.excel.read;

import com.mzlion.core.beans.PropertyUtilBean;
import com.mzlion.core.date.DateUtils;
import com.mzlion.core.lang.DigitalUtils;
import com.mzlion.core.lang.StringUtils;
import com.mzlion.poi.beans.BeanPropertyCellDescriptor;
import com.mzlion.poi.exception.ExcelCellProcessException;
import com.mzlion.poi.exception.ExcelDateFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * Excel的Cell解析：主要解析cell的值并且赋值给JavaBean
 * </p>
 *
 * @author mzlion
 */
public class CellParser<E> {

    //slf4j
    private Logger logger = LoggerFactory.getLogger(CellParser.class);

    //公式处理
    private FormulaEvaluator evaluator;

    private BeanPropertyCellDescriptor beanPropertyCellDescriptor;

    private E entity;

    public CellParser(E entity, BeanPropertyCellDescriptor beanPropertyCellDescriptor, FormulaEvaluator evaluator) {
        this.beanPropertyCellDescriptor = beanPropertyCellDescriptor;
        this.entity = entity;
        this.evaluator = evaluator;
    }

    public void process(Cell cell) {
        if (cell == null) {
            logger.debug(" ===> Cell is empty,process finish.");
            return;
        }
        int rowIndex = cell.getRowIndex() + 1;
        int cellIndex = beanPropertyCellDescriptor.getCellIndex() + 1;
        Object cellValue = this.getCellValue(cell);
        logger.debug(" ===> Processing cell at [{},{}],the content is {}", rowIndex, cellIndex, cellValue);
        if (cellValue == null) {
            logger.debug(" ===> Cell content is empty,process finish.");
            return;
        }

        PropertyDescriptor propertyDescriptor = PropertyUtilBean.getInstance().getPropertyDescriptor(entity.getClass(), beanPropertyCellDescriptor.getPropertyName());
        Class<?> propertyTypeClass = propertyDescriptor.getPropertyType();
        try {
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (Date.class.equals(propertyTypeClass)) {
                if (cellValue instanceof Date) {
                    writeMethod.invoke(this.entity, cellValue);
                } else if (cellValue instanceof String) {
                    if (StringUtils.hasLength(this.beanPropertyCellDescriptor.getExcelDateFormat())) {
                        writeMethod.invoke(this.entity, DateUtils.parseDate((String) cellValue, this.beanPropertyCellDescriptor.getExcelDateFormat()));
                    } else {
                        throw new ExcelDateFormatException(String.format("The cell header [%s], mapped cell value [%s] need convert java.util.Date at coordinate [%d,%d],but 'dateFormat is empty."
                                , beanPropertyCellDescriptor.getTitle(), cellValue, rowIndex, cellIndex));
                    }
                } else {
                    logger.warn(" ===> The cell value can not cast java.util.Date instance->{}", cellValue);
                }
            } else if (boolean.class.equals(propertyTypeClass) || Boolean.class.equals(propertyTypeClass)) {
                if (cellValue instanceof Boolean) {
                    writeMethod.invoke(this.entity, cellValue);
                } else if (cellValue instanceof String) {
                    writeMethod.invoke(this.entity, Boolean.parseBoolean((String) cellValue));
                } else {
                    logger.warn(" ===> The cell value can not cast java.lang.Boolean instance->{}", cellValue);
                }
            } else if (byte.class.equals(propertyTypeClass) || Byte.class.equals(propertyTypeClass)) {
                //ignore
            } else if (char.class.equals(propertyTypeClass) || Character.class.equals(propertyTypeClass)) {
                //ignore
            } else if (int.class.equals(propertyTypeClass) || Integer.class.equals(propertyTypeClass)) {
                writeMethod.invoke(this.entity, Integer.valueOf(String.valueOf(cellValue)));
            } else if (float.class.equals(propertyTypeClass) || Float.class.equals(propertyTypeClass)) {
                writeMethod.invoke(this.entity, Float.valueOf(String.valueOf(cellValue)));
            } else if (double.class.equals(propertyTypeClass) || Double.class.equals(propertyTypeClass)) {
                writeMethod.invoke(this.entity, Double.valueOf(String.valueOf(cellValue)));
            } else if (long.class.equals(propertyTypeClass) || Long.class.equals(propertyTypeClass)) {
                writeMethod.invoke(this.entity, Long.valueOf(String.valueOf(cellValue)));
            } else if (short.class.equals(propertyTypeClass) || Short.class.equals(propertyTypeClass)) {
                writeMethod.invoke(this.entity, Short.valueOf(String.valueOf(cellValue)));
            } else if (BigDecimal.class.equals(propertyTypeClass)) {
                writeMethod.invoke(this.entity, new BigDecimal(String.valueOf(cellValue)));
            } else if (String.class.equals(propertyTypeClass)) {
                if (cellValue instanceof String) {
                    writeMethod.invoke(this.entity, cellValue);
                } else if (cellValue instanceof Double) {
                    writeMethod.invoke(this.entity, DigitalUtils.avoidScientificNotation(String.valueOf(cellValue)));
                } else {
                    writeMethod.invoke(this.entity, String.valueOf(cellValue));
                }
            } else {
                writeMethod.invoke(this.entity, cellValue);
            }
        } catch (ReflectiveOperationException e) {
            throw new ExcelCellProcessException(e);
        }
    }

    /**
     * 解析单元格的值
     *
     * @return 返回单元格的值
     */
    private Object getCellValue(Cell cell) {
        CellValue cellValue = this.evaluator.evaluate(cell);
        switch (cellValue.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                return cellValue.getBooleanValue();
            case Cell.CELL_TYPE_ERROR:
                break;
            //CELL_TYPE_FORMULA will never happen
            case Cell.CELL_TYPE_FORMULA:
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return DateUtil.getJavaDate(cellValue.getNumberValue());
                }
                return cellValue.getNumberValue();
            case Cell.CELL_TYPE_STRING:
                return cellValue.getStringValue();
        }
        return null;
    }
}
