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

import io.allurx.annotation.parser.util.Singleton;

import java.lang.annotation.Annotation;

/**
 * Handler for processing annotations on target objects.
 *
 * @param <T> the type of the target object
 * @param <A> the type of the annotation present on the target object
 * @param <R> the type of the result produced by the handler
 * @author allurx
 * @see Parse
 */
@Singleton
public interface AnnotationHandler<T, A extends Annotation, R> {

    /**
     * Processes the target object and its associated annotation.
     *
     * @param target     the object to be processed
     * @param annotation the annotation present on the target object
     * @return the result of the processing
     */
    R handle(T target, A annotation);
}
