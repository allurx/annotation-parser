/*
 * Copyright 2024 allurx
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.allurx.annotation.parser.util;

import io.allurx.kit.base.Conditional;

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
 * This class provides static methods for performing various reflection-related tasks,
 * such as retrieving fields, accessing and modifying field values, invoking methods,
 * and working with constructors.
 */
public final class Reflections {

    private Reflections() {
    }

    /**
     * Retrieves the {@link Field} objects declared in the specified class.
     *
     * @param inputClass the {@link Class} of the input object
     * @param inherited  whether to include inherited {@link Field} objects from superclasses
     * @return a list of {@link Field} objects declared in the specified class,
     * including inherited fields if specified
     */
    public static List<Field> listFields(Class<?> inputClass, boolean inherited) {
        return Optional.ofNullable(inputClass)
                .filter(clazz -> clazz != Object.class)
                .map(clazz -> Conditional.of(Stream.of(clazz.getDeclaredFields()).collect(Collectors.toList()))
                        .when(inherited)
                        .consume(fields -> fields.addAll(listFields(clazz.getSuperclass(), true)))
                        .get())
                .orElseGet(ArrayList::new);
    }

    /**
     * Retrieves the value of a specific {@link Field} in the input object.
     *
     * @param input the input object from which to retrieve the field value
     * @param field the {@link Field} object representing the field to access
     * @return the value of the specified {@link Field}
     * @throws ReflectionException if accessing the field fails
     */
    public static Object getFieldValue(Object input, Field field) {
        try {
            if (!field.canAccess(input)) field.setAccessible(true);
            return field.get(input);
        } catch (Exception e) {
            throw new ReflectionException("Failed to get value of field %s from %s.".formatted(field.getName(), input.getClass()), e);
        }
    }

    /**
     * Sets the value of a specific field in the input object.
     *
     * @param input    the input object to modify
     * @param field    the {@link Field} object representing the field to modify
     * @param newInput the new value to set for the field
     * @throws ReflectionException if setting the field value fails
     */
    public static void setFieldValue(Object input, Field field, Object newInput) {
        try {
            if (!field.canAccess(input)) field.setAccessible(true);
            field.set(input, newInput);
        } catch (Exception e) {
            throw new ReflectionException("Failed to set value of field %s in %s.".formatted(field.getName(), input.getClass()), e);
        }
    }

    /**
     * Invokes a method on the input object.
     *
     * @param input  the input object on which to invoke the method
     * @param method the {@link Method} object representing the method to invoke
     * @param args   the arguments to pass to the method
     * @return the result of the method execution
     * @throws ReflectionException if method invocation fails
     */
    public static Object invokeMethod(Object input, Method method, Object... args) {
        try {
            if (!method.canAccess(input)) method.setAccessible(true);
            return method.invoke(input, args);
        } catch (Exception e) {
            throw new ReflectionException("Failed to invoke method %s.".formatted(method), e);
        }
    }

    /**
     * Retrieves a declared constructor of the specified class that matches the given parameter types.
     *
     * @param clazz          the {@code Class} object to search for a constructor
     * @param parameterTypes the parameter types of the constructor to find
     * @param <T>            the type of the object that the constructor represents
     * @return the declared constructor that matches the specified parameter types
     */
    public static <T> Optional<Constructor<T>> getDeclaredConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        try {
            return Optional.of(clazz.getDeclaredConstructor(parameterTypes));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
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
            if (!constructor.canAccess(null)) constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new ReflectionException("Failed to instantiate object using constructor %s with parameters %s".formatted(constructor, Arrays.toString(args)), e);
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
     * Retrieves the {@link Class} object of the specified input.
     *
     * @param input the input object
     * @param <T>   the type of the object
     * @return the {@link Class} object representing the type of the specified input
     */
    public static <T> Class<T> getClass(T input) {
        return uncheckedCast(input.getClass());
    }
}