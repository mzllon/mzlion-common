package com.mzlion.core.lang;

import com.mzlion.core.beans.PropertyUtilBean;
import com.mzlion.core.exceptions.FatalBeanException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * <p>
 * 2016-05-22 21:13 JavaBean的工具类
 * </p>
 *
 * @author mzlion
 */
public class BeanUtils {

    /**
     * 将Javabean对象转为Map,其中值的类型为Object
     *
     * @param bean 对象
     * @return Map对象
     */
    public static Map<String, Object> toMapAsValueObject(Object bean) {
        Assert.assertNotNull(bean, "No bean specified");
        List<PropertyDescriptor> propertyDescriptors = PropertyUtilBean.getInstance().getPropertyDescriptors(bean);

        try {
            Map<String, Object> propertiesMap = new HashMap<>(propertyDescriptors.size());
            for (PropertyDescriptor pd : propertyDescriptors) {
                Method readMethod = pd.getReadMethod();
                propertiesMap.put(pd.getName(), readMethod.invoke(bean));
            }
            return propertiesMap;
        } catch (Exception e) {
            throw new FatalBeanException("Cannot read property value", e);
        }
    }

    /**
     * 将Javabean对象转为Map,其中值的类型为{@code String}，空值的属性会被忽略。
     * <pre>
     *     Person person = new Person();
     *     person.setUserId("uid-9900-01");
     *     person.setUsername("张三");
     *     BeanUtils.toMapAsValueString(person);
     *     //打印的结果为 {"userId":"uid-9900-01","username":"张三"}
     * </pre>
     *
     * @param bean 对象
     * @return Map对象
     */
    public static Map<String, String> toMapAsValueString(Object bean) {
        return toMapAsValueString(bean, true);
    }

    /**
     * 将Javabean对象转为Map,其中值的类型为{@code String}
     *
     * @param bean       对象
     * @param ignoreNull 是否忽略空值
     * @return Map对象
     */
    public static Map<String, String> toMapAsValueString(Object bean, boolean ignoreNull) {
        Map<String, Object> propertiesMap = toMapAsValueObject(bean);
        Map<String, String> resultMap = new HashMap<>(propertiesMap.size());

        Object _value;
        for (String propertyName : propertiesMap.keySet()) {
            _value = propertiesMap.get(propertyName);
            if (_value == null) {
                if (!ignoreNull)
                    resultMap.put(propertyName, null);
            } else {
                if (_value instanceof Number) {
                    Number number = (Number) _value;
                    resultMap.put(propertyName, number.toString());
                } else if (_value instanceof String) {
                    resultMap.put(propertyName, (String) _value);
                } else if (_value instanceof Date) {
                    resultMap.put(propertyName, String.valueOf(((Date) _value).getTime()));
                } else {
                    resultMap.put(propertyName, _value.toString());
                }
            }
        }
        return resultMap;
    }

    /**
     * Javabean的属性值拷贝，即对象的拷贝
     *
     * @param source 原始对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, (String) null);
    }

    /**
     * Javabean的属性值拷贝，即对象的拷贝
     *
     * @param source           原始对象
     * @param target           目标对象
     * @param ignoreProperties 过滤的属性名
     */
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        Assert.assertNotNull(source, "Source must not be null");
        Assert.assertNotNull(target, "Target must not be null");
        List<PropertyDescriptor> targetPDList = PropertyUtilBean.getInstance().getPropertyDescriptors(target);
        List<String> ignoreList = (ignoreProperties == null ? null : Arrays.asList(ignoreProperties));

        for (PropertyDescriptor targetPD : targetPDList) {
            Method writeMethod = targetPD.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPD.getName()))) {
                PropertyDescriptor sourcePD = PropertyUtilBean.getInstance().getPropertyDescriptor(source, targetPD.getName());
                if (sourcePD != null) {
                    Method readMethod = sourcePD.getReadMethod();
                    if (readMethod != null && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        try {
                            Object value = readMethod.invoke(source);
                            if (Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new FatalBeanException(String.format("Could not copy property '%s' from source to target", targetPD.getName()), e);
                        }
                    }
                }
            }
        }

    }
}
