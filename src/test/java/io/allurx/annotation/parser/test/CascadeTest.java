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
import io.allurx.annotation.parser.type.Cascade;
import io.allurx.kit.base.reflection.AnnotatedTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

/**
 * Test class for parsing annotations on {@link Field} of an object.
 * This class contains unit tests to verify the behavior of annotation parsing
 * with cascade behavior on nested objects.
 *
 * @author allurx
 */
class CascadeTest {

    /**
     * Tests the parsing of annotations on a Boy object and its nested Girl object.
     * It verifies that the parsed names meet the expected masked criteria.
     */
    @Test
    void test() {

        // Create a Boy object with a nested Girl object.
        var boy = new Boy("Boy", new Girl("Girl"));

        // Parse the Boy object using AnnotationParser with an AnnotatedTypeToken.
        var parsed = AnnotationParser.parse(boy, new AnnotatedTypeToken<@Cascade Boy>() {
        });

        // Verify that both the Boy's name and the nested Girl's name are masked.
        Assertions.assertEquals("******", parsed.name);
        Assertions.assertEquals("******", parsed.girl.name);
    }

    @Test
    void ignoredFields() {

        var input = new FieldHolder("123456", "source-final", "source-transient");
        var parsed = AnnotationParser.parse(input, new AnnotatedTypeToken<@Cascade FieldHolder>() {
        });

        Assertions.assertNotSame(input, parsed);
        Assertions.assertEquals("******", parsed.value);
        Assertions.assertEquals("123456", FieldHolder.staticValue);
        Assertions.assertEquals("source-final", parsed.finalValue);
        Assertions.assertEquals("source-transient", parsed.transientValue);
        Assertions.assertEquals("123456", FieldHolder.STATIC_FINAL_VALUE);
    }

    @Test
    void copiesInheritedFieldsWithoutParsingThemByDefault() {

        var input = new Child("source-parent", "source-child");
        var parsed = AnnotationParser.parse(input, new AnnotatedTypeToken<@Cascade Child>() {
        });

        Assertions.assertNotSame(input, parsed);
        Assertions.assertEquals("source-parent", parsed.parentValue);
        Assertions.assertEquals("******", parsed.childValue);
    }

    @Test
    void parsesInheritedFieldsWhenEnabled() {

        var input = new Child("source-parent", "source-child");
        var parsed = AnnotationParser.parse(input, new AnnotatedTypeToken<@Cascade(inherited = true) Child>() {
        });

        Assertions.assertNotSame(input, parsed);
        Assertions.assertEquals("******", parsed.parentValue);
        Assertions.assertEquals("******", parsed.childValue);
    }

    /**
     * A record representing a Boy with a name and a nested Girl.
     *
     * @param name The name of the boy, annotated with {@link EraseString}.
     * @param girl The Girl object, annotated with {@link Cascade}.
     */
    record Boy(@EraseString String name, @Cascade Girl girl) {
    }

    /**
     * A record representing a Girl with a name.
     *
     * @param name The name of the girl, annotated with {@link EraseString}.
     */
    record Girl(@EraseString String name) {
    }

    static class FieldHolder {

        static @EraseString String staticValue = "123456";

        static final @EraseString String STATIC_FINAL_VALUE = "123456";

        final @EraseString String finalValue;

        transient @EraseString String transientValue;

        @EraseString
        String value;

        FieldHolder() {
            this("default-value", "default-final", "default-transient");
        }

        FieldHolder(String value, String finalValue, String transientValue) {
            this.value = value;
            this.finalValue = finalValue;
            this.transientValue = transientValue;
        }
    }

    static class Parent {

        @EraseString
        String parentValue;

        Parent() {
            this("default-parent");
        }

        Parent(String parentValue) {
            this.parentValue = parentValue;
        }
    }

    static class Child extends Parent {

        @EraseString
        String childValue;

        Child() {
            this("default-parent", "default-child");
        }

        Child(String parentValue, String childValue) {
            super(parentValue);
            this.childValue = childValue;
        }
    }
}
