package com.mzlion.poi.beans;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mzlion on 2016/6/8.
 */
public class CellDescriptorTest {

    @Test
    public void sort() throws Exception {
        List<CellDescriptor> data = new ArrayList<>();
        CellDescriptor cellDescriptor;

        cellDescriptor = new CellDescriptor();
        cellDescriptor.setTitle("title3");
        cellDescriptor.setPropertyName("prop3");
        cellDescriptor.setRequired(false);
        data.add(cellDescriptor);

        cellDescriptor = new CellDescriptor();
        cellDescriptor.setTitle("title1");
        cellDescriptor.setPropertyName("prop1");
        cellDescriptor.setRequired(true);
        data.add(cellDescriptor);

        cellDescriptor = new CellDescriptor();
        cellDescriptor.setTitle("title2");
        cellDescriptor.setPropertyName("prop2");
        cellDescriptor.setRequired(false);
        data.add(cellDescriptor);

        System.out.println(data);
    }

}