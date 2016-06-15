package com.mzlion.poi.excel;

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
class DataRowParser {
    private final ExcelReadConfig excelReadConfig;
    private final List<PropertyCellMapping> propertyCellMappingList;
    private final FormulaEvaluator formulaEvaluator;

    DataRowParser(ExcelReadConfig excelReadConfig, List<PropertyCellMapping> propertyCellMappingList,
                  FormulaEvaluator formulaEvaluator) {
        this.excelReadConfig = excelReadConfig;
        this.propertyCellMappingList = propertyCellMappingList;
        this.formulaEvaluator = formulaEvaluator;
    }

    Object process(Row row) {
        try {
            Object entity = this.excelReadConfig.getRawClass().newInstance();
            for (PropertyCellMapping propertyCellMapping : this.propertyCellMappingList) {
                Cell cell = row.getCell(propertyCellMapping.getCellIndex());
                CellParser cellParser = new CellParser(entity, propertyCellMapping, this.formulaEvaluator);
                cellParser.process(cell);
            }
            return entity;
        } catch (ReflectiveOperationException e) {
            throw new BeanNewInstanceException(this.excelReadConfig.getRawClass(), e);
        }
    }


}
