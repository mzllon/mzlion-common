package com.mzlion.poi.excel;

import com.mzlion.core.beans.PropertyUtilBean;
import com.mzlion.core.lang.ClassUtils;
import com.mzlion.core.lang.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by mzlion on 2016/6/20.
 */
class DataRowWriter<E> {

    private final WriteExcelEngine writeExcelEngine;

    DataRowWriter(WriteExcelEngine writeExcelEngine) {
        this.writeExcelEngine = writeExcelEngine;
    }


    void write(Collection<E> dataSet, Sheet sheet, int startRowIndex) {
        if (ClassUtils.isAssignable(Map.class, this.writeExcelEngine.writeExcelConfig.getRawClass())) {
//            this.doWriteForMap((Collection<Map<String, Object>>) dataSet, sheet, startRowIndex)
        } else {
            this.doWriteForBean(dataSet, sheet, startRowIndex);
        }
    }

    private void doWriteForBean(Collection<E> dataSet, Sheet sheet, int startRowIndex) {
        int index = 0;
        for (E entity : dataSet) {
            Row row = sheet.createRow(startRowIndex + index);
            int maxMerRowCount = 0;
            List<Collection<Object>> childrenDataList = new ArrayList<>();
            List<Cell> mergeCellList = new ArrayList<>();
            for (WriteExcelCellHeaderConfig writeExcelCellHeaderConfig : this.writeExcelEngine.writeExcelCellHeaderConfigList) {
                List<WriteExcelCellHeaderConfig> children = writeExcelCellHeaderConfig.children;
                if (CollectionUtils.isNotEmpty(children)) {

                } else {
                    PropertyDescriptor propertyDescriptor = PropertyUtilBean.getInstance().getPropertyDescriptor(entity, writeExcelCellHeaderConfig.propertyName);
                    Method readMethod = propertyDescriptor.getReadMethod();
                    Object value = readMethod.invoke(entity);
                    Cell cell = row.createCell(writeExcelCellHeaderConfig.cellIndex);
                    if (this.writeExcelEngine.styleHandler != null)
                        cell.setCellStyle(this.writeExcelEngine.styleHandler.
                                getDataCellStyle(cell.getRowIndex(), value, readMethod.getReturnType(), null));
                }
            }
        }
    }
}
