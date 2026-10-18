/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
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
