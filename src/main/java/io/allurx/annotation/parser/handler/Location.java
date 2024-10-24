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
package io.allurx.annotation.parser.handler;

import java.lang.reflect.AnnotatedElement;

/**
 * Defines where the annotation appears on the target object.
 *
 * @author allurx
 * @see AnnotatedElement
 */
public enum Location {

    /**
     * Directly present on the target.
     */
    DIRECTLY_PRESENT,

    /**
     * Indirectly present on the target.
     */
    INDIRECTLY_PRESENT,

    /**
     * Present on the target.
     */
    PRESENT,

    /**
     * Associated with the target.
     */
    ASSOCIATED
}
