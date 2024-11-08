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
import io.allurx.annotation.parser.util.InstanceCreators;
import io.allurx.annotation.parser.util.Reflections;
import io.allurx.kit.base.Conditional;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;

/**
 * Cascade type parser, which only processes objects directly annotated with {@link Cascade}
 * and their non-constant {@link Field}s.
 *
 * @author allurx
 * @see Cascade
 */
public class CascadeTypeParser implements TypeParser<Object, AnnotatedType> {

    /**
     * Default constructor
     */
    public CascadeTypeParser() {
    }

    @Override
    public Object parse(Object input, AnnotatedType annotatedType) {
        return Conditional.of(input.getClass())
                .when(Class::isRecord)
                .map(clazz -> {
                    var recordComponents = clazz.getRecordComponents();
                    var componentValues = Arrays.stream(recordComponents)
                            .map(rc -> AnnotationParser.parse(Reflections.invokeMethod(input, rc.getAccessor()), rc.getAnnotatedType()))
                            .toArray();
                    var constructor = Reflections.getDeclaredConstructor(clazz,
                                    Arrays.stream(recordComponents)
                                            .map(RecordComponent::getType)
                                            .toArray(Class<?>[]::new))
                            .orElseThrow();
                    return Reflections.newInstance(constructor, componentValues);
                })
                .elseIf(Class::isEnum)
                .map(clazz -> input)
                .orElse()
                .map(clazz -> Reflections.listFields(clazz, annotatedType.getDeclaredAnnotation(Cascade.class).inherited())
                        .parallelStream()
                        .filter(field -> !(Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers())))
                        .reduce(InstanceCreators.find(clazz).create(),
                                (o, field) -> {
                                    var fieldValue = Reflections.getFieldValue(input, field);
                                    Reflections.setFieldValue(o, field, AnnotationParser.parse(fieldValue, field.getAnnotatedType()));
                                    return o;
                                }, (o1, o2) -> o1))
                .get();
    }

    @Override
    public boolean support(Object input, AnnotatedType annotatedType) {
        return input != null && annotatedType.getDeclaredAnnotation(Cascade.class) != null;
    }

    @Override
    public int order() {
        return LOWEST_PRIORITY;
    }
}
