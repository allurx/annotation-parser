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

package io.allurx.annotation.parser.test.annotation;

import io.allurx.annotation.parser.handler.Parse;
import io.allurx.annotation.parser.test.handler.EraseStringAnnotationHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate that the annotated {@link String}
 * should be erased or masked when processed.
 * <p>
 * This annotation is intended for use with the {@link EraseStringAnnotationHandler}
 * to handle the logic for erasing or modifying string representations at runtime.
 *
 * @author allurx
 */
@Target({ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parse(handler = EraseStringAnnotationHandler.class, annotation = EraseString.class)
public @interface EraseString {
}

