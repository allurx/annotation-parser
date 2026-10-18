/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */
package io.allurx.annotation.parser.util;

/**
 * Exception thrown when an instance cannot be created.
 * This exception extends {@link RuntimeException} to indicate
 * that it is an unchecked exception, which may occur during the
 * instantiation process of a class.
 *
 * @author allurx
 */
public class UnableCreateInstanceException extends RuntimeException {

    /**
     * Constructs a new UnableCreateInstanceException with the specified detail message.
     *
     * @param message the detail message that explains the reason for the exception
     */
    public UnableCreateInstanceException(String message) {
        super(message);
    }
}
