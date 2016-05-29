package com.mzlion.core.beans;

import com.mzlion.core.lang.Assert;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * <p>
 * Javabean属性工具类
 * </p>
 * Created by mzlion on 2016/5/22.
 */
public class PropertyUtilBean {

    /**
     * 获取对象{@link PropertyUtilBean}
     */
    public static PropertyUtilBean getInstance() {
        return new PropertyUtilBean();
    }

    /**
     * 缓存已经内省过的Class
     */
    private final WeakHashMap<Class<?>, BeanIntrospectionData> descriptorsCache = new WeakHashMap<>();

    /**
     * 私有化构造器
     */
    private PropertyUtilBean() {
    }

    /**
     * 获取Javabean的属性描述列表
     *
     * @param bean 对象内容
     * @return {@code PropertyDescriptor}数组
     * @see #getPropertyDescriptors(Class)
     */
    public List<PropertyDescriptor> getPropertyDescriptors(Object bean) {
        if (bean == null) {
            throw new IllegalArgumentException("Bean must not be null.");
        }
        return this.getPropertyDescriptors(bean.getClass());
    }

    /**
     * 获取Javabean的属性描述列表
     *
     * @param beanClass 对象内容
     * @return {@code PropertyDescriptor}数组
     */
    public List<PropertyDescriptor> getPropertyDescriptors(Class<?> beanClass) {
        if (beanClass == null) {
            throw new IllegalArgumentException("BeanClass must not be null.");
        }
        BeanIntrospectionData beanIntrospectionData;
        synchronized (descriptorsCache) {
            beanIntrospectionData = descriptorsCache.get(beanClass);
        }
        if (beanIntrospectionData == null) {
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                List<PropertyDescriptor> propertyDescriptorList = new ArrayList<>(propertyDescriptors.length);
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    if (!"class".equals(propertyDescriptor.getName())) {
                        propertyDescriptorList.add(propertyDescriptor);
                    }
                }
                beanIntrospectionData = new BeanIntrospectionData(propertyDescriptorList);
                synchronized (descriptorsCache) {
                    descriptorsCache.put(beanClass, beanIntrospectionData);
                }
            } catch (IntrospectionException e) {
                throw new FatalBeanException(String.format("Failed to obtain BeanInfo for class [%s]", beanClass.getName()), e);
            }
        }
        return beanIntrospectionData.getDescriptors();
    }

    public PropertyDescriptor getPropertyDescriptor(Object bean, String propertyName) {
        Assert.assertNotNull(bean, "Bean must not be null.");
        return this.getPropertyDescriptor(bean.getClass(), propertyName);
    }

    public PropertyDescriptor getPropertyDescriptor(Class<?> beanClass, String propertyName) {
        List<PropertyDescriptor> propertyDescriptors = this.getPropertyDescriptors(beanClass);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getName().equals(propertyName)) {
                return propertyDescriptor;
            }
        }
        return null;
    }

    /**
     * 清理缓存
     */
    public void clearDescriptors() {
        descriptorsCache.clear();
    }
}
