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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 满足以下条件之一，{@link InstanceCreator}运行期间只会创建一个这个类的实例。
 * <ul>
 *     <li>{@link Class}上<b>存在</b>该注解</li>
 *     <li>{@link Class#getInterfaces()}中的任意一个类上<b>存在</b>该注解</li>
 * </ul>
 *
 * @author zyc
 * @see InstanceCreator
 * @see InstanceCreators
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Singleton {
}
