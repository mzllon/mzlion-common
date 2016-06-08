package com.mzlion.poi.excel.imports;

import com.mzlion.core.beans.PropertyUtilBean;
import com.mzlion.core.date.DateUtils;
import com.mzlion.core.lang.DigitalUtils;
import com.mzlion.core.lang.StringUtils;
import com.mzlion.poi.beans.CellDescriptor;
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
 * Created by mzlion on 2016/6/8.
 */
public class CellParser<T> {

    private Logger logger = LoggerFactory.getLogger(CellParser.class);

    private FormulaEvaluator evaluator;

    private int rowIndex;

    private Cell cell;

    private CellDescriptor cellDescriptor;

    private T instance;

    public CellParser(int rowIndex, Cell cell, CellDescriptor cellDescriptor, T instance, FormulaEvaluator evaluator) {
        this.rowIndex = rowIndex;
        this.cell = cell;
        this.cellDescriptor = cellDescriptor;
        this.instance = instance;
        this.evaluator = evaluator;
    }

    public void process() {
        logger.debug(" ===> Starting process cell at [{},{}]", rowIndex + 1, cellDescriptor.getCellIndex() + 1);
        if (cell == null) {
            return;
        }
        PropertyDescriptor propertyDescriptor = PropertyUtilBean.getInstance().getPropertyDescriptor(instance.getClass(), cellDescriptor.getPropertyName());
        Class<?> propertyTypeClass = propertyDescriptor.getPropertyType();
        Object cellValue = this.getCellValue();
        if (cellValue == null) {
            return;
        }
        try {
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (Date.class.equals(propertyTypeClass)) {
                if (cellValue instanceof Date) {
                    writeMethod.invoke(this.instance, cellValue);
                } else if (cellValue instanceof String) {
                    if (StringUtils.hasLength(this.cellDescriptor.getExcelDateFormat())) {
                        writeMethod.invoke(this.instance, DateUtils.parseDate((String) cellValue, this.cellDescriptor.getExcelDateFormat()));
                    } else {
                        throw new ExcelDateFormatException();
                    }
                } else {
                    logger.warn(" ===> The cell value can not cast java.util.Date instance->{}", cellValue);
                }
            } else if (boolean.class.equals(propertyTypeClass) || Boolean.class.equals(propertyTypeClass)) {
                if (cellValue instanceof Boolean) {
                    writeMethod.invoke(this.instance, cellValue);
                } else if (cellValue instanceof String) {
                    writeMethod.invoke(this.instance, Boolean.parseBoolean((String) cellValue));
                } else {
                    logger.warn(" ===> The cell value can not cast java.lang.Boolean instance->{}", cellValue);
                }
            } else if (byte.class.equals(propertyTypeClass) || Byte.class.equals(propertyTypeClass)) {
                //ignore
            } else if (char.class.equals(propertyTypeClass) || Character.class.equals(propertyTypeClass)) {
                //ignore
            } else if (int.class.equals(propertyTypeClass) || Integer.class.equals(propertyTypeClass)) {
                writeMethod.invoke(this.instance, Integer.valueOf(String.valueOf(cellValue)));
            } else if (float.class.equals(propertyTypeClass) || Float.class.equals(propertyTypeClass)) {
                writeMethod.invoke(this.instance, Float.valueOf(String.valueOf(cellValue)));
            } else if (double.class.equals(propertyTypeClass) || Double.class.equals(propertyTypeClass)) {
                writeMethod.invoke(this.instance, Double.valueOf(String.valueOf(cellValue)));
            } else if (long.class.equals(propertyTypeClass) || Long.class.equals(propertyTypeClass)) {
                writeMethod.invoke(this.instance, Long.valueOf(String.valueOf(cellValue)));
            } else if (short.class.equals(propertyTypeClass) || Short.class.equals(propertyTypeClass)) {
                writeMethod.invoke(this.instance, Short.valueOf(String.valueOf(cellValue)));
            } else if (BigDecimal.class.equals(propertyTypeClass)) {
                writeMethod.invoke(this.instance, new BigDecimal(String.valueOf(cellValue)));
            } else if (String.class.equals(propertyTypeClass)) {
                if (cellValue instanceof String) {
                    writeMethod.invoke(this.instance, cellValue);
                } else if (cellValue instanceof Double) {
                    writeMethod.invoke(this.instance, DigitalUtils.avoidScientificNotation(String.valueOf(cellValue)));
                } else {
                    writeMethod.invoke(this.instance, String.valueOf(cellValue));
                }
            } else {
                writeMethod.invoke(this.instance, cellValue);
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            throw new ExcelCellProcessException(e);
        }
    }

    /**
     * 解析单元格的值
     *
     * @return 返回单元格的值
     */
    private Object getCellValue() {
        CellValue cellValue = this.evaluator.evaluate(this.cell);
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
                if (DateUtil.isCellDateFormatted(this.cell)) {
                    return DateUtil.getJavaDate(cellValue.getNumberValue());
                }
                return cellValue.getNumberValue();
            case Cell.CELL_TYPE_STRING:
                return cellValue.getStringValue();
        }
        return null;
    }
}
