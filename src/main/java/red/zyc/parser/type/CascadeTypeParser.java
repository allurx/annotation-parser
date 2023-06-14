/*
 * Copyright 2023 the original author or authors.
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

package red.zyc.parser.type;


import red.zyc.parser.AnnotationParser;
import red.zyc.parser.util.InstanceCreators;
import red.zyc.parser.util.Reflections;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Optional;

/**
 * 级联类型解析器，只会解析被<b>直接</b>标注{@link Cascade}注解的对象，以及其中的非常量{@link Field}。
 *
 * @author zyc
 * @see Cascade
 */
public class CascadeTypeParser implements TypeParser<Object, AnnotatedType> {

    @Override
    public Object parse(Object value, AnnotatedType annotatedType) {
        Class<?> clazz = value.getClass();
        if (clazz.isRecord()) {
            var recordComponents = clazz.getRecordComponents();
            var componentValues = Arrays.stream(recordComponents).map(rc -> AnnotationParser.parse(Reflections.invokeMethod(value, rc.getAccessor()), rc.getAnnotatedType())).toArray();
            var constructor = Optional.ofNullable(Reflections.getDeclaredConstructor(clazz, Arrays.stream(recordComponents).map(RecordComponent::getType).toArray(Class<?>[]::new))).orElseThrow();
            return Reflections.newInstance(constructor, componentValues);
        } else {
            return Reflections.listAllFields(clazz).parallelStream()
                    .filter(field -> !(Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers())))
                    .reduce(InstanceCreators.find(clazz).create(),
                            (o, field) -> {
                                var fieldValue = Reflections.getFieldValue(value, field);
                                Reflections.setFieldValue(o, field, AnnotationParser.parse(fieldValue, field.getAnnotatedType()));
                                return o;
                            }, (o1, o2) -> o1);
        }
    }

    @Override
    public boolean support(Object value, AnnotatedType annotatedType) {
        return value != null && annotatedType.getDeclaredAnnotation(Cascade.class) != null;
    }

    @Override
    public int order() {
        return LOWEST_PRIORITY;
    }
}
