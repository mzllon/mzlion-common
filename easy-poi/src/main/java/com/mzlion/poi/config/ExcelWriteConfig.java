package com.mzlion.poi.config;

import com.mzlion.core.lang.Assert;
import com.mzlion.core.lang.ClassUtils;
import com.mzlion.core.lang.CollectionUtils;
import com.mzlion.core.util.ReflectionUtils;
import com.mzlion.poi.annotation.ExcelCell;
import com.mzlion.poi.annotation.ExcelEntity;
import com.mzlion.poi.annotation.ExcelHyperLink;
import com.mzlion.poi.annotation.ExcelMappedEntity;
import com.mzlion.poi.beans.DefaultMapBeanTypeReference;
import com.mzlion.poi.beans.PropertyCellMapping;
import com.mzlion.poi.beans.TypeReference;
import com.mzlion.poi.constant.ExcelHyperLinkType;
import com.mzlion.poi.constant.ExcelType;
import com.mzlion.poi.excel.write.DefaultExcelCellStyle;
import com.mzlion.poi.excel.write.ExcelCellStyle;
import com.mzlion.poi.exception.BeanNotConfigAnnotationException;
import net.jodah.typetools.TypeResolver;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Excel导出配置选项
 *
 * @author mzlion
 * @date 2016-06-07
 */
public class ExcelWriteConfig implements Serializable {

    /**
     * Excel标题
     */
    private String title;

    /**
     * Excel副标题
     */
    private String secondTitle;
    /**
     *
     */
    private ExcelType excelType;

    private float titleRowHeight;

    private float secondTitleRowHeight;

    private float dataRowHeight;

    private String sheetName;

    private boolean headerRowCreate;

    private Type type;

    private Class<?> rawClass;

    private Class<? extends ExcelCellStyle> excelCellStyleClass;

    private List<PropertyCellMapping> propertyCellMappingList;

    private List<PropertyCellMapConfig> propertyCellMapConfigList;

    private ExcelWriteConfig(Builder builder) {
        this.title = builder.title;
        this.secondTitle = builder.secondTitle;
        this.excelType = builder.excelType;
        this.titleRowHeight = builder.titleRowHeight;
        this.secondTitleRowHeight = builder.secondTitleRowHeight;
        this.dataRowHeight = builder.dataRowHeight;
        this.sheetName = builder.sheetName;
        this.headerRowCreate = builder.headerRowCreate;
        this.excelCellStyleClass = builder.excelCellStyleClass;
        this.type = builder.type;
        if (this.type == null) {
            throw new NullPointerException("The bean type is null.");
        }
        this.rawClass = TypeResolver.resolveRawClass(this.type, null);
        this.propertyCellMapConfigList = builder.propertyCellMapConfigList;

        List<PropertyCellMapping> propertyCellMappingList;
        if (ClassUtils.isAssignable(Map.class, this.rawClass)) {
            Assert.assertNotEmpty(this.propertyCellMapConfigList, "The PropertyCellMapConfig list must not be null or empty when the bean type is Map class.");
            propertyCellMappingList = this.generatePropertyCellMapByMap();
        } else {
            //check has @ExcelEntity annotation
            ExcelEntity excelEntity = this.rawClass.getAnnotation(ExcelEntity.class);
            if (excelEntity == null) {
                throw new BeanNotConfigAnnotationException("The bean [" + this.rawClass.getName() + "] must config ExcelEntity annotation.");
            }
            propertyCellMappingList = this.generatePropertyCellMapByBean(this.rawClass, new ArrayList<Class<?>>());
            if (CollectionUtils.isEmpty(propertyCellMappingList)) {
                throw new BeanNotConfigAnnotationException("The entity [" + this.rawClass.getName() + "] must config at least a '@ExcelCell' annotation on properties.");
            }
        }


        //sort
        this.sortPropertyCellMappingList(propertyCellMappingList);
    }

    public String getTitle() {
        return title;
    }

