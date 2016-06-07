package com.mzlion.core.beans;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * <p>
 * Javabean的内省缓存封装对象
 * </p>
 * Created by mzlion on 2016/5/22.
 */
public class BeanIntrospectionData {

    private final List<PropertyDescriptor> descriptors;

    public BeanIntrospectionData(List<PropertyDescriptor> descriptors) {
        this.descriptors = descriptors;
    }

    public List<PropertyDescriptor> getDescriptors() {
        return descriptors;
    }

    public PropertyDescriptor getDescriptor(String propertyName) {
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            if (propertyDescriptor.getName().equals(propertyName)) {
                return propertyDescriptor;
            }
        }
        return null;
    }
}
