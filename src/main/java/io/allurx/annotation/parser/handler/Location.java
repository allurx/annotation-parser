/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
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

