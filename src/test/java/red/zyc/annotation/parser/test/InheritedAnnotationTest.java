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
package red.zyc.annotation.parser.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import red.zyc.annotation.parser.AnnotationParser;
import red.zyc.annotation.parser.handler.AnnotationHandler;
import red.zyc.annotation.parser.handler.Location;
import red.zyc.annotation.parser.handler.Parse;
import red.zyc.annotation.parser.type.AnnotatedTypeToken;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 解析{@link Inherited}注解
 *
 * @author zyc
 */
public class InheritedAnnotationTest {

    @Test
    void test() {
        var sub = new Sub();
        var parsed = AnnotationParser.parse(sub, new AnnotatedTypeToken<@Accumulator(1) Sub>() {
        });

        Assertions.assertEquals(3, parsed.i);
    }

    @Parse(handler = AccumulatorHandler.class, annotation = Accumulator.class, location = Location.PRESENT)
    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @interface Accumulator {

        int value();
    }

    static class Sub extends Super {
        int i = 0;
    }

    @Accumulator(2)
    static class Super {

    }

    static class AccumulatorHandler implements AnnotationHandler<Sub, Accumulator, Sub> {

        @Override
        public Sub handle(Sub sub, Accumulator annotation) {
            sub.i += annotation.value();
            return sub;
        }
    }
}
