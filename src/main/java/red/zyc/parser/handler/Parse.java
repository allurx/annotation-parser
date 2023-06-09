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

package red.zyc.parser.handler;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedType;

/**
 * 探测并处理{@link #annotation()}，该注解有两种使用方式：
 * <ol>
 *     <li>标注在{@link ElementType#TYPE_USE}上时，则根据该类型所代表的{@link Class}探测并处理{@link #annotation()}</li>
 *     <li>标注在注解上时，则根据{@link AnnotatedType}探测并处理{@link #annotation()}</li>
 * </ol>
 *
 * @author zyc
 * @see AnnotationHandler
 * @see Location
 */
@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Parse {

    /**
     * @return {@link #annotation()}的处理器
     */
    Class<? extends AnnotationHandler<?, ? extends Annotation>> handler();

    /**
     * @return 需要被解析的注解类型
     */
    Class<? extends Annotation> annotation();

    /**
     * @return {@link #annotation()}以何种形式存在于对象上
     */
    Location[] location() default Location.PRESENT;

}
