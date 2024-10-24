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
package io.allurx.annotation.parser.test;

import io.allurx.annotation.parser.AnnotationParser;
import io.allurx.annotation.parser.test.annotation.EraseString;
import io.allurx.kit.base.reflection.AnnotatedTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class is designed to test the parsing of annotations
 * within a {@link Map} using a specified type token.
 * It verifies that values in the map are correctly processed
 * according to the annotation rules defined by {@link EraseString}.
 *
 * @author allurx
 */
class MapTest {

    /**
     * Test method to validate the annotation parsing from a map.
     * It creates a map with integers as keys and their string
     * representations as values, then parses it to replace
     * string values annotated with {@link EraseString}
     * with asterisks.
     */
    @Test
    void test() {

        // Create a map with integer keys and their string representations as values
        var map = IntStream.range(0, 10).boxed().collect(Collectors.toMap(Function.identity(), Object::toString));

        // Parse the map with the specified type token, applying the annotations
        var parsed = AnnotationParser.parse(map, new AnnotatedTypeToken<Map<Integer, @EraseString String>>() {
        });

        // Verify that all values in the parsed map are replaced with "******"
        parsed.forEach((k, v) -> Assertions.assertEquals("******", v));
    }
}
