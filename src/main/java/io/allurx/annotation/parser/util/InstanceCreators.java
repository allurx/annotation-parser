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

import io.allurx.kit.base.reflection.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A helper class for managing instance creators. Users can register or remove
 * instance creators for specific types through this class.
 *
 * <p>This class allows you to define how instances of specific types should be created.
 * For example, consider the following class definition:
 * <pre>
 *      class Person {
 *          Person(your constructor parameters) {
 *              ...
 *          }
 *      }
 * </pre>
 * In the `Person` class, there is only one parameterized constructor without a no-argument constructor.
 * This class provides mechanisms to register instance creators that can handle such cases
 * during object initialization.
 *
 * <p><strong>Note:</strong> Avoid registering instance creators for primitive types,
 * interface types, abstract types, or array types, as these are generally not meaningful.
 * Instead, register creators for concrete types that may require additional operations during initialization.</p>
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
     * Retrieves the instance creator for the specified {@link Class}.
     *
     * @param clazz the specified {@link Class}
     * @param <T>   the type of the specified {@link Class}
     * @return the instance creator for the specified {@link Class}
     */
    public static <T> InstanceCreator<T> find(Class<T> clazz) {
        return TypeConverter.uncheckedCast(INSTANCE_CREATORS.computeIfAbsent(clazz, c -> {
            if (isSingleton(c)) {
                return () -> SINGLETONS.computeIfAbsent(c, cc -> findInstanceCreator(cc).create());
            } else {
                return findInstanceCreator(c);
            }
        }));
    }

    /**
     * Registers an instance creator for the specified {@link Class}.
     *
     * <p><strong>Note:</strong> Do not register instance creators for primitive types,
     * interface types, abstract types, or array types, as these types do not have meaningful instance creators.
     * Instead, register creators for specific types that may require additional operations during initialization.</p>
     *
     * @param clazz           the specified {@link Class}
     * @param instanceCreator the instance creator for the specified {@link Class}
     * @param <T>             the type of the object created by the instance creator
     */
    public static <T> void add(Class<T> clazz, InstanceCreator<T> instanceCreator) {
        INSTANCE_CREATORS.put(clazz, instanceCreator);
    }

    /**
     * Removes the instance creator for the specified {@link Class}.
     *
     * @param clazz the specified {@link Class}
     * @param <T>   the type of the specified {@link Class}
     */
    public static <T> void remove(Class<T> clazz) {
        INSTANCE_CREATORS.remove(clazz);
    }

    /**
     * a map of all registered instance creators
     *
     * @return a map of all registered instance creators
     */
    public static Map<Class<?>, InstanceCreator<?>> instanceCreators() {
        return INSTANCE_CREATORS;
    }

    /**
     * Retrieves the instance creator for the specified {@link Class}, attempting to find it in the following order:
     * <ol>
     *     <li>If the type has a no-argument constructor, create an instance creator using that constructor.</li>
     *     <li>If the type is a {@link Collection} or {@link Map}, attempt to find a constructor that takes a
     *     {@link Collection} or {@link Map} as a parameter, respectively, in accordance with the definitions of
     *     {@link Collection} and {@link Map}.</li>
     * </ol>
     *
     * @param clazz the specified {@link Class}
     * @param <T>   the type of the specified {@link Class}
     * @return the instance creator for the specified {@link Class}
     */
    private static <T> InstanceCreator<T> findInstanceCreator(Class<T> clazz) {
        return Optional.ofNullable(createByNoArgsConstructor(clazz))
                .or(() -> Optional.ofNullable(createByCollectionOrMapConstructor(clazz)))
                .orElseThrow(() -> new UnableCreateInstanceException(String.format(
                        "Unable to create an instance of %s. Please provide an InstanceCreator for this class.", clazz)));
    }

    /**
     * Creates an instance creator using the no-argument constructor of the specified {@link Class}.
     *
     * @param clazz the specified {@link Class}
     * @param <T>   the type of the object
     * @return the instance creator for the object
     */
    private static <T> InstanceCreator<T> createByNoArgsConstructor(Class<T> clazz) {
        return Optional.ofNullable(Reflections.getDeclaredConstructor(clazz))
                .<InstanceCreator<T>>map(constructor -> () -> Reflections.newInstance(constructor)).orElse(null);
    }

    /**
     * Creates an instance creator using a constructor that follows the definitions of {@link Collection} and {@link Map}.
     *
     * @param clazz the specified {@link Class}
     * @param <T>   the type of the object
     * @return the instance creator for the object
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
     * Determines if the specified {@link Class} is a singleton.
     *
     * @param clazz the specified {@link Class}
     * @return true if it is a singleton, false otherwise
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
