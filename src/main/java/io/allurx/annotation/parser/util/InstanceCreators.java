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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实例创建器帮助类，用户可以通过这个类注册或者删除指定类型的实例创建器。
 *
 * @author allurx
 * @see InstanceCreator
 * @see Singleton
 */
public final class InstanceCreators {

    private static final Map<?, ?> EMPTY_MAP = new HashMap<>();
    private static final List<?> EMPTY_LIST = new ArrayList<>();
    private static final Map<Class<?>, InstanceCreator<?>> INSTANCE_CREATORS = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Object> SINGLETONS = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Boolean> SINGLETON_MARK = new ConcurrentHashMap<>();

    private InstanceCreators() {
    }

    /**
     * 获取指定{@link Class}的实例创建器
     *
     * @param clazz 指定的{@link Class}
     * @param <T>   指定{@link Class}的类型
     * @return {@link Class}的实例创建器
     */
    @SuppressWarnings("unchecked")
    public static <T> InstanceCreator<T> find(Class<T> clazz) {
        return (InstanceCreator<T>) INSTANCE_CREATORS.computeIfAbsent(clazz, c -> {
            if (isSingleton(c)) {
                return () -> SINGLETONS.computeIfAbsent(c, cc -> findInstanceCreator(cc).create());
            } else {
                return findInstanceCreator(c);
            }
        });
    }

    /**
     * 注册指定{@link Class}的实例创建器。
     * <p><strong>注意：不要去注册基本类型、接口类型、抽象类型、数组类型的实例创建器，
     * 这些类型的实例创建器是没有意义的，而应该注册那些在初始化过程中可能需要进行一些额外操作的具体类型。
     * 因为程序运行期间不一定能够准确的实例化对象。
     * </strong></p>
     *
     * @param clazz           指定的{@link Class}
     * @param instanceCreator 指定{@link Class}的实例创建器
     * @param <T>             实例创建器创建的对象类型
     */
    public static <T> void add(Class<T> clazz, InstanceCreator<T> instanceCreator) {
        INSTANCE_CREATORS.put(clazz, instanceCreator);
    }

    /**
     * 移除指定{@link Class}的实例创建器
     *
     * @param clazz 指定的{@link Class}
     * @param <T>   指定的{@link Class}的实例类型
     */
    public static <T> void remove(Class<T> clazz) {
        INSTANCE_CREATORS.remove(clazz);
    }

    /**
     * @return 所有实例创建器
     */
    public static Map<Class<?>, InstanceCreator<?>> instanceCreators() {
        return INSTANCE_CREATORS;
    }

    /**
     * 获取指定{@link Class}的实例创建器，尝试获取的顺序如下：
     * <ol>
     *     <li>如果该类型存在无参构造器，则通过该构造器来生成实例创建器。</li>
     *     <li>如果该类型是{@link Collection}或者{@link Map}类型则尝试获取其带有一个{@link Collection}或者{@link Map}
     *     类型参数的构造器来生成实例创建器，具体原因请参照{@link Collection}或者{@link Map}的定义规范。
     *     </li>
     * </ol>
     *
     * @param clazz 指定{@link Class}
     * @param <T>   指定{@link Class}的类型
     * @return 指定 {@link Class}的实例创建器
     */
    private static <T> InstanceCreator<T> findInstanceCreator(Class<T> clazz) {
        return Optional.ofNullable(createByNoArgsConstructor(clazz))
                .or(() -> Optional.ofNullable(createByCollectionOrMapConstructor(clazz)))
                .orElseThrow(() -> new UnableCreateInstanceException(String.format("无法创建%s的实例，请为该class添加一个InstanceCreator", clazz)));
    }

    /**
     * 通过{@link Class}的{@link Constructor 无参构造器}创建实例
     *
     * @param clazz 对象的{@link Class}
     * @param <T>   对象的类型
     * @return 该对象的实例创建器
     */
    private static <T> InstanceCreator<T> createByNoArgsConstructor(Class<T> clazz) {
        return Optional.ofNullable(Reflections.getDeclaredConstructor(clazz))
                .<InstanceCreator<T>>map(constructor -> () -> Reflections.newInstance(constructor)).orElse(null);
    }

    /**
     * 通过遵循{@link Collection}和{@link Map}定义规范的{@link Constructor 构造器}创建实例
     *
     * @param clazz 对象的{@link Class}
     * @param <T>   对象的类型
     * @return 该对象的实例创建器
     */
    private static <T> InstanceCreator<T> createByCollectionOrMapConstructor(Class<T> clazz) {
        if (Collection.class.isAssignableFrom(clazz)) {
            return Optional.ofNullable(Reflections.getDeclaredConstructor(clazz, Collection.class))
                    .<InstanceCreator<T>>map(constructor -> () -> Reflections.newInstance(constructor, EMPTY_LIST))
                    .orElse(null);
        } else if (Map.class.isAssignableFrom(clazz)) {
            return Optional.ofNullable(Reflections.getDeclaredConstructor(clazz, Map.class))
                    .<InstanceCreator<T>>map(constructor -> () -> Reflections.newInstance(constructor, EMPTY_MAP))
                    .orElse(null);
        }
        return null;
    }

    /**
     * 判断{@link Class}是否是单例
     *
     * @param clazz {@link Class}
     * @return 是否是单例
     * @see Singleton
     */
    private static boolean isSingleton(Class<?> clazz) {
        return Optional.ofNullable(SINGLETON_MARK.get(clazz)).orElse(singleton(clazz));
    }

    private static boolean singleton(Class<?> clazz) {
        Class<?>[] interfaces;
        boolean v = clazz != null && clazz != Object.class &&
                ((clazz.isInterface() ? clazz.getDeclaredAnnotation(Singleton.class) != null : clazz.isAnnotationPresent(Singleton.class)) ||
                        ((interfaces = clazz.getInterfaces()).length > 0 && Arrays.stream(interfaces).anyMatch(InstanceCreators::singleton)) ||
                        singleton(clazz.getSuperclass()));
        Optional.ofNullable(clazz).ifPresent(c -> SINGLETON_MARK.putIfAbsent(c, v));
        return v;
    }

}
