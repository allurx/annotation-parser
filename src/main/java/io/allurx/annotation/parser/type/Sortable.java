/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */
package io.allurx.annotation.parser.type;

/**
 * An interface representing sortable objects.
 *
 * @author allurx
 */
public interface Sortable {

    /**
     * The highest priority (executed first).
     *
     * @see Integer#MIN_VALUE
     */
    int HIGHEST_PRIORITY = Integer.MIN_VALUE;

    /**
     * The lowest priority (executed last).
     *
     * @see Integer#MAX_VALUE
     */
    int LOWEST_PRIORITY = Integer.MAX_VALUE;

    /**
     * The order value of the object; a higher order value indicates a lower priority.
     * Objects with the same order value will only keep the first one encountered.
     * For example, a {@link TypeParser} with a higher order value will be executed after one with a lower order value.
     * If two {@link TypeParser} instances have the same order value, only the first parser will be retained.
     *
     * @return the order value
     */
    int order();
}
