/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */
package io.allurx.annotation.parser.type;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Indicates that the annotated object requires parsing of its {@link Field}s.
 *
 * @author allurx
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE_USE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cascade {

    /**
     * Determines whether to parse {@link Field}s inherited from parent classes.
     *
     * @return true if inherited {@link Field}s should be parsed, false otherwise
     */
    boolean inherited() default false;
}
