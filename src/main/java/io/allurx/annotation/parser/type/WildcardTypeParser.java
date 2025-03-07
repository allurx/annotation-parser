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
package io.allurx.annotation.parser.type;

import io.allurx.annotation.parser.AnnotationParser;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * {@link WildcardType} type parser.
 * <p>
 * This class is responsible for parsing annotated wildcard types,
 * processing both upper and lower bounds of the wildcard type with custom logic
 * based on annotations defined on these bounds.
 * </p>
 *
 * @author allurx
 * @see WildcardType
 */
public class WildcardTypeParser implements TypeParser<Object, AnnotatedWildcardType> {

    /**
     * Default constructor
     */
    public WildcardTypeParser() {
    }

    @Override
    public Object parse(Object input, AnnotatedWildcardType annotatedWildcardType) {
        return Stream.of(annotatedWildcardType.getAnnotatedUpperBounds(), annotatedWildcardType.getAnnotatedLowerBounds())
                .flatMap(Arrays::stream)
                .reduce(input, AnnotationParser::parse, (v1, v2) -> null);
    }

    @Override
    public boolean support(Object input, AnnotatedType annotatedType) {
        return input != null && annotatedType instanceof AnnotatedWildcardType;
    }

    @Override
    public int order() {
        return HIGHEST_PRIORITY + 1;
    }
}
