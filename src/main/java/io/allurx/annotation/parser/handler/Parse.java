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
package io.allurx.annotation.parser.handler;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Detects and processes the {@link #annotation()}.
 *
 * @author allurx
 * @see AnnotationHandler
 * @see Location
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Parse {

    /**
     * The handler for the {@link #annotation()}.
     *
     * @return the handler for the {@link #annotation()}.
     */
    Class<? extends AnnotationHandler<?, ? extends Annotation, ?>> handler();

    /**
     * The type of annotation to be parsed.
     *
     * @return the type of annotation to be parsed.
     */
    Class<? extends Annotation> annotation();

    /**
     * How the {@link #annotation()} is present on the target object.
     *
     * @return how the {@link #annotation()} is present on the target object.
     */
    Location[] location() default Location.DIRECTLY_PRESENT;

}

