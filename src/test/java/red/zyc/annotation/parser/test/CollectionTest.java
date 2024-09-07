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
import red.zyc.annotation.parser.test.annotation.EraseString;
import red.zyc.annotation.parser.type.AnnotatedTypeToken;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 解析{@link Collection}中的注解
 *
 * @author zyc
 */
class CollectionTest {

    @Test
    void test() {

        var list = IntStream.range(0, 10).mapToObj(value -> "123456").collect(Collectors.toList());
        var parsed = AnnotationParser.parse(list, new AnnotatedTypeToken<List<@EraseString String>>() {
        });

        parsed.forEach(s -> Assertions.assertEquals("******", s));
    }
}
