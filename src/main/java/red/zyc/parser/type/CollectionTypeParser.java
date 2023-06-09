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
import red.zyc.parser.support.InstanceCreators;
import red.zyc.parser.util.Reflections;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 泛型{@link Collection}类型解析器
 *
 * @author zyc
 */
public class CollectionTypeParser implements TypeParser<Collection<Object>, AnnotatedParameterizedType> {

    @Override
    public Collection<Object> parse(Collection<Object> value, AnnotatedParameterizedType annotatedParameterizedType) {
        return value.parallelStream().map(o -> AnnotationParser.parse(o, annotatedParameterizedType.getAnnotatedActualTypeArguments()[0]))
                .collect(Collectors.toCollection(() -> InstanceCreators.getInstanceCreator(Reflections.getClass(value)).create()));
    }

    @Override
    public boolean support(Object value, AnnotatedType annotatedType) {
        return value instanceof Collection && annotatedType instanceof AnnotatedParameterizedType;
    }

    @Override
    public int order() {
        return 0;
    }
}
