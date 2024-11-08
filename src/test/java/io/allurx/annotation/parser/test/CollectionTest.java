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
package io.allurx.annotation.parser.test;

import io.allurx.annotation.parser.AnnotationParser;
import io.allurx.annotation.parser.test.annotation.EraseString;
import io.allurx.kit.base.reflection.AnnotatedTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Test class for parsing annotations within a {@link Collection}.
 * This class contains unit tests to verify the behavior of annotation parsing
 * on collections, specifically focusing on string masking behavior.
 *
 * @author allurx
 */
class CollectionTest {

    /**
     * Tests the parsing of annotations on a list of strings.
     * It verifies that each string in the list is correctly masked according to
     * the {@link EraseString} annotation.
     */
    @Test
    void test() {

        // Create a list of strings using IntStream.
        var list = IntStream.range(0, 10)
                .mapToObj(value -> "123456")
                .collect(Collectors.toList());

        // Parse the list using AnnotationParser with an AnnotatedTypeToken.
        var parsed = AnnotationParser.parse(list, new AnnotatedTypeToken<List<@EraseString String>>() {
        });

        // Verify that each parsed string is masked.
        parsed.forEach(s -> Assertions.assertEquals("******", s));
    }
}
