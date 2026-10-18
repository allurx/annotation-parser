/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */
package io.allurx.annotation.parser.test.handler;

import io.allurx.annotation.parser.handler.AnnotationHandler;
import io.allurx.annotation.parser.test.annotation.EraseString;

/**
 * Handler for processing the {@link EraseString} annotation.
 * This class implements the {@link AnnotationHandler} interface
 * to define the logic for erasing or masking a string input.
 *
 * <p>When invoked, the {@link #handle(String, EraseString)} method
 * replaces the original input with asterisks ("******").</p>
 *
 * @author allurx
 * @see AnnotationHandler
 * @see EraseString
 */
public class EraseStringAnnotationHandler implements AnnotationHandler<String, EraseString, String> {

    /**
     * Handles the annotation by returning a masked version of the input string.
     *
     * @param input      the original string to be processed
     * @param annotation the {@link EraseString} annotation instance
     * @return a string composed of asterisks ("******")
     */
    @Override
    public String handle(String input, EraseString annotation) {
        return "******";
    }
}

