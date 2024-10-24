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
package io.allurx.annotation.parser.util;

/**
 * Custom exception class for handling reflection-related errors.
 * This exception extends {@link RuntimeException} and does not need to be
 * declared in a method's `throws` clause.
 *
 * @author allurx
 */
public class ReflectionException extends RuntimeException {

    /**
     * Constructs a new {@code ReflectionException} with the specified detail message
     * and cause.
     *
     * @param message the detail message
     * @param cause   the cause (can be null)
     */
    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

}
