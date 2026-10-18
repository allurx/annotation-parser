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
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Generic {@link Collection} type parser.
 *
 * @author allurx
 */
public class CollectionTypeParser implements TypeParser<Collection<Object>, AnnotatedParameterizedType> {

    /**
     * Default constructor
     */
    public CollectionTypeParser() {
    }

    @Override
    public Collection<Object> parse(Collection<Object> input, AnnotatedParameterizedType annotatedParameterizedType) {
        return input.stream()
                .map(o -> AnnotationParser.parse(o, annotatedParameterizedType.getAnnotatedActualTypeArguments()[0]))
                .collect(Collectors.toCollection(() -> Instances.create(Reflections.getClass(input))));
    }

    @Override
    public boolean support(Object input, AnnotatedType annotatedType) {
        return input instanceof Collection && annotatedType instanceof AnnotatedParameterizedType;
    }

    @Override
    public int order() {
        return 0;
    }
}
