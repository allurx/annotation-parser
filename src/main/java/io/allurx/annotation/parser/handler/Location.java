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
package io.allurx.annotation.parser.handler;

import java.lang.reflect.AnnotatedElement;

/**
 * Defines where the annotation appears on the input object.
 *
 * @author allurx
 * @see AnnotatedElement
 */
public enum Location {

    /**
     * Directly present on the input.
     */
    DIRECTLY_PRESENT,

    /**
     * Indirectly present on the input.
     */
    INDIRECTLY_PRESENT,

    /**
     * Present on the input.
     */
    PRESENT,

    /**
     * Associated with the input.
     */
    ASSOCIATED
}

