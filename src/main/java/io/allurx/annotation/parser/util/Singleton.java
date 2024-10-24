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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that only one instance of the annotated class can be created
 * during the runtime of an {@link InstanceCreator}. The conditions for this
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
 * @see InstanceCreator
 * @see InstanceCreators
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Singleton {
}
