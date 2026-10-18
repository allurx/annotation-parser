/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
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
     * How the {@link #annotation()} is present on the input.
     *
     * @return how the {@link #annotation()} is present on the input.
     */
    Location[] location() default Location.DIRECTLY_PRESENT;

}


