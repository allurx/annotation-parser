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

package red.zyc.parser.type;

import red.zyc.parser.AnnotationParser;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * {@link WildcardType}类型解析器
 *
 * @author zyc
 */
public class WildcardTypeParser implements TypeParser<Object, AnnotatedWildcardType> {

    @Override
    public Object parse(Object value, AnnotatedWildcardType annotatedWildcardType) {
        return Stream.of(annotatedWildcardType.getAnnotatedUpperBounds(), annotatedWildcardType.getAnnotatedLowerBounds())
                .flatMap(Arrays::stream).reduce(value, AnnotationParser::parse, (v1, v2) -> null);
    }

    @Override
    public boolean support(Object value, AnnotatedType annotatedType) {
        return value != null && annotatedType instanceof AnnotatedWildcardType;
    }

    @Override
    public int order() {
        return HIGHEST_PRIORITY + 1;
    }
}
