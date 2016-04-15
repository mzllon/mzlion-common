package com.mzlion.core.json.gson;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 2016-04-14 {@code List}类型的参数化
 *
 * @author mzlion
 */
public class ListParameterizedType implements ParameterizedType {

    private final Type type;

    public ListParameterizedType(Type type) {
        this.type = type;
    }

    /**
     * Returns an array of {@code Type} objects representing the actual type
     * arguments to this type.
     * <p>
     * <p>Note that in some cases, the returned array be empty. This can occur
     * if this type represents a non-parameterized type nested within
     * a parameterized type.
     *
     * @return an array of {@code Type} objects representing the actual type
     * arguments to this type
     * @throws TypeNotPresentException             if any of the
     *                                             actual type arguments refers to a non-existent type declaration
     * @throws MalformedParameterizedTypeException if any of the
     *                                             actual type parameters refer to a parameterized type that cannot
     *                                             be instantiated for any reason
     * @since 1.5
     */
    @Override
    public Type[] getActualTypeArguments() {
        return new Type[]{type};
    }

    /**
     * Returns the {@code Type} object representing the class or interface
     * that declared this type.
     *
     * @return the {@code Type} object representing the class or interface
     * that declared this type
     * @since 1.5
     */
    @Override
    public Type getRawType() {
        return List.class;
    }

    /**
     * Returns a {@code Type} object representing the type that this type
     * is a member of.  For example, if this type is {@code O<T>.I<S>},
     * return a representation of {@code O<T>}.
     * <p>
     * <p>If this type is a top-level type, {@code null} is returned.
     *
     * @return a {@code Type} object representing the type that
     * this type is a member of. If this type is a top-level type,
     * {@code null} is returned
     * @throws TypeNotPresentException             if the owner type
     *                                             refers to a non-existent type declaration
     * @throws MalformedParameterizedTypeException if the owner type
     *                                             refers to a parameterized type that cannot be instantiated
     *                                             for any reason
     * @since 1.5
     */
    @Override
    public Type getOwnerType() {
        return null;
    }
}
