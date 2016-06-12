package com.mzlion.poi;

import net.jodah.typetools.TypeResolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public abstract class TypeReference<T> implements Comparable<TypeReference<T>> {

    private Type genericsType;

    public TypeReference() {
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof Class<?>) {
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        }
        this.genericsType = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        System.out.println(this.getGenericsType());
        TypeResolver.enableCache();
        Class<?> aClass = TypeResolver.resolveRawClass(this.genericsType, Object.class);
        System.out.println(aClass);
    }

    public Type getGenericsType() {
        return genericsType;
    }

    @Override
    public int compareTo(TypeReference<T> o) {
        return 0;
    }
}
