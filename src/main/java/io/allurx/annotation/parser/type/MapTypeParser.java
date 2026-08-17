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
import io.allurx.annotation.parser.util.Instances;
import io.allurx.annotation.parser.util.Reflections;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.util.Map;

/**
 * Generic {@link Map} type parser.
 *
 * @author allurx
 */
public class MapTypeParser implements TypeParser<Map<Object, Object>, AnnotatedParameterizedType> {

    /**
     * Default constructor
     */
    public MapTypeParser() {
    }

    @Override
    public Map<Object, Object> parse(Map<Object, Object> input, AnnotatedParameterizedType annotatedParameterizedType) {
        AnnotatedType[] annotatedActualTypeArguments = annotatedParameterizedType.getAnnotatedActualTypeArguments();
        Map<Object, Object> parsed = Instances.create(Reflections.getClass(input));
        input.forEach((key, value) -> parsed.put(
                AnnotationParser.parse(key, annotatedActualTypeArguments[0]),
                AnnotationParser.parse(value, annotatedActualTypeArguments[1])
        ));
        return parsed;
    }

    @Override
    public boolean support(Object input, AnnotatedType annotatedType) {
        return input instanceof Map && annotatedType instanceof AnnotatedParameterizedType;
    }

    @Override
    public int order() {
        return 1;
    }
}
