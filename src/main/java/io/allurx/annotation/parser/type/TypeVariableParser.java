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
package io.allurx.annotation.parser.type;

import io.allurx.annotation.parser.AnnotationParser;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

/**
 * {@link TypeVariable} type parser.
 * <p>
 * This class is responsible for parsing annotated type variables.
 * It processes the bounds of the type variable, allowing for custom parsing
 * based on the annotations defined on those bounds.
 *
 * @author allurx
 */
public class TypeVariableParser implements TypeParser<Object, AnnotatedTypeVariable> {

    /**
     * Default constructor
     */
    public TypeVariableParser() {
    }

    @Override
    public Object parse(Object value, AnnotatedTypeVariable annotatedTypeVariable) {
        return Arrays.stream(annotatedTypeVariable.getAnnotatedBounds())
                .reduce(value, AnnotationParser::parse, (v1, v2) -> null);
    }

    @Override
    public boolean support(Object value, AnnotatedType annotatedType) {
        return value != null && annotatedType instanceof AnnotatedTypeVariable;
    }

    @Override
    public int order() {
        return HIGHEST_PRIORITY;
    }
}