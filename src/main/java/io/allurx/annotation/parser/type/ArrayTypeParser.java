/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
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
    public Object[] parse(Object[] input, AnnotatedArrayType annotatedArrayType) {
        return Arrays.stream(input)
                .map(o -> AnnotationParser.parse(o, annotatedArrayType.getAnnotatedGenericComponentType()))
                .<Object>toArray(length -> Reflections.newArray(input.getClass().getComponentType(), length));
    }

    @Override
    public boolean support(Object input, AnnotatedType annotatedType) {
        return input instanceof Object[] && annotatedType instanceof AnnotatedArrayType;
    }

    @Override
    public int order() {
        return 2;
    }
}
