package com.mzlion.poi.excel;

import com.mzlion.core.beans.PropertyUtilBean;
import com.mzlion.core.lang.ClassUtils;
import com.mzlion.core.lang.CollectionUtils;
import com.mzlion.core.lang.StringUtils;
import com.mzlion.poi.beans.PropertyCellMapping;
import com.mzlion.poi.exception.BeanPropertyReflectException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * Excel的cell生成
 *
 * @author mzlion
 * @date 2016-06-12
 */
class ExcelCellGenerator {
    private final Logger logger = LoggerFactory.getLogger(ExcelCellGenerator.class);


    private ExcelWriterEngine excelWriterEngine;

    ExcelCellGenerator(ExcelWriterEngine excelWriterEngine) {
        this.excelWriterEngine = excelWriterEngine;
    }

    @SuppressWarnings("unchecked")
    Cell generate(Sheet sheet, Row row, Object entity, PropertyCellMapping propertyCellMapping) {
        Cell cell = row.createCell(propertyCellMapping.getCellIndex());
        String propertyName = propertyCellMapping.getPropertyName();
        Object value;
        if (entity instanceof Map) {
            Map<String, Object> entityMap = (Map<String, Object>) entity;
            value = entityMap.get(propertyName);
            if (value == null) {
                cell.setCellType(Cell.CELL_TYPE_BLANK);
            } else
                this.setCell(cell, value, value.getClass(), propertyCellMapping);
            if (this.excelWriterEngine.excelCellStyle != null)
                cell.setCellStyle(this.excelWriterEngine.excelCellStyle.getDataCellStyle(cell.getRowIndex(), value, value == null ? null : value.getClass(), propertyCellMapping));
            return cell;

        } else {
            PropertyDescriptor propertyDescriptor = PropertyUtilBean.getInstance().getPropertyDescriptor(entity, propertyName);
            Method readMethod = propertyDescriptor.getReadMethod();
            try {
                value = readMethod.invoke(entity);
            } catch (ReflectiveOperationException e) {
                throw new BeanPropertyReflectException("The entity [" + entity.getClass().getName() + "] of property [" + propertyCellMapping.getPropertyName() + "] can not reflect.", e);
            }
            if (value == null) {
                cell.setCellType(Cell.CELL_TYPE_BLANK);
                if (this.excelWriterEngine.excelCellStyle != null)
                    cell.setCellStyle(this.excelWriterEngine.excelCellStyle.getDataCellStyle(cell.getRowIndex(), null, null, propertyCellMapping));
                return cell;
            }
            if (CollectionUtils.isEmpty(propertyCellMapping.getChildrenMapping())) {
                this.setCell(cell, value, readMethod.getReturnType(), propertyCellMapping);
                if (this.excelWriterEngine.excelCellStyle != null)
                    cell.setCellStyle(this.excelWriterEngine.excelCellStyle.getDataCellStyle(cell.getRowIndex(), value, readMethod.getReturnType(), propertyCellMapping));
                return cell;
            }

            row.removeCell(cell);
            if (ClassUtils.isAssignable(Collection.class, propertyDescriptor.getPropertyType())) {
                //需要创建rows
                this.excelWriterEngine.createDataRows(sheet, row.getRowNum(), (Collection<?>) value, propertyCellMapping.getChildrenMapping());
            } else {
                this.excelWriterEngine.createDataRows(sheet, row.getRowNum(), Collections.singletonList(value), propertyCellMapping.getChildrenMapping());
            }

        }
        return cell;
    }

    private void setCell(Cell cell, Object value, Class<?> returnType, PropertyCellMapping propertyCellMapping) {
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
                    logger.warn(" ===> This value [propertyName={},value={}] convert to String.class failed.", propertyCellMapping.getPropertyName(), value);
                }
                break;
            case FORMULA:
                break;
            default:
                cell.setCellType(Cell.CELL_TYPE_BLANK);
                break;
        }

    }
}
