/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */
package io.allurx.annotation.parser.util;

/**
 * Creates instances by coordinating creator resolution and lifecycle policies.
 *
 * @author allurx
 * @see InstanceCreators
 * @see Singletons
 */
public final class Instances {

    private Instances() {
    }

    /**
     * Creates or obtains an instance of the specified type. Types marked with {@link Singleton} are obtained from
     * {@link Singletons}; all other types are created directly by their registered or discovered creator.
     *
     * @param clazz the type to create
     * @param <T>   the type of the instance
     * @return the created or cached instance
     */
    public static <T> T create(Class<T> clazz) {
        var instanceCreator = InstanceCreators.find(clazz);
        return Singletons.isSingleton(clazz)
                ? Singletons.getOrCreate(clazz, instanceCreator)
                : instanceCreator.create();
    }
}
