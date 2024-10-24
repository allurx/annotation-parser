/*
 * Copyright 2024 allurx
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.allurx.annotation.parser.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.allurx.kit.base.reflection.TypeConverter.uncheckedCast;

/**
 * Utility class for reflection operations.
 * This class provides static methods to perform various reflection-related tasks
 * such as retrieving fields, accessing and modifying field values, invoking methods,
 * and working with constructors.
 * <p>
 * The class is final and has a private constructor to prevent instantiation.
 *
 * @author allurx
 */
public final class Reflections {

    private Reflections() {
    }

    /**
     * Retrieves the {@link Field} objects declared in the specified class.
     *
     * @param targetClass the {@link Class} of the target object
     * @param inherited   whether to include inherited {@link Field} objects from superclasses
     * @return a list of {@link Field} objects declared in the specified class,
     * including inherited fields if specified
     */
    public static List<Field> listFields(Class<?> targetClass, boolean inherited) {
        return Optional.ofNullable(targetClass)
                .filter(clazz -> clazz != Object.class)
                .map(clazz -> {
                    List<Field> fields = Stream.of(clazz.getDeclaredFields()).collect(Collectors.toList());
                    if (inherited) {
                        fields.addAll(listFields(clazz.getSuperclass(), true));
                    }
                    return fields;
                }).orElseGet(ArrayList::new);
    }

    /**
     * Retrieves the value of a specific {@link Field} in the target object.
     *
     * @param target the target object from which to retrieve the field value
     * @param field  the {@link Field} object representing the field to access
     * @return the value of the specified {@link Field}
     * @throws ReflectionException if accessing the field fails
     */
    public static Object getFieldValue(Object target, Field field) {
        try {
            if (field.canAccess(target)) {
                return field.get(target);
            }
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            throw new ReflectionException(String.format("Failed to get value of field %s from %s.", field.getName(), target.getClass()), e);
        }
    }

    /**
     * Sets the value of a specific field in the target object.
     *
     * @param target   the target object to modify
     * @param field    the {@link Field} object representing the field to modify
     * @param newValue the new value to set for the field
     * @throws ReflectionException if setting the field value fails
     */
    public static void setFieldValue(Object target, Field field, Object newValue) {
        try {
            if (field.canAccess(target)) {
                field.set(target, newValue);
                return;
            }
            field.setAccessible(true);
            field.set(target, newValue);
        } catch (Exception e) {
            throw new ReflectionException(String.format("Failed to set value of field %s in %s.", field.getName(), target.getClass()), e);
        }
    }

    /**
     * Invokes a method on the target object.
     *
     * @param target the target object on which to invoke the method
     * @param method the {@link Method} object representing the method to invoke
     * @param args   the arguments to pass to the method
     * @return the result of the method execution
     * @throws ReflectionException if method invocation fails
     */
    public static Object invokeMethod(Object target, Method method, Object... args) {
        try {
            if (method.canAccess(target)) {
                return method.invoke(target, args);
            }
            method.setAccessible(true);
            return method.invoke(target, args);
        } catch (Exception e) {
            throw new ReflectionException(String.format("Failed to invoke method %s.", method), e);
        }
    }

    /**
     * Retrieves a declared constructor of the specified class that matches the given parameter types.
     *
     * @param clazz          the {@code Class} object to search for a constructor
     * @param parameterTypes the parameter types of the constructor to find
     * @param <T>            the type of the object that the constructor represents
     * @return the declared constructor that matches the specified parameter types, or null if not found
     */
    public static <T> Constructor<T> getDeclaredConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * Instantiates an object represented by the specified constructor with the given arguments.
     *
     * @param constructor the constructor to use for instantiation
     * @param args        the arguments to pass to the constructor
     * @param <T>         the type of the object represented by the constructor
     * @return an instance of the object created by the constructor
     * @throws ReflectionException if instantiation fails
     */
    public static <T> T newInstance(Constructor<T> constructor, Object... args) {
        try {
            if (!constructor.canAccess(null)) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new ReflectionException(String.format("Failed to instantiate object using constructor %s with parameters %s", constructor, Arrays.toString(args)), e);
        }
    }

    /**
     * Creates a new array of the specified component type and length.
     *
     * @param componentType the {@link Class} of the component type
     * @param length        the length of the array to create
     * @param <T>           the type of the components in the array
     * @return a new array of the specified component type and length
     */
    public static <T> T[] newArray(Class<T> componentType, int length) {
        return uncheckedCast(Array.newInstance(componentType, length));
    }

    /**
     * Retrieves the {@link Class} object of the specified value.
     *
     * @param value the object value
     * @param <T>   the type of the object
     * @return the {@link Class} object representing the type of the specified value
     */
    public static <T> Class<T> getClass(T value) {
        return uncheckedCast(value.getClass());
    }
}