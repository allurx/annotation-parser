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
