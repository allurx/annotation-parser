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

import red.zyc.parser.handler.AnnotationHandler;
import red.zyc.parser.handler.Location;
import red.zyc.parser.handler.Parse;
import red.zyc.parser.support.InstanceCreators;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * <ol>
 *     <li>如果对象上<b>直接存在</b>{@link Parse}注解，则根据该对象的{@link Class}解析该对象</li>
 *     <li>否则找出对象上所有<b>直接存在</b>且被{@link Parse}直接标记的注解，并按照这些注解出现的顺序解析对象</li>
 * </ol>
 *
 * @author zyc
 * @see Parse
 * @see AnnotationHandler
 */
public class ObjectTypeParser implements TypeParser<Object, AnnotatedType> {

    @Override
    public Object parse(Object value, AnnotatedType annotatedType) {
        return Optional.ofNullable(annotatedType.getDeclaredAnnotation(Parse.class))
                .map(parsed -> Stream.of(new ParsedElement(parsed, value.getClass())))
                .orElse(Arrays.stream(annotatedType.getDeclaredAnnotations())
                        .map(annotation -> annotation.annotationType().getDeclaredAnnotation(Parse.class))
                        .filter(Objects::nonNull)
                        .map(parsed -> new ParsedElement(parsed, annotatedType)))
                .map(parsedElement -> locateAnnotation(parsedElement.parse, parsedElement.annotatedElement))
                .reduce(value, (v, metadata) -> metadata.annotations.stream().reduce(v, metadata.annotationHandler::handle, (v1, v2) -> null), (v1, v2) -> null);
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
     * 定位目标对象上符合条件的所有注解
     *
     * @param parse           {@link Parse}
     * @param annotatedElement {@link AnnotatedElement}
     * @return {@link ParsedInfo}
     */
    private ParsedInfo locateAnnotation(Parse parse, AnnotatedElement annotatedElement) {
        @SuppressWarnings("unchecked")
        var annotationHandler = (AnnotationHandler<Object, Annotation>) InstanceCreators.getInstanceCreator(parse.handler()).create();
        var set = new HashSet<Annotation>();
        for (Location location : parse.location()) {
            switch (location) {
                case DIRECTLY_PRESENT ->
                        Optional.ofNullable(annotatedElement.getDeclaredAnnotation(parse.annotation())).ifPresent(set::add);
                case INDIRECTLY_PRESENT ->
                        set.addAll(Arrays.asList(annotatedElement.getDeclaredAnnotationsByType(parse.annotation())));
                case PRESENT ->
                        Optional.ofNullable(annotatedElement.getAnnotation(parse.annotation())).ifPresent(set::add);
                case ASSOCIATED ->
                        set.addAll(Arrays.asList(annotatedElement.getAnnotationsByType(parse.annotation())));
            }
        }
        return new ParsedInfo(set, annotationHandler);
    }

    /**
     * 待解析的元素
     *
     * @param parse           {@link Parse}
     * @param annotatedElement 待解析的元素
     */
    record ParsedElement(Parse parse, AnnotatedElement annotatedElement) {
    }

    /**
     * 解析后的信息
     *
     * @param annotations       符合解析条件的所有注解
     * @param annotationHandler 注解处理器
     */
    record ParsedInfo(HashSet<Annotation> annotations, AnnotationHandler<Object, Annotation> annotationHandler) {
    }

}
