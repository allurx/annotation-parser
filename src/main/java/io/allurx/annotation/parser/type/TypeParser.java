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

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Array;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.Map;

/**
 * Type parser for parsing different types, such as {@link Collection}, {@link Map}, and {@link Array}.
 * Users can implement this interface to define specific type parsers and register them by calling
 * {@link AnnotationParser#addTypeParser} method.
 * Essentially, any object that needs parsing can be represented by a {@link TypeVariable} or {@link WildcardType},
 * and the object itself may need to be parsed or may require parsing of its internal fields.
 * Therefore, when registering type parsers, the following two conventions must be adhered to:
 *
 * <ol>
 *     <li>
 *         The execution order of type parsers should be later than {@link TypeVariableParser} and {@link WildcardTypeParser}.
 *     </li>
 *     <li>
 *         The execution order of type parsers should be earlier than {@link ObjectTypeParser} and {@link CascadeTypeParser}.
 *     </li>
 * </ol>
 * Otherwise, the parsing results may not be as expected.
 *
 * @param <T>  The type of the object to be parsed
 * @param <AT> {@link AnnotatedType}
 * @author allurx
 * @see CollectionTypeParser
 * @see MapTypeParser
 * @see ArrayTypeParser
 * @see TypeVariableParser
 * @see WildcardTypeParser
 * @see ObjectTypeParser
 * @see CascadeTypeParser
 */
public interface TypeParser<T, AT extends AnnotatedType> extends Sortable, Comparable<TypeParser<?, ? extends AnnotatedType>> {

    /**
     * Parses the object. Subclasses implementing this method should return a new {@link T} instance
     * if they can parse the input <b>whenever possible</b>.
     *
     * @param input         The object to be parsed
     * @param annotatedType The {@link AnnotatedType} of the object to be parsed
     * @return The newly parsed object
     */
    T parse(T input, AT annotatedType);

    /**
     * Determines whether the parser supports parsing the input object.
     *
     * @param input         The object to be parsed
     * @param annotatedType The {@link AnnotatedType} of the object to be parsed
     * @return Whether the parser supports parsing the input object
     */
    boolean support(Object input, AnnotatedType annotatedType);

    /**
     * Execution order of the parser.
     *
     * @param typeParser The parser to compare with
     * @return The execution order of the parser
     */
    @Override
    default int compareTo(TypeParser<?, ? extends AnnotatedType> typeParser) {
        return Integer.compare(order(), typeParser.order());
    }
}
