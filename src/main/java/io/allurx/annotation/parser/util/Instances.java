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
