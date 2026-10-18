/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
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
