package com.mzlion.poi.beans;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 该类主要用于传递泛型的类型，避免在运行时期找不到泛型的实际类型。
 * <p>
 * 具体用法：由于本类是一个抽象类，所以需要子类去实现。比如下面的代码实现String的泛型传递。
 * <pre>
 *  TypeReference ref = new TypeReference&lt;List&lt;String>>() { };
 * </pre>
 * </p>
 *
 * @author mzlion
 * @date 2016-06-12
 */
public abstract class TypeReference<T> implements Comparable<T> {

    private final Type type;

    protected TypeReference() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof Class<?>)
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }


    @Override
    public int compareTo(T o) {
        return 0;
    }
}
