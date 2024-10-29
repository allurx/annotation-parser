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
import io.allurx.annotation.parser.util.Reflections;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Parser for {@link Array} types.
 *
 * @author allurx
 */
public class ArrayTypeParser implements TypeParser<Object[], AnnotatedArrayType> {

    /**
     * Default constructor
     */
    public ArrayTypeParser() {
    }

    @Override
    public Object[] parse(Object[] value, AnnotatedArrayType annotatedArrayType) {
        return Arrays.stream(value)
                .parallel()
                .map(o -> AnnotationParser.parse(o, annotatedArrayType.getAnnotatedGenericComponentType()))
                .<Object>toArray(length -> Reflections.newArray(value.getClass().getComponentType(), length));
    }

    @Override
    public boolean support(Object value, AnnotatedType annotatedType) {
        return value instanceof Object[] && annotatedType instanceof AnnotatedArrayType;
    }

    @Override
    public int order() {
        return 2;
    }

}
