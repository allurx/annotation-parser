/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
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
 * @see InstanceCreators
 * @see Instances
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
