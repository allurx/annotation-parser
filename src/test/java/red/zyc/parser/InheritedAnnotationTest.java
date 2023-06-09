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
import red.zyc.parser.handler.Parse;
import red.zyc.parser.type.AnnotatedTypeToken;

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
        var parsed = AnnotationParser.parse(sub, new AnnotatedTypeToken<@Parse(handler = MyAnnotationHandler.class, annotation = MyAnnotation.class) Sub>() {
        });

        Assertions.assertEquals(1, parsed.i);
    }


    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @interface MyAnnotation {

        int value();
    }

    static class Sub extends Super {
        int i = 0;
    }

    @MyAnnotation(1)
    static class Super {

    }

    static class MyAnnotationHandler implements AnnotationHandler<Sub, MyAnnotation> {

        @Override
        public Sub handle(Sub target, MyAnnotation annotation) {
            target.i += annotation.value();
            return target;
        }
    }
}
