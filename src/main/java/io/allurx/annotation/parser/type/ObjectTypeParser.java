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

import io.allurx.annotation.parser.handler.AnnotationHandler;
import io.allurx.annotation.parser.handler.Location;
import io.allurx.annotation.parser.handler.Parse;
import io.allurx.annotation.parser.util.InstanceCreators;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

/**
 * Finds all annotations marked with {@link Parse} on the object and parses the object in the order these annotations appear.
 *
 * @author allurx
 * @see Parse
 * @see AnnotationHandler
 */
public class ObjectTypeParser implements TypeParser<Object, AnnotatedType> {

    /**
     * Default constructor
     */
    public ObjectTypeParser() {
    }

    @Override
    public Object parse(Object value, AnnotatedType annotatedType) {
        return Arrays.stream(annotatedType.getDeclaredAnnotations())
                .map(annotation -> annotation.annotationType().getDeclaredAnnotation(Parse.class))
                .filter(Objects::nonNull)
                .map(parse -> parseAnnotation(value, annotatedType, parse))
                .reduce(value, (o, parsedInfo) -> parsedInfo.annotations.stream()
                                .reduce(value, parsedInfo.annotationHandler::handle, (v1, v2) -> null),
                        (v1, v2) -> null);
    }

    @Override
    public boolean support(Object value, AnnotatedType annotatedType) {
        return value != null;
    }

    @Override
    public int order() {
        return LOWEST_PRIORITY - 1;
    }

    /**
     * Parses all annotations on the target object that meet the specified conditions.
     *
     * @param value         The object to be parsed.
     * @param annotatedType {@link AnnotatedType}
     * @param parse         {@link Parse}
     * @return {@link ParsedInfo} containing parsed annotations and their handler.
     */
    private ParsedInfo parseAnnotation(Object value, AnnotatedType annotatedType, Parse parse) {
        @SuppressWarnings("unchecked")
        var annotationHandler = (AnnotationHandler<Object, Annotation, Object>) InstanceCreators.find(parse.handler()).create();
        var set = new HashSet<Annotation>();
        for (Location location : parse.location()) {
            switch (location) {
                case DIRECTLY_PRESENT ->
                        Optional.ofNullable(annotatedType.getDeclaredAnnotation(parse.annotation())).ifPresent(set::add);
                case INDIRECTLY_PRESENT ->
                        set.addAll(Arrays.asList(annotatedType.getDeclaredAnnotationsByType(parse.annotation())));
                case PRESENT -> {
                    Optional.ofNullable(annotatedType.getAnnotation(parse.annotation())).ifPresent(set::add);
                    Optional.ofNullable(value.getClass().getAnnotation(parse.annotation())).ifPresent(set::add);
                }
                case ASSOCIATED -> {
                    set.addAll(Arrays.asList(annotatedType.getAnnotationsByType(parse.annotation())));
                    set.addAll(Arrays.asList(value.getClass().getAnnotationsByType(parse.annotation())));
                }
            }
        }
        return new ParsedInfo(set, annotationHandler);
    }

    /**
     * Information about parsed annotations.
     *
     * @param annotations       All annotations that meet the parsing conditions.
     * @param annotationHandler The handler for the annotations.
     */
    record ParsedInfo(HashSet<Annotation> annotations,
                      AnnotationHandler<Object, Annotation, Object> annotationHandler) {
    }
}