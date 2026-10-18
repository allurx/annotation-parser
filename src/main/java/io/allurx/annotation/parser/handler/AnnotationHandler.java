/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
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

