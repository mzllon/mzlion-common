package com.mzlion.poi.excel.write;

import com.mzlion.core.beans.PropertyUtilBean;
import com.mzlion.core.lang.StringUtils;
import com.mzlion.poi.beans.PropertyCellMapping;
import com.mzlion.poi.exception.BeanPropertyReflectException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

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

    private ExcelWriterEngine excelWriterEngine;

    public CellGenerator(ExcelWriterEngine excelWriterEngine) {
        this.excelWriterEngine = excelWriterEngine;
    }

    @SuppressWarnings("unchecked")
    public Cell generate(Row row, E entity, PropertyCellMapping propertyCellMapping) {
        Cell cell = row.createCell(propertyCellMapping.getCellIndex());
        String propertyName = propertyCellMapping.getPropertyName();
        Object value;
        Class<?> returnType;
        if (entity instanceof Map) {
            Map<String, Object> entityMap = (Map<String, Object>) entity;
            value = entityMap.get(propertyName);
            returnType = value.getClass();
        } else {
            PropertyDescriptor propertyDescriptor = PropertyUtilBean.getInstance().getPropertyDescriptor(entity, propertyName);
            Method readMethod = propertyDescriptor.getReadMethod();
            returnType = readMethod.getReturnType();
            try {
                value = readMethod.invoke(entity);
            } catch (ReflectiveOperationException e) {
                throw new BeanPropertyReflectException("The entity [" + entity.getClass().getName() + "] of property [" + propertyCellMapping.getPropertyName() + "] can not reflect.", e);
            }
        }
        if (value == null) {
            cell.setCellType(Cell.CELL_TYPE_BLANK);
            return cell;
        }
        if (this.excelWriterEngine.excelCellStyle != null)
            cell.setCellStyle(this.excelWriterEngine.excelCellStyle.getDataCellStyle(row.getRowNum(), value, returnType, propertyCellMapping));

        switch (propertyCellMapping.getType()) {
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
                    cell.getCellStyle().setDataFormat(this.excelWriterEngine.workbook.getCreationHelper().createDataFormat().getFormat("@"));
                } else if (returnType.equals(Date.class)) {
                    if (StringUtils.hasLength(propertyCellMapping.getExcelDateFormat())) {
                        cell.getCellStyle().setDataFormat(this.excelWriterEngine.workbook.getCreationHelper().createDataFormat()
                                .getFormat(propertyCellMapping.getExcelDateFormat()));
                        cell.setCellValue((Date) value);
                    } else {
                        cell.setCellValue((Date) value);
                    }
                } else {
                    cell.setCellValue(value.toString());
                }
                break;
            case HYPER_LINK:
                if (returnType.equals(String.class)) {
                    String link = (String) value;
                    cell.setCellValue(link);
                    Hyperlink hyperlink = this.excelWriterEngine.workbook.getCreationHelper().createHyperlink(propertyCellMapping.getExcelHyperLinkType().getType());
                    hyperlink.setAddress(link);
                    hyperlink.setLabel(StringUtils.isEmpty(propertyCellMapping.getHyperlinkName()) ? link : propertyCellMapping.getHyperlinkName());
                    cell.setHyperlink(hyperlink);
                } else {
                    logger.warn(" ===> This value [propertyName={},value={}] convert to String.class failed.", propertyName, value);
                }
                break;
            case FORMULA:
                break;
            default:
                cell.setCellType(Cell.CELL_TYPE_BLANK);
                break;
        }

        return cell;
    }
}
