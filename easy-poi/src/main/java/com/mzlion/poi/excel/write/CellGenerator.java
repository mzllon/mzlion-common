package com.mzlion.poi.excel.write;

import com.mzlion.core.beans.PropertyUtilBean;
import com.mzlion.core.lang.StringUtils;
import com.mzlion.poi.beans.BeanPropertyCellDescriptor;
import com.mzlion.poi.exception.BeanPropertyReflectException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Excel的cell生成
 *
 * @param <E> 泛型类型
 * @author mzlion
 * @date 2016-06-12
 */
public class CellGenerator<E> {
    private final Logger logger = LoggerFactory.getLogger(CellGenerator.class);
//    private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new IdentityHashMap<>(8);
//
//    static {
//        primitiveTypeToWrapperMap.put(boolean.class, Boolean.class);
//        primitiveTypeToWrapperMap.put(byte.class, Byte.class);
//        primitiveTypeToWrapperMap.put(char.class, Character.class);
//        primitiveTypeToWrapperMap.put(double.class, Double.class);
//        primitiveTypeToWrapperMap.put(float.class, Float.class);
//        primitiveTypeToWrapperMap.put(int.class, Integer.class);
//        primitiveTypeToWrapperMap.put(long.class, Long.class);
//        primitiveTypeToWrapperMap.put(short.class, Short.class);
//    }

    /**
     * excel row
     */
    private final Row row;

    /**
     * the data
     */
    private final E entity;

    /**
     * the cell of config
     */
    private final BeanPropertyCellDescriptor beanPropertyCellDescriptor;

    /**
     * excel cell style
     */
    private final ExcelCellStyle excelCellStyle;

    private Workbook workbook;

    public CellGenerator(Row row, E entity, BeanPropertyCellDescriptor beanPropertyCellDescriptor, ExcelCellStyle excelCellStyle) {
        this.row = row;
        this.entity = entity;
        this.beanPropertyCellDescriptor = beanPropertyCellDescriptor;
        this.excelCellStyle = excelCellStyle;
        this.workbook = this.row.getSheet().getWorkbook();
    }

    public Cell generate() {
        Cell cell = this.row.createCell(this.beanPropertyCellDescriptor.getCellIndex());
        if (this.excelCellStyle != null)
            cell.setCellStyle(this.excelCellStyle.getDataCellStyle(row.getRowNum(), entity, beanPropertyCellDescriptor));
        String propertyName = beanPropertyCellDescriptor.getPropertyName();
        PropertyDescriptor propertyDescriptor = PropertyUtilBean.getInstance().getPropertyDescriptor(entity, propertyName);
        Method readMethod = propertyDescriptor.getReadMethod();
        try {
            Object value = readMethod.invoke(entity);
            if (value == null) {
                cell.setCellType(Cell.CELL_TYPE_BLANK);
                return cell;
            }
            Class<?> returnType = readMethod.getReturnType();
            switch (beanPropertyCellDescriptor.getType()) {
                case AUTO:
                    if (returnType.equals(int.class) || returnType.equals(Integer.class) ||
                            returnType.equals(float.class) || returnType.equals(Float.class) ||
                            returnType.equals(double.class) || returnType.equals(Double.class) ||
                            returnType.equals(long.class) || returnType.equals(Long.class) ||
                            returnType.equals(short.class) || returnType.equals(Short.class) ||
                            returnType.equals(BigDecimal.class)) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        Number number = (Number) value;
                        cell.setCellValue(number.doubleValue());
                    } else if (returnType.equals(boolean.class) || returnType.equals(Boolean.class)) {
                        cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                        cell.setCellValue((Boolean) value);
                    } else if (returnType.equals(String.class)) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(value.toString());
                        cell.getCellStyle().setDataFormat(this.workbook.getCreationHelper().createDataFormat().getFormat("@"));
                    } else if (returnType.equals(Date.class)) {
                        if (StringUtils.hasLength(this.beanPropertyCellDescriptor.getExcelDateFormat())) {
                            cell.getCellStyle().setDataFormat(this.workbook.getCreationHelper().createDataFormat()
                                    .getFormat(beanPropertyCellDescriptor.getExcelDateFormat()));
                            cell.setCellValue((Date) value);
                        } else {
                            cell.setCellValue((Date) value);
                        }
                    } else {
                        cell.setCellValue(value.toString());
                    }
                    break;
                case HYPER_LINK:
                    break;
                case PICTURE:
                    break;
                case FORMULA:
                    break;
                default:
                    cell.setCellType(Cell.CELL_TYPE_BLANK);
                    break;
            }
        } catch (ReflectiveOperationException e) {
            throw new BeanPropertyReflectException("The entity [" + entity.getClass().getName() + "] of property [" + beanPropertyCellDescriptor.getPropertyName() + "] can not reflect.", e);
        }
        return cell;
    }
}
