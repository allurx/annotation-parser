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
import io.allurx.annotation.parser.handler.AnnotationHandler;
import io.allurx.annotation.parser.handler.Location;
import io.allurx.annotation.parser.handler.Parse;
import io.allurx.kit.base.reflection.AnnotatedTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tests the parsing of the {@link Accumulator} annotation.
 * <p>
 * This test verifies that the value from the inherited {@link Accumulator} annotation
 * on the superclass is correctly added to the field in the subclass.
 * </p>
 *
 * @author allurx
 */
class InheritedAnnotationTest {

    @Test
    void test() {
        var sub = new Sub();
        var parsed = AnnotationParser.parse(sub, new AnnotatedTypeToken<@Accumulator(1) Sub>() {
        });

        Assertions.assertEquals(3, parsed.i);
    }

    /**
     * Annotation to accumulate a value.
     * <p>
     * This annotation can be applied to type uses and is inherited by subclasses.
     * </p>
     */
    @Parse(handler = AccumulatorHandler.class, annotation = Accumulator.class, location = Location.PRESENT)
    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @interface Accumulator {
        int value();
    }

    /**
     * Subclass that extends {@link Super}.
     * <p>
     * Contains an integer field that will accumulate values from the {@link Accumulator} annotation.
     * </p>
     */
    static class Sub extends Super {
        int i = 0;
    }

    /**
     * Superclass with an {@link Accumulator} annotation.
     * <p>
     * Inherits the accumulated value from this annotation in subclasses.
     * </p>
     */
    @Accumulator(2)
    static class Super {
    }

    /**
     * Handler for processing the {@link Accumulator} annotation.
     * <p>
     * This handler adds the value from the annotation to the integer field of the subclass.
     * </p>
     */
    static class AccumulatorHandler implements AnnotationHandler<Sub, Accumulator, Sub> {
        @Override
        public Sub handle(Sub sub, Accumulator annotation) {
            sub.i += annotation.value();
            return sub;
        }
    }
}
