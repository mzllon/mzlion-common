/**
 * Copyright 2002-2015 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mzlion.poi.utils;

import sun.reflect.ConstantPool;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.*;
import java.util.*;

/**
 * Enhanced type resolution utilities.
 *
 * @author Jonathan Halterman
 */
@SuppressWarnings("restriction")
public final class TypeResolver {
    /**
     * Cache of type variable/argument pairs
     */
    private static final Map<Class<?>, Reference<Map<TypeVariable<?>, Type>>> typeVariableCache = Collections
            .synchronizedMap(new WeakHashMap<Class<?>, Reference<Map<TypeVariable<?>, Type>>>());
    private static volatile boolean CACHE_ENABLED = true;
    private static boolean RESOLVES_LAMBDAS;
    private static Method GET_CONSTANT_POOL;
    private static Map<String, Method> OBJECT_METHODS = new HashMap<String, Method>();

    static {
        Double javaVersion = Double.parseDouble(System.getProperty("java.specification.version", "0"));
        if (javaVersion >= 1.8) {
            try {
                GET_CONSTANT_POOL = Class.class.getDeclaredMethod("getConstantPool");
                GET_CONSTANT_POOL.setAccessible(true);

                for (Method method : Object.class.getDeclaredMethods())
                    OBJECT_METHODS.put(method.getName(), method);

                RESOLVES_LAMBDAS = true;
            } catch (Exception e) {
            }
        }
    }

    /**
     * An unknown type.
     */
    public static final class Unknown {
        private Unknown() {
        }
    }

    private TypeResolver() {
    }

    /**
     * Enables the internal caching of resolved TypeVariables.
     */
    public static void enableCache() {
        CACHE_ENABLED = true;
    }

    /**
     * Disables the internal caching of resolved TypeVariables.
     */
    public static void disableCache() {
        typeVariableCache.clear();
        CACHE_ENABLED = false;
    }

    /**
     * Returns the raw class representing the argument for the {@code type} using type variable information from the
     * {@code subType}. If no arguments can be resolved then {@code Unknown.class} is returned.
     *
     * @param type    to resolve argument for
     * @param subType to extract type variable information from
     * @return argument for {@code type} else {@link Unknown.class} if no type arguments are declared
     * @throws IllegalArgumentException if more or less than one argument is resolved for the {@code type}
     */
    public static <T, S extends T> Class<?> resolveRawArgument(Class<T> type, Class<S> subType) {
        return resolveRawArgument(resolveGenericType(type, subType), subType);
    }

    /**
     * Returns the raw class representing the argument for the {@code genericType} using type variable information from
     * the {@code subType}. If {@code genericType} is an instance of class, then {@code genericType} is returned. If no
     * arguments can be resolved then {@code Unknown.class} is returned.
     *
     * @param genericType to resolve argument for
     * @param subType     to extract type variable information from
     * @return argument for {@code genericType} else {@link Unknown.class} if no type arguments are declared
     * @throws IllegalArgumentException if more or less than one argument is resolved for the {@code genericType}
     */
    public static Class<?> resolveRawArgument(Type genericType, Class<?> subType) {
        Class<?>[] arguments = resolveRawArguments(genericType, subType);
        if (arguments == null)
            return Unknown.class;

        if (arguments.length != 1)
            throw new IllegalArgumentException(
                    "Expected 1 argument for generic type " + genericType + " but found " + arguments.length);

        return arguments[0];
    }

    /**
     * Returns an array of raw classes representing arguments for the {@code type} using type variable information from
     * the {@code subType}. Arguments for {@code type} that cannot be resolved are returned as {@code Unknown.class}. If
     * no arguments can be resolved then {@code null} is returned.
     *
     * @param type    to resolve arguments for
     * @param subType to extract type variable information from
     * @return array of raw classes representing arguments for the {@code type} else {@code null} if no type arguments are
     * declared
     */
    public static <T, S extends T> Class<?>[] resolveRawArguments(Class<T> type, Class<S> subType) {
        return resolveRawArguments(resolveGenericType(type, subType), subType);
    }

