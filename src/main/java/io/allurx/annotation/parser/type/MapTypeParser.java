/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
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
