package com.mzlion.poi.excel;

import com.mzlion.poi.config.ExcelCellConfig;
import com.mzlion.poi.exception.BeanNewInstanceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * Created by mzlion on 2016/6/9.
 */
class DataRowParser<T> {
    private final ExcelReaderEngine<T> excelReaderEngine;

    DataRowParser(ExcelReaderEngine<T> excelReaderEngine) {
        this.excelReaderEngine = excelReaderEngine;
    }

    T process(Row row) {
        try {
            Object entity = this.excelReaderEngine.excelReadConfig.getRawClass().newInstance();
            for (ExcelCellConfig excelCellConfig : this.excelReaderEngine.excelCellConfigs) {
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
