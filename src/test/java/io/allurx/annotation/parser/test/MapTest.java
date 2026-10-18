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

import java.util.LinkedHashMap;
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

    @Test
    void duplicateParsedKeysUseLastValue() {

        var map = new LinkedHashMap<String, String>();
        map.put("first", "1");
        map.put("second", "2");

        var parsed = AnnotationParser.parse(map, new AnnotatedTypeToken<Map<@EraseString String, String>>() {
        });

        Assertions.assertEquals(1, parsed.size());
        Assertions.assertEquals("2", parsed.get("******"));
    }

}
