/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
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

