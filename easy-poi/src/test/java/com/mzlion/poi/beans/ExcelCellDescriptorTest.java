package com.mzlion.poi.beans;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mzlion on 2016/6/8.
 */
public class ExcelCellDescriptorTest {

    @Test
    public void sort() throws Exception {
        List<ExcelCellDescriptor> data = new ArrayList<>();
        ExcelCellDescriptor excelCellDescriptor;

        excelCellDescriptor = new ExcelCellDescriptor();
        excelCellDescriptor.setTitle("title3");
        excelCellDescriptor.setPropertyName("prop3");
        excelCellDescriptor.setRequired(false);
        data.add(excelCellDescriptor);

        excelCellDescriptor = new ExcelCellDescriptor();
        excelCellDescriptor.setTitle("title1");
        excelCellDescriptor.setPropertyName("prop1");
        excelCellDescriptor.setRequired(true);
        data.add(excelCellDescriptor);

        excelCellDescriptor = new ExcelCellDescriptor();
        excelCellDescriptor.setTitle("title2");
        excelCellDescriptor.setPropertyName("prop2");
        excelCellDescriptor.setRequired(false);
        data.add(excelCellDescriptor);

        System.out.println(data);
    }

}