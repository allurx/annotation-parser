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
package red.zyc.annotation.parser.type;

import red.zyc.annotation.parser.AnnotationParser;
import red.zyc.annotation.parser.util.Reflections;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * {@link Array}类型解析器
 *
 * @author allurx
 */
public class ArrayTypeParser implements TypeParser<Object[], AnnotatedArrayType> {

    @Override
    public Object[] parse(Object[] value, AnnotatedArrayType annotatedArrayType) {
        return Arrays.stream(value).parallel().map(o -> AnnotationParser.parse(o, annotatedArrayType.getAnnotatedGenericComponentType()))
                .<Object>toArray(length -> Reflections.newArray(value.getClass().getComponentType(), length));
    }

    @Override
    public boolean support(Object value, AnnotatedType annotatedType) {
        return value instanceof Object[] && annotatedType instanceof AnnotatedArrayType;
    }

    @Override
    public int order() {
        return 2;
    }

}
