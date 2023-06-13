/*
 * Copyright 2023 the original author or authors.
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

package red.zyc.parser.util;

import red.zyc.parser.exception.AnnotationParseException;

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

/**
 * @author zyc
 */
public final class Reflections {

    private Reflections() {
    }

    /**
     * 获取目标对象以及所有父类定义的 {@link Field}。
     *
     * @param targetClass 目标对象的{@link Class}
     * @return 目标对象以及所有父类定义的 {@link Field}
     */
    public static List<Field> listAllFields(Class<?> targetClass) {
        return Optional.ofNullable(targetClass)
                .filter(clazz -> clazz != Object.class)
                .map(clazz -> {
                    List<Field> fields = Stream.of(clazz.getDeclaredFields()).collect(Collectors.toList());
                    fields.addAll(listAllFields(clazz.getSuperclass()));
                    return fields;
                }).orElseGet(ArrayList::new);
    }

    /**
     * 获取目标对象中某个{@link Field}的值
     *
     * @param target 目标对象
     * @param field  目标对象的{@link Field}
     * @return {@link Field}的值
     */
    public static Object getFieldValue(Object target, Field field) {
        try {
            if (field.canAccess(target)) {
                return field.get(target);
            }
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            throw new AnnotationParseException(String.format("获取%s的域%s失败。", target.getClass(), field.getName()), e);
        }
    }

    /**
     * 设置目标对象某个域的值
     *
     * @param target   目标对象
     * @param field    目标对象的{@link Field}
     * @param newValue 将要设置的新值
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
            throw new AnnotationParseException(String.format("%s的域%s赋值失败。", target.getClass(), field.getName()), e);
        }
    }

    /**
     * 执行方法
     *
     * @param target 目标对象
     * @param method 目标对象的{@link Method}
     * @param args   方法参数
     * @return 方法执行结果
     */
    public static Object invokeMethod(Object target, Method method, Object... args) {
        try {
            if (method.canAccess(target)) {
                return method.invoke(target, args);
            }
            method.setAccessible(true);
            return method.invoke(target, args);
        } catch (Exception e) {
            throw new AnnotationParseException(String.format("执行方法%s失败。", method), e);
        }
    }

    /**
     * 从指定的{@code class}中获取带有指定参数的构造器
     *
     * @param clazz          指定的{@code class}
     * @param parameterTypes 构造器的参数
     * @param <T>            构造器代表的对象类型
     * @return 带有指定参数的构造器
     */
    public static <T> Constructor<T> getDeclaredConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * 实例化构造器代表的对象
     *
     * @param constructor 构造器
     * @param args        构造器参数
     * @param <T>         构造器代表对象的类型
     * @return 构造器代表的对象
     */
    public static <T> T newInstance(Constructor<T> constructor, Object... args) {
        try {
            if (!constructor.canAccess(null)) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new AnnotationParseException(String.format("构造器%s通过参数%s实例化失败", constructor, Arrays.toString(args)), e);
        }
    }

    /**
     * 创建指定组件类型、长度的数组
     *
     * @param componentType 组件的{@link Class}
     * @param length        数组长度
     * @param <T>           组件的类型
     * @return 指定组件类型、长度的数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(Class<T> componentType, int length) {
        return (T[]) Array.newInstance(componentType, length);
    }

    /**
     * {@link Object#getClass()}返回是通配符类型的{@link Class}，
     * 可以通过这个方法获取指定类型对象的{@link Class}。
     *
     * @param value 对象值
     * @param <T>   对象类型
     * @return 指定类型对象的 {@link Class}
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClass(T value) {
        return (Class<T>) value.getClass();
    }

}