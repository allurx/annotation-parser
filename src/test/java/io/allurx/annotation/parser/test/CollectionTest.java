/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
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
