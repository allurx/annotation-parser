/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */
package io.allurx.annotation.parser.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that only one instance of the annotated class is returned by {@link Instances}
 * until it is removed from {@link Singletons}. The conditions for this
 * are as follows:
 * <ul>
 *     <li>The annotation is <b>present</b> on the {@link Class} itself.</li>
 *     <li>The annotation is <b>directly present</b> on any interface listed in
 *         {@link Class#getInterfaces()} or any interface recursively implemented.</li>
 *     <li>The annotation is <b>directly present</b> on any interface in the
 *         {@link Class#getInterfaces()} of all parent classes, or any recursively implemented interfaces.</li>
 * </ul>
 * <p>Note: Annotations marked with {@link Inherited} are only inherited by
 * classes; interfaces do not inherit annotations from other interfaces.</p>
 *
 * @author allurx
 * @see Instances
 * @see Singletons
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Singleton {
}
