/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */
package io.allurx.annotation.parser.type;

import io.allurx.annotation.parser.AnnotationParser;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

/**
 * {@link TypeVariable} type parser.
 * <p>
 * This class is responsible for parsing annotated type variables,
 * processing the bounds of the type variable based on custom annotations
 * defined on those bounds.
 * </p>
 *
 * @author allurx
 * @see TypeVariable
 */
public class TypeVariableParser implements TypeParser<Object, AnnotatedTypeVariable> {

    /**
     * Default constructor
     */
    public TypeVariableParser() {
    }

    @Override
    public Object parse(Object input, AnnotatedTypeVariable annotatedTypeVariable) {
        return Arrays.stream(annotatedTypeVariable.getAnnotatedBounds())
                .reduce(input, AnnotationParser::parse, (v1, v2) -> null);
    }

    @Override
    public boolean support(Object input, AnnotatedType annotatedType) {
        return input != null && annotatedType instanceof AnnotatedTypeVariable;
    }

    @Override
    public int order() {
        return HIGHEST_PRIORITY;
    }
}