    /**
     * Returns an array of raw classes representing arguments for the {@code genericType} using type variable information
     * from the {@code subType}. Arguments for {@code genericType} that cannot be resolved are returned as
     * {@code Unknown.class}. If no arguments can be resolved then {@code null} is returned.
     *
     * @param genericType to resolve arguments for
     * @param subType     to extract type variable information from
     * @return array of raw classes representing arguments for the {@code genericType} else {@code null} if no type
     * arguments are declared
     */
    public static Class<?>[] resolveRawArguments(Type genericType, Class<?> subType) {
        Class<?>[] result = null;
        Class<?> functionalInterface = null;

        // Handle lambdas
        if (RESOLVES_LAMBDAS && subType.isSynthetic()) {
            Class<?> fi = genericType instanceof ParameterizedType
                    && ((ParameterizedType) genericType).getRawType() instanceof Class
                    ? (Class<?>) ((ParameterizedType) genericType).getRawType()
                    : genericType instanceof Class ? (Class<?>) genericType : null;
            if (fi != null && fi.isInterface())
                functionalInterface = fi;
        }

        if (genericType instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) genericType;
            Type[] arguments = paramType.getActualTypeArguments();
            result = new Class[arguments.length];
            for (int i = 0; i < arguments.length; i++)
                result[i] = resolveRawClass(arguments[i], subType, functionalInterface);
        } else if (genericType instanceof TypeVariable) {
            result = new Class[1];
            result[0] = resolveRawClass(genericType, subType, functionalInterface);
        } else if (genericType instanceof Class) {
            TypeVariable<?>[] typeParams = ((Class<?>) genericType).getTypeParameters();
            result = new Class[typeParams.length];
            for (int i = 0; i < typeParams.length; i++)
                result[i] = resolveRawClass(typeParams[i], subType, functionalInterface);
        }

