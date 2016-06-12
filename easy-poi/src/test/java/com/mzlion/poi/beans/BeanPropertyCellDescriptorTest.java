package com.mzlion.poi.beans;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mzlion on 2016/6/8.
 */
public class BeanPropertyCellDescriptorTest {

    @Test
    public void sort() throws Exception {
        List<BeanPropertyCellDescriptor> data = new ArrayList<>();
        BeanPropertyCellDescriptor beanPropertyCellDescriptor;

        beanPropertyCellDescriptor = new BeanPropertyCellDescriptor();
        beanPropertyCellDescriptor.setTitle("title3");
        beanPropertyCellDescriptor.setPropertyName("prop3");
        beanPropertyCellDescriptor.setRequired(false);
        data.add(beanPropertyCellDescriptor);

        beanPropertyCellDescriptor = new BeanPropertyCellDescriptor();
        beanPropertyCellDescriptor.setTitle("title1");
        beanPropertyCellDescriptor.setPropertyName("prop1");
        beanPropertyCellDescriptor.setRequired(true);
        data.add(beanPropertyCellDescriptor);

        beanPropertyCellDescriptor = new BeanPropertyCellDescriptor();
        beanPropertyCellDescriptor.setTitle("title2");
        beanPropertyCellDescriptor.setPropertyName("prop2");
        beanPropertyCellDescriptor.setRequired(false);
        data.add(beanPropertyCellDescriptor);

        System.out.println(data);
    }

}