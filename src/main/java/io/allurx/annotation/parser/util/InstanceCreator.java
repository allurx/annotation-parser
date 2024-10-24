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

/**
 * This interface allows users to define specific instance creators for particular types.
 * For example, consider the following class definition:
 * <pre>
 *      class Person {
 *          Person(your constructor parameters) {
 *              ...
 *          }
 *      }
 * </pre>
 * In the `Person` class definition, there is only one parameterized constructor and no no-argument constructor.
 * Therefore, at runtime, the program can determine the presence of a parameterized constructor but does not know
 * what parameters to pass to invoke it. It is recommended to add a no-argument constructor to your class
 * to allow the program to initialize the object via reflection. Alternatively, you can implement this interface
 * to define a default creator for the `Person` class:
 * <pre>
 *     class PersonInstanceCreator implements InstanceCreator&lt;Person&gt; {
 *
 *          &#64;Override
 *          public Person create() {
 *              return new Person(your constructor parameters);
 *          }
 *
 *     }
 * </pre>
 * After that, you can call {@link InstanceCreators#add} to register this instance creator,
 * and the program will use the registered creator to instantiate the corresponding object at runtime.
 *
 * @param <T> the type of the instance
 * @author allurx
 * @see Singleton
 */
@FunctionalInterface
public interface InstanceCreator<T> {

    /**
     * Creates an instance of the specified type.
     *
     * @return an instance of type {@link T}
     */
    T create();
}
