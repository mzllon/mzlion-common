package com.mzlion.poi.beans;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mzlion on 2016/6/8.
 */
public class PropertyCellMappingTest {

    @Test
    public void sort() throws Exception {
        List<PropertyCellMapping> data = new ArrayList<>();
        PropertyCellMapping propertyCellMapping;

        propertyCellMapping = new PropertyCellMapping();
        propertyCellMapping.setTitle("title3");
        propertyCellMapping.setPropertyName("prop3");
        propertyCellMapping.setRequired(false);
        data.add(propertyCellMapping);

        propertyCellMapping = new PropertyCellMapping();
        propertyCellMapping.setTitle("title1");
        propertyCellMapping.setPropertyName("prop1");
        propertyCellMapping.setRequired(true);
        data.add(propertyCellMapping);

        propertyCellMapping = new PropertyCellMapping();
        propertyCellMapping.setTitle("title2");
        propertyCellMapping.setPropertyName("prop2");
        propertyCellMapping.setRequired(false);
        data.add(propertyCellMapping);

        System.out.println(data);
    }

}