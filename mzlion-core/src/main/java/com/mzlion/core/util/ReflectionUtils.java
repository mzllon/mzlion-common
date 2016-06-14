package com.mzlion.core.util;

import com.mzlion.core.lang.ArrayUtils;
import com.mzlion.core.lang.Assert;
import com.mzlion.core.reflect.FieldFilter;
import com.mzlion.core.reflect.StaticFieldFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by mzlion on 2016/6/7.
 */
public class ReflectionUtils {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    private static final WeakHashMap<Class<?>, List<Field>> declaredFieldsCache = new WeakHashMap<>(255);

    private static final WeakHashMap<Class<?>, Method[]> declaredMethodCache = new WeakHashMap<>(255);

    public static List<Field> getDeclaredFields(Class<?> targetClass) {
        Assert.notNull(targetClass, "Target class must not be null.");
        List<Field> declaredFields = declaredFieldsCache.get(targetClass);
        if (declaredFields == null) {
            logger.debug(" It cannot find field list in cache for targetClass->{}", targetClass);
            declaredFields = new ArrayList<>();
            Class<?> currentClass = targetClass;
            Field[] fields;
            while (currentClass != null) {
                fields = currentClass.getDeclaredFields();
                Collections.addAll(declaredFields, fields);
                currentClass = currentClass.getSuperclass();
            }
            logger.debug(" Reflect fields success,then put then into cache container.targetClass->{},fields->{}",
                    targetClass, declaredFields);
            declaredFieldsCache.put(targetClass, declaredFields.size() == 0 ? Collections.<Field>emptyList() : declaredFields);
        } else {
            logger.debug(" ===> The cache exist class [{}] reflection.", targetClass.getName());
        }
        return declaredFields;
    }

    public static List<Field> filter(List<Field> fieldList, FieldFilter... fieldFilters) {
        Assert.notEmpty(fieldList, "Field list is null or empty.");
        if (ArrayUtils.isEmpty(fieldFilters)) {
            return new ArrayList<>(fieldList);
        }
        List<Field> filterFieldList = new ArrayList<>(fieldList.size());
        boolean filter;
        for (Field field : fieldList) {
            filter = false;
            for (FieldFilter fieldFilter : fieldFilters) {
                if (fieldFilter.matches(field)) {
                    filter = true;
                    break;
                }
            }
            if (!filter) filterFieldList.add(field);
        }
        return filterFieldList;
    }

    public static List<Field> getDeclaredFieldsIgnoreStatic(Class<?> targetClass) {
        List<Field> declaredFields = getDeclaredFields(targetClass);
        return filter(declaredFields, new StaticFieldFilter());
    }

    /**
     * 根据{@code fieldName}在{@code targetClass}查找，支持父类的属性查找。
     *
     * @param targetClass the class to introspect.
     * @param fieldName   the name of the field.
     * @return the Field object, or {@code null} if not found.
     */
    public static Field findField(Class<?> targetClass, String fieldName) {
        return findField(targetClass, fieldName, null);
    }

    /**
     * 根据{@code fieldName}或{@code fieldType}在{@code targetClass}查找，支持父类的属性查找。
     *
     * @param targetClass the class to introspect.
     * @param fieldName   the name of the field.
     * @param fieldType   the type of the field.
     * @return the Field object, or {@code null} if not found.
     */
    public static Field findField(Class<?> targetClass, String fieldName, Class<?> fieldType) {
        Assert.notNull(targetClass, "Target class must not be null.");
        Assert.isTrue(fieldName != null || fieldType != null, "Either fieldName or fieldType of the field must be specified.");
        Class<?> searchType = targetClass;
        while (Object.class != searchType && searchType != null) {
            List<Field> declaredFields = getDeclaredFields(searchType);
            for (Field declaredField : declaredFields) {
                if ((fieldName == null || fieldName.equals(declaredField.getName())) &&
                        (fieldType == null || fieldType.equals(declaredField.getType()))) return declaredField;
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }
}
