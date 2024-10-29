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

