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

import org.junit.jupiter.api.Test;
import red.zyc.parser.annotation.EraseString;
import red.zyc.parser.type.AnnotatedTypeToken;
import red.zyc.parser.type.Cascade;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 解析对象{@link Field}上的注解
 *
 * @author zyc
 */
public class CascadeTest {

    @Test
    void test() {

        var boy = new Boy("Boy", new Girl("Girl"));
        var parsed = AnnotationParser.parse(boy, new AnnotatedTypeToken<@Cascade Boy>() {
        });

        assertEquals("******", parsed.name);
        assertEquals("******", parsed.girl.name);
    }

    record Boy(@EraseString String name, @Cascade Girl girl) {
    }

    record Girl(@EraseString String name) {
    }
}
