package com.mzlion.poi.excel.read;

import com.mzlion.poi.beans.PropertyCellMapping;
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
    private final List<PropertyCellMapping> propertyCellMappingList;
    private final FormulaEvaluator formulaEvaluator;

    public DataRowParser(ExcelReadConfig<E> excelReadConfig, List<PropertyCellMapping> propertyCellMappingList,
                         FormulaEvaluator formulaEvaluator) {
        this.excelReadConfig = excelReadConfig;
        this.propertyCellMappingList = propertyCellMappingList;
        this.formulaEvaluator = formulaEvaluator;
    }

    public E process(Row row) {
        try {
            E entity = this.excelReadConfig.getBeanClass().newInstance();
            for (PropertyCellMapping propertyCellMapping : this.propertyCellMappingList) {
                Cell cell = row.getCell(propertyCellMapping.getCellIndex());
                CellParser<E> cellParser = new CellParser<>(entity, propertyCellMapping, this.formulaEvaluator);
                cellParser.process(cell);
            }
            return entity;
        } catch (ReflectiveOperationException e) {
            throw new BeanNewInstanceException(this.excelReadConfig.getBeanClass(), e);
        }
    }


}
