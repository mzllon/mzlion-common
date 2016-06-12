package com.mzlion.poi.excel.read;

import com.mzlion.poi.beans.BeanPropertyCellDescriptor;
import com.mzlion.poi.config.ExcelReadConfig;
import com.mzlion.poi.exception.BeanNewInstanceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

/**
 * Created by mzlion on 2016/6/9.
 */
public class DataRowParser<E> {
    private final ExcelReadConfig<E> excelReadConfig;
    private final List<BeanPropertyCellDescriptor> beanPropertyCellDescriptorList;
    private final FormulaEvaluator formulaEvaluator;

    public DataRowParser(ExcelReadConfig<E> excelReadConfig, List<BeanPropertyCellDescriptor> beanPropertyCellDescriptorList,
                         FormulaEvaluator formulaEvaluator) {
        this.excelReadConfig = excelReadConfig;
        this.beanPropertyCellDescriptorList = beanPropertyCellDescriptorList;
        this.formulaEvaluator = formulaEvaluator;
    }

    public E process(Row row) {
        try {
            E entity = this.excelReadConfig.getBeanClass().newInstance();
            for (BeanPropertyCellDescriptor beanPropertyCellDescriptor : this.beanPropertyCellDescriptorList) {
                Cell cell = row.getCell(beanPropertyCellDescriptor.getCellIndex());
                CellParser<E> cellParser = new CellParser<>(entity, beanPropertyCellDescriptor, this.formulaEvaluator);
                cellParser.process(cell);
            }
            return entity;
        } catch (ReflectiveOperationException e) {
            throw new BeanNewInstanceException(this.excelReadConfig.getBeanClass(), e);
        }
    }


}
