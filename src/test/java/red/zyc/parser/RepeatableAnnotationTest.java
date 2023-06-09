/*
 * Copyright 2023 the original author or authors.
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
package red.zyc.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import red.zyc.parser.handler.AnnotationHandler;
import red.zyc.parser.handler.Location;
import red.zyc.parser.handler.Parse;
import red.zyc.parser.type.AnnotatedTypeToken;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 解析{@link Repeatable}注解
 *
 * @author zyc
 */
public class RepeatableAnnotationTest {

    @Test
    void test() {

        int i = 0;
        int parsed = AnnotationParser.parse(i, new AnnotatedTypeToken<@MyAnnotation(1) @MyAnnotation(2) @MyAnnotation(3) Integer>() {
        });

        Assertions.assertEquals(6, parsed);
    }


    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(RepeatableAnnotation.class)
    @Documented
    @interface MyAnnotation {

        int value();
    }

    @Parse(handler = MyAnnotationHandler.class, annotation = MyAnnotation.class, location = Location.INDIRECTLY_PRESENT)
    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface RepeatableAnnotation {

        MyAnnotation[] value();
    }


    static class MyAnnotationHandler implements AnnotationHandler<Integer, MyAnnotation> {

        @Override
        public Integer handle(Integer target, MyAnnotation annotation) {
            return target + annotation.value();
        }
    }

}
