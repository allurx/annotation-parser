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

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages singleton detection and instances independently of instance creator resolution.
 *
 * @author allurx
 * @see Singleton
 * @see Instances
 */
public final class Singletons {

    private static final Map<Class<?>, Object> INSTANCES = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Boolean> SINGLETON_MARKS = new ConcurrentHashMap<>();

    private Singletons() {
    }

    /**
     * Determines whether the specified type is marked as a singleton, either directly, through a superclass,
     * or through an implemented interface.
     *
     * @param clazz the type to inspect
     * @return true if the type is a singleton, false otherwise
     */
    public static boolean isSingleton(Class<?> clazz) {
        return Optional.ofNullable(SINGLETON_MARKS.get(clazz)).orElseGet(() -> singleton(clazz));
    }

    /**
     * Returns the singleton for the specified type, creating it atomically when necessary.
     *
     * @param clazz           the singleton type
     * @param instanceCreator the creator used for initialization
     * @param <T>             the type of the singleton
     * @return the singleton instance
     */
    public static <T> T getOrCreate(Class<T> clazz, InstanceCreator<T> instanceCreator) {
        return clazz.cast(INSTANCES.computeIfAbsent(clazz, c -> instanceCreator.create()));
    }

    /**
     * Removes the cached singleton for the specified type.
     *
     * @param clazz the singleton type
     */
    public static void remove(Class<?> clazz) {
        INSTANCES.remove(clazz);
    }

    private static boolean singleton(Class<?> clazz) {
        Class<?>[] interfaces;
        boolean value = clazz != null && clazz != Object.class &&
                ((clazz.isInterface() ? clazz.getDeclaredAnnotation(Singleton.class) != null : clazz.isAnnotationPresent(Singleton.class)) ||
                        ((interfaces = clazz.getInterfaces()).length > 0 && Arrays.stream(interfaces).anyMatch(Singletons::singleton)) ||
                        singleton(clazz.getSuperclass()));
        Optional.ofNullable(clazz).ifPresent(c -> SINGLETON_MARKS.putIfAbsent(c, value));
        return value;
    }
}