    public String getSecondTitle() {
        return secondTitle;
    }

    public ExcelType getExcelType() {
        return excelType;
    }

    public float getTitleRowHeight() {
        return titleRowHeight;
    }

    public float getSecondTitleRowHeight() {
        return secondTitleRowHeight;
    }

    public float getDataRowHeight() {
        return dataRowHeight;
    }

    public String getSheetName() {
        return sheetName;
    }

    public boolean isHeaderRowCreate() {
        return headerRowCreate;
    }

    public Type getType() {
        return type;
    }

    public Class<?> getRawClass() {
        return rawClass;
    }

    public Class<? extends ExcelCellStyle> getExcelCellStyleClass() {
        return excelCellStyleClass;
    }

    public List<PropertyCellMapping> getPropertyCellMappingList() {
        return propertyCellMappingList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExcelWriteConfig{");
        sb.append("title='").append(title).append('\'');
        sb.append(", secondTitle='").append(secondTitle).append('\'');
        sb.append(", excelType=").append(excelType);
        sb.append(", titleRowHeight=").append(titleRowHeight);
        sb.append(", secondTitleRowHeight=").append(secondTitleRowHeight);
        sb.append(", dataRowHeight=").append(dataRowHeight);
        sb.append(", sheetName='").append(sheetName).append('\'');
        sb.append(", headerRowCreate=").append(headerRowCreate);
        sb.append(", type=").append(type);
        sb.append(", excelCellStyleClass=").append(excelCellStyleClass);
        sb.append('}');
        return sb.toString();
    }

    public static class Builder {

        private String title;

        private String secondTitle;

        private ExcelType excelType;

        private float titleRowHeight;

        private float secondTitleRowHeight;

        private float dataRowHeight;

        private String sheetName;

        private boolean headerRowCreate;

        private Type type;

        private Class<? extends ExcelCellStyle> excelCellStyleClass;

        private List<PropertyCellMapConfig> propertyCellMapConfigList;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder secondTitle(String secondTitle) {
            this.secondTitle = secondTitle;
            return this;
        }

        public Builder excelType(ExcelType excelType) {
            this.excelType = excelType;
            return this;
        }

        public Builder titleRowHeight(float titleRowHeight) {
            this.titleRowHeight = titleRowHeight;
            return this;
        }

        public Builder secondTitleRowHeight(float secondTitleRowHeight) {
            this.secondTitleRowHeight = secondTitleRowHeight;
            return this;
        }

        public Builder dataRowHeight(float dataRowHeight) {
            this.dataRowHeight = dataRowHeight;
            return this;
        }

        public Builder sheetName(String sheetName) {
            this.sheetName = sheetName;
            return this;
        }

        public Builder headerRowCreate(boolean headerRowCreate) {
            this.headerRowCreate = headerRowCreate;
            return this;
        }

        public Builder beanClass(Class<?> beanClass) {
            this.type = beanClass;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public <T> Builder type(TypeReference<T> typeReference) {
            this.type = typeReference.getType();
            return this;
        }

        public Builder excelCellStyleClass(Class<? extends ExcelCellStyle> excelCellStyleClass) {
            this.excelCellStyleClass = excelCellStyleClass;
            return this;
        }

        public Builder mapConfig(PropertyCellMapConfig config) {
            Assert.assertNotNull(config, "PropertyCellMapConfig is null.");
            this.propertyCellMapConfigList.add(config);
            return this;
        }

        public Builder mapConfig(List<PropertyCellMapConfig> configList) {
            Assert.assertNotEmpty(configList, "The PropertyCellMapConfig list is null.");
            this.propertyCellMapConfigList.addAll(configList);
            return this;
        }


        public Builder() {
            this.excelType = ExcelType.XLSX;
            this.sheetName = "Sheet1";
            this.titleRowHeight = 20;
            this.secondTitleRowHeight = 16;
            this.dataRowHeight = 13.5f;
            this.excelCellStyleClass = DefaultExcelCellStyle.class;
            this.type(new DefaultMapBeanTypeReference().getType());
            this.propertyCellMapConfigList = new ArrayList<>();
            this.headerRowCreate = true;
        }

        Builder(ExcelWriteConfig excelWriteConfig) {
            this.title = excelWriteConfig.title;
            this.secondTitle = excelWriteConfig.secondTitle;
            this.excelType = excelWriteConfig.excelType;
            this.titleRowHeight = excelWriteConfig.titleRowHeight;
            this.secondTitleRowHeight = excelWriteConfig.secondTitleRowHeight;
            this.dataRowHeight = excelWriteConfig.dataRowHeight;
            this.sheetName = excelWriteConfig.sheetName;
            this.headerRowCreate = excelWriteConfig.headerRowCreate;
            this.type = excelWriteConfig.type;
            this.excelCellStyleClass = excelWriteConfig.excelCellStyleClass;
            this.propertyCellMapConfigList = excelWriteConfig.propertyCellMapConfigList;
        }

        public ExcelWriteConfig build() {
            return new ExcelWriteConfig(this);
        }

    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    private List<PropertyCellMapping> generatePropertyCellMapByBean(Class<?> clazz, List<Class<?>> excludeList) {
        /*List<PropertyCellMapping> propertyCellMappingList = new ArrayList<>(this.propertyCellMapConfigList.size());
        List<Field> fieldList = ReflectionUtils.getDeclaredFieldsIgnoreStatic(this.rawClass);
        fieldList = ReflectionUtils.filter(fieldList, new StaticFieldFilter());
        for (Field field : fieldList) {
            String fieldName = field.getName();
            ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
            if (excelCell != null) {
                PropertyCellMapping propertyCellMapping = new PropertyCellMapping();
                propertyCellMapping.setTitle(excelCell.value());
                propertyCellMapping.setRequired(excelCell.required());
                propertyCellMapping.setPropertyName(fieldName);
                propertyCellMapping.setType(excelCell.type());
                propertyCellMapping.setExcelDateFormat(excelCell.excelDateFormat());
                propertyCellMapping.setJavaDateFormat(excelCell.javaDateFormat());
                propertyCellMapping.setWidth(excelCell.width());
                propertyCellMapping.setAutoWrap(excelCell.autoWrap());
                switch (excelCell.type()) {
                    case HYPER_LINK:
                        ExcelHyperLink excelHyperLink = field.getAnnotation(ExcelHyperLink.class);
                        if (excelHyperLink != null) {
                            propertyCellMapping.setExcelHyperLinkType(excelHyperLink.value());
                            propertyCellMapping.setHyperlinkName(excelHyperLink.linkName());
                        } else {
                            propertyCellMapping.setExcelHyperLinkType(ExcelHyperLinkType.URL);
                        }
                        break;
                }

                ExcelMappedEntity excelMappedEntity = field.getAnnotation(ExcelMappedEntity.class);
                if (excelMappedEntity != null) {
                    Class<?> fieldType = field.getType();
                    Class<?> fieldRawClass = field.isAnnotationPresent(ExcelEntity.class) ? fieldType : TypeResolver.resolveRawClass(fieldType.getGenericSuperclass(), null);

                }

                propertyCellMappingList.add(propertyCellMapping);
            }
        }
        return propertyCellMappingList = this.annotationExcelCell(this.rawClass, new ArrayList<Class<?>>());
        */
        excludeList.add(clazz);
        List<Field> fieldList = ReflectionUtils.getDeclaredFieldsIgnoreStatic(clazz);
        List<PropertyCellMapping> propertyCellMappingList = new ArrayList<>(fieldList.size());
        for (Field field : fieldList) {
            String fieldName = field.getName();
            ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
            if (excelCell != null) {
                PropertyCellMapping propertyCellMapping = new PropertyCellMapping();
                propertyCellMapping.setTitle(excelCell.value());
                propertyCellMapping.setRequired(excelCell.required());
                propertyCellMapping.setPropertyName(fieldName);
                propertyCellMapping.setType(excelCell.type());
                propertyCellMapping.setExcelDateFormat(excelCell.excelDateFormat());
                propertyCellMapping.setJavaDateFormat(excelCell.javaDateFormat());
                propertyCellMapping.setWidth(excelCell.width());
                propertyCellMapping.setAutoWrap(excelCell.autoWrap());
                switch (excelCell.type()) {
                    case HYPER_LINK:
                        ExcelHyperLink excelHyperLink = field.getAnnotation(ExcelHyperLink.class);
                        if (excelHyperLink != null) {
                            propertyCellMapping.setExcelHyperLinkType(excelHyperLink.value());
                            propertyCellMapping.setHyperlinkName(excelHyperLink.linkName());
                        } else {
                            propertyCellMapping.setExcelHyperLinkType(ExcelHyperLinkType.URL);
                        }
                        break;
                }

                ExcelMappedEntity excelMappedEntity = field.getAnnotation(ExcelMappedEntity.class);
                if (excelMappedEntity != null) {
                    Class<?> fieldType = field.getType();
                    Class<?> fieldRawClass = fieldType.isAnnotationPresent(ExcelEntity.class) ? fieldType : TypeResolver.resolveRawArgument(field.getGenericType(), Collection.class);
                    if (excludeList.contains(fieldRawClass)) {
                        break;
                    }
                    List<PropertyCellMapping> children = this.generatePropertyCellMapByBean(fieldRawClass, excludeList);
                    propertyCellMapping.setChildrenMapping(children);
                }

                propertyCellMappingList.add(propertyCellMapping);
            }
        }
        return propertyCellMappingList;
    }

    private void sortPropertyCellMappingList(List<PropertyCellMapping> propertyCellMappingList) {
        List<PropertyCellMapping> noOrderList = new ArrayList<>(propertyCellMappingList.size() >> 1);
        List<PropertyCellMapping> orderList = new ArrayList<>(propertyCellMappingList.size() >> 1);
        for (PropertyCellMapping propertyCellMapping : propertyCellMappingList) {
            if (propertyCellMapping.getCellIndex() == 0) {
                noOrderList.add(propertyCellMapping);
            } else {
                propertyCellMapping.setCellIndex(propertyCellMapping.getCellIndex() - 1);
                orderList.add(propertyCellMapping);
            }
        }

        //sort order
        Collections.sort(orderList);

        int noOrderIndex = 0, count = 0, cellIndex, i;
        PropertyCellMapping bpcd;
        this.propertyCellMappingList = new ArrayList<>(propertyCellMappingList.size());
        for (PropertyCellMapping propertyCellMapping : orderList) {
            cellIndex = propertyCellMapping.getCellIndex();
            for (i = noOrderIndex; count < cellIndex; i++) {
                bpcd = noOrderList.get(i);
                bpcd.setCellIndex(count++);
                this.propertyCellMappingList.add(bpcd);
                noOrderIndex++;
            }
            this.propertyCellMappingList.add(propertyCellMapping);
            count++;
        }
        int size = noOrderList.size();
        if (noOrderIndex < size) {
            for (; noOrderIndex < size; noOrderIndex++) {
                bpcd = noOrderList.get(noOrderIndex);
                bpcd.setCellIndex(count++);
                this.propertyCellMappingList.add(bpcd);
            }
        }
    }

    private List<PropertyCellMapping> generatePropertyCellMapByMap() {
        List<PropertyCellMapping> propertyCellMappingList = new ArrayList<>(this.propertyCellMapConfigList.size());
        PropertyCellMapping propertyCellMapping;
        for (PropertyCellMapConfig propertyCellMapConfig : this.propertyCellMapConfigList) {
            propertyCellMapping = new PropertyCellMapping(propertyCellMapConfig);
            propertyCellMappingList.add(propertyCellMapping);
        }
        return propertyCellMappingList;
    }

}
