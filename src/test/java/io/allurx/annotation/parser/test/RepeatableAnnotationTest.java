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
import io.allurx.annotation.parser.handler.AnnotationHandler;
import io.allurx.annotation.parser.handler.Location;
import io.allurx.annotation.parser.handler.Parse;
import io.allurx.kit.base.reflection.AnnotatedTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This class tests the parsing of the {@link Repeatable} annotation
 * by summing values from multiple instances of the {@link Accumulator} annotation.
 *
 * @author allurx
 */
class RepeatableAnnotationTest {

    /**
     * Test method to validate the parsing and summation of values
     * from repeatable {@link Accumulator} annotations.
     * It parses an integer with multiple {@link Accumulator} annotations
     * and asserts that the resulting sum equals 6.
     */
    @Test
    void test() {

        int i = 0;

        // Parse the integer with multiple @Accumulator annotations
        int parsed = AnnotationParser.parse(i, new AnnotatedTypeToken<@Accumulator(1) @Accumulator(2) @Accumulator(3) Integer>() {
        });

        // Verify that the sum of accumulator values is correct
        Assertions.assertEquals(6, parsed);
    }

    /**
     * Annotation to accumulate integer values.
     * Can be used multiple times due to {@link Repeatable}.
     */
    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(RepeatableAnnotation.class)
    @Documented
    @interface Accumulator {
        int value();
    }

    /**
     * Wrapper annotation for repeatable {@link Accumulator} annotations.
     */
    @Parse(handler = AccumulatorHandler.class, annotation = Accumulator.class, location = Location.INDIRECTLY_PRESENT)
    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface RepeatableAnnotation {
        Accumulator[] value();
    }

    /**
     * Handler for processing {@link Accumulator} annotations.
     * It sums the target integer with the value of the annotation.
     */
    static class AccumulatorHandler implements AnnotationHandler<Integer, Accumulator, Integer> {

        @Override
        public Integer handle(Integer target, Accumulator annotation) {
            return target + annotation.value();
        }
    }
}

