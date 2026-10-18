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

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Test class for parsing annotations within an {@link Array}.
 * This class contains unit tests to verify the behavior of annotation parsing on arrays.
 *
 * @author allurx
 */
class ArrayTest {

    /**
     * Tests the parsing of annotations in a string array.
     * It verifies that the parsed values meet the expected criteria.
     */
    @Test
    void test() {

        // Create a test array of strings.
        var array = new String[]{"123456", "123456", "123456"};

        // Parse the array using AnnotationParser with an AnnotatedTypeToken.
        var parsed = AnnotationParser.parse(array, new AnnotatedTypeToken<@EraseString String[]>() {
        });

        // Verify that each parsed string matches the expected masked value.
        Arrays.stream(parsed).forEach(s -> Assertions.assertEquals("******", s));
    }
}