        return result;
    }

    /**
     * Returns the generic {@code type} using type variable information from the {@code subType} else {@code null} if the
     * generic type cannot be resolved.
     *
     * @param type    to resolve generic type for
     * @param subType to extract type variable information from
     * @return generic {@code type} else {@code null} if it cannot be resolved
     */
    public static Type resolveGenericType(Class<?> type, Type subType) {
        Class<?> rawType;
        if (subType instanceof ParameterizedType)
            rawType = (Class<?>) ((ParameterizedType) subType).getRawType();
        else
            rawType = (Class<?>) subType;

        if (type.equals(rawType))
            return subType;

        Type result;
        if (type.isInterface()) {
            for (Type superInterface : rawType.getGenericInterfaces())
                if (superInterface != null && !superInterface.equals(Object.class))
                    if ((result = resolveGenericType(type, superInterface)) != null)
                        return result;
        }

        Type superClass = rawType.getGenericSuperclass();
        if (superClass != null && !superClass.equals(Object.class))
            if ((result = resolveGenericType(type, superClass)) != null)
                return result;

        return null;
    }

    /**
     * Resolves the raw class for the {@code genericType}, using the type variable information from the {@code subType}
     * else {@link Unknown} if the raw class cannot be resolved.
     *
     * @param type    to resolve raw class for
     * @param subType to extract type variable information from
     * @return raw class for the {@code genericType} else {@link Unknown} if it cannot be resolved
     */
    public static Class<?> resolveRawClass(Type genericType, Class<?> subType) {
        return resolveRawClass(genericType, subType, null);
    }

    private static Class<?> resolveRawClass(Type genericType, Class<?> subType, Class<?> functionalInterface) {
        if (genericType instanceof Class) {
            return (Class<?>) genericType;
        } else if (genericType instanceof ParameterizedType) {
            return resolveRawClass(((ParameterizedType) genericType).getRawType(), subType, functionalInterface);
        } else if (genericType instanceof GenericArrayType) {
            GenericArrayType arrayType = (GenericArrayType) genericType;
            Class<?> compoment = resolveRawClass(arrayType.getGenericComponentType(), subType, functionalInterface);
            return Array.newInstance(compoment, 0).getClass();
        } else if (genericType instanceof TypeVariable) {
            TypeVariable<?> variable = (TypeVariable<?>) genericType;
            genericType = getTypeVariableMap(subType, functionalInterface).get(variable);
            genericType = genericType == null ? resolveBound(variable)
                    : resolveRawClass(genericType, subType, functionalInterface);
        }

        return genericType instanceof Class ? (Class<?>) genericType : Unknown.class;
    }

    private static Map<TypeVariable<?>, Type> getTypeVariableMap(final Class<?> targetType,
                                                                 Class<?> functionalInterface) {
        Reference<Map<TypeVariable<?>, Type>> ref = typeVariableCache.get(targetType);
        Map<TypeVariable<?>, Type> map = ref != null ? ref.get() : null;

        if (map == null) {
            map = new HashMap<TypeVariable<?>, Type>();

            // Populate lambdas
            if (functionalInterface != null)
                populateLambdaArgs(functionalInterface, targetType, map);

            // Populate interfaces
            populateSuperTypeArgs(targetType.getGenericInterfaces(), map, functionalInterface != null);

            // Populate super classes and interfaces
            Type genericType = targetType.getGenericSuperclass();
            Class<?> type = targetType.getSuperclass();
            while (type != null && !Object.class.equals(type)) {
                if (genericType instanceof ParameterizedType)
                    populateTypeArgs((ParameterizedType) genericType, map, false);
                populateSuperTypeArgs(type.getGenericInterfaces(), map, false);

                genericType = type.getGenericSuperclass();
                type = type.getSuperclass();
            }

            // Populate enclosing classes
            type = targetType;
            while (type.isMemberClass()) {
                genericType = type.getGenericSuperclass();
                if (genericType instanceof ParameterizedType)
                    populateTypeArgs((ParameterizedType) genericType, map, functionalInterface != null);

                type = type.getEnclosingClass();
            }

            if (CACHE_ENABLED)
                typeVariableCache.put(targetType, new WeakReference<Map<TypeVariable<?>, Type>>(map));
        }

        return map;
    }

    /**
     * Populates the {@code map} with variable/argument pairs for the {@code functionalInterface}.
     */
    private static void populateLambdaArgs(Class<?> functionalInterface, final Class<?> lambdaType,
                                           Map<TypeVariable<?>, Type> map) {
        if (GET_CONSTANT_POOL != null) {
            // Find SAM
            for (Method m : functionalInterface.getMethods()) {
                if (!m.isDefault() && !Modifier.isStatic(m.getModifiers()) && !m.isBridge()) {
                    // Skip methods that override Object.class
                    Method objectMethod = OBJECT_METHODS.get(m.getName());
                    if (objectMethod != null && Arrays.equals(m.getTypeParameters(), objectMethod.getTypeParameters()))
                        continue;

                    // Get functional interface's type params
                    Type returnTypeVar = m.getGenericReturnType();
                    Type[] paramTypeVars = m.getGenericParameterTypes();

                    ConstantPool constantPool;
                    try {
                        constantPool = (ConstantPool) GET_CONSTANT_POOL.invoke(lambdaType);
                    } catch (Exception e) {
                        return;
                    }

                    String[] methodRefInfo = getMethodRefInfo(constantPool);
                    if (methodRefInfo == null) {
                        return;
                    }

                    // Populate return type argument
                    if (returnTypeVar instanceof TypeVariable) {
                        Class<?> returnType = TypeDescriptor.getReturnType(methodRefInfo[2]).getType(lambdaType.getClassLoader());
                        if (!returnType.equals(Void.class))
                            map.put((TypeVariable<?>) returnTypeVar, returnType);
                    }

                    TypeDescriptor[] arguments = TypeDescriptor.getArgumentTypes(methodRefInfo[2]);

                    // Populate object type from arbitrary object method reference
                    int paramOffset = 0;
                    if (paramTypeVars.length > 0 && paramTypeVars[0] instanceof TypeVariable
                            && paramTypeVars.length == arguments.length + 1) {
                        Class<?> instanceType = TypeDescriptor.getObjectType(methodRefInfo[0]).getType(lambdaType.getClassLoader());
                        map.put((TypeVariable<?>) paramTypeVars[0], instanceType);
                        paramOffset = 1;
                    }

                    // Handle additional arguments that are captured from the lambda's enclosing scope
                    int argOffset = 0;
                    if (paramTypeVars.length < arguments.length) {
                        argOffset = arguments.length - paramTypeVars.length;
                    }

                    // Populate type arguments
                    for (int i = 0; i + argOffset < arguments.length; i++) {
                        if (paramTypeVars[i] instanceof TypeVariable) {
                            map.put((TypeVariable<?>) paramTypeVars[i + paramOffset],
                                    arguments[i + argOffset].getType(lambdaType.getClassLoader()));
                        }
                    }

                    return;
                }
            }
        }
    }

    /**
     * Populates the {@code map} with with variable/argument pairs for the given {@code types}.
     */
    private static void populateSuperTypeArgs(final Type[] types, final Map<TypeVariable<?>, Type> map,
                                              boolean depthFirst) {
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                if (!depthFirst)
                    populateTypeArgs(parameterizedType, map, depthFirst);
                Type rawType = parameterizedType.getRawType();
                if (rawType instanceof Class)
                    populateSuperTypeArgs(((Class<?>) rawType).getGenericInterfaces(), map, depthFirst);
                if (depthFirst)
                    populateTypeArgs(parameterizedType, map, depthFirst);
            } else if (type instanceof Class) {
                populateSuperTypeArgs(((Class<?>) type).getGenericInterfaces(), map, depthFirst);
            }
        }
    }

    /**
     * Populates the {@code map} with variable/argument pairs for the given {@code type}.
     */
    private static void populateTypeArgs(ParameterizedType type, Map<TypeVariable<?>, Type> map, boolean depthFirst) {
        if (type.getRawType() instanceof Class) {
            TypeVariable<?>[] typeVariables = ((Class<?>) type.getRawType()).getTypeParameters();
            Type[] typeArguments = type.getActualTypeArguments();

            if (type.getOwnerType() != null) {
                Type owner = type.getOwnerType();
                if (owner instanceof ParameterizedType)
                    populateTypeArgs((ParameterizedType) owner, map, depthFirst);
            }

            for (int i = 0; i < typeArguments.length; i++) {
                TypeVariable<?> variable = typeVariables[i];
                Type typeArgument = typeArguments[i];

                if (typeArgument instanceof Class) {
                    map.put(variable, typeArgument);
                } else if (typeArgument instanceof GenericArrayType) {
                    map.put(variable, typeArgument);
                } else if (typeArgument instanceof ParameterizedType) {
                    map.put(variable, typeArgument);
                } else if (typeArgument instanceof TypeVariable) {
                    TypeVariable<?> typeVariableArgument = (TypeVariable<?>) typeArgument;
                    if (depthFirst) {
                        Type existingType = map.get(variable);
                        if (existingType != null) {
                            map.put(typeVariableArgument, existingType);
                            continue;
                        }
                    }

                    Type resolvedType = map.get(typeVariableArgument);
                    if (resolvedType == null)
                        resolvedType = resolveBound(typeVariableArgument);
                    map.put(variable, resolvedType);
                }
            }
        }
    }

    /**
     * Resolves the first bound for the {@code typeVariable}, returning {@code Unknown.class} if none can be resolved.
     */
    public static Type resolveBound(TypeVariable<?> typeVariable) {
        Type[] bounds = typeVariable.getBounds();
        if (bounds.length == 0)
            return Unknown.class;

        Type bound = bounds[0];
        if (bound instanceof TypeVariable)
            bound = resolveBound((TypeVariable<?>) bound);

        return bound == Object.class ? Unknown.class : bound;
    }

    /**
     * Resolves method ref info.
     *
     * @return the method ref info for the {@code constantPool}, or null if no appropriate method ref could be found.
     */
    private static String[] getMethodRefInfo(ConstantPool constantPool) {
        String[] returnValue = null;

        for (int i = constantPool.getSize() - 1; i >= 0; i--) {
            try {
                String[] methodRefInfo = constantPool.getMemberRefInfoAt(i);
                String methodName = methodRefInfo[1];
                // Always ignore constructors
                if (methodName.equals("<init>")) {
                    continue;
                }
                // If we found a method named valueOf, keep searching for other methods but still return
                // the information for the valueOf method in case it turns out to be the only valid method.
                returnValue = methodRefInfo;
                if (!methodName.equals("valueOf")) {
                    break;
                }
            } catch (IllegalArgumentException ignore) {
            }
        }

        return returnValue;
    }
}
