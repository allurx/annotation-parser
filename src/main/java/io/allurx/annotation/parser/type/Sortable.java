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
