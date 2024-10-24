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

/**
 * This class tests the parsing of annotations on a {@link String}
 * to demonstrate how annotations can modify the string representation.
 *
 * @author allurx
 */
class StringTest {

    /**
     * Test method to validate the parsing and modification of a {@link String}
     * annotated with {@link EraseString}.
     * It asserts that the original string "123456" is transformed to "******".
     */
    @Test
    void test() {

        // Original string to be parsed
        var s = "123456";

        // Parse the string with the @EraseString annotation to modify its value
        var parsed = AnnotationParser.parse(s, new AnnotatedTypeToken<@EraseString String>() {
        });

        // Verify that the parsed value is transformed correctly
        Assertions.assertEquals("******", parsed);
    }
}
