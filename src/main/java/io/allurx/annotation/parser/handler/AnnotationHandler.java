/*
 * Copyright 2024 allurx
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
 * Handler for processing annotations on input object.
 *
 * @param <T> the type of the input
 * @param <A> the type of the annotation present on the input object
 * @param <R> the type of the result produced by the handler
 * @author allurx
 * @see Parse
 */
@Singleton
public interface AnnotationHandler<T, A extends Annotation, R> {

    /**
     * Processes the input and its associated annotation.
     *
     * @param input      the object to be processed
     * @param annotation the annotation present on the input
     * @return the result of the processing
     */
    R handle(T input, A annotation);
}

