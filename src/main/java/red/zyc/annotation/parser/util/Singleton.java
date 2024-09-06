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
package red.zyc.annotation.parser.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 满足以下条件之一，{@link InstanceCreator}运行期间只会创建一个这个类的实例。
 * <ul>
 *     <li>{@link Class}上<b>存在</b>该注解</li>
 *     <li>{@link Class#getInterfaces()}中任意一个接口或者该接口递归实现的接口上<b>直接存在</b>该注解</li>
 *     <li>所有父类的{@link Class#getInterfaces()}中任意一个接口或者该接口递归实现的接口上<b>直接存在</b>该注解</li>
 * </ul>
 * 注意：{@link Inherited}标注的注解只会被{@code class}继承，{@code interface}之间无法继承。
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
