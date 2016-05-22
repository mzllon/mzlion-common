package com.mzlion.core.beans;

import com.mzlion.core.vo.Person;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * Created by mzlion on 2016/5/22.
 */
public class PropertyUtilBeanTest {
    @Test
    public void getInstance() throws Exception {

    }

    @Test
    public void getPropertyDescriptors() throws Exception {

    }

    @Test
    public void getPropertyDescriptors1() throws Exception {
        List<PropertyDescriptor> propertyDescriptors = PropertyUtilBean.getInstance().getPropertyDescriptors(Person.class);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            System.out.println(propertyDescriptor);
        }
    }

    @Test
    public void clearDescriptors() throws Exception {

    }

}