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
package io.allurx.annotation.parser;

import io.allurx.annotation.parser.type.ArrayTypeParser;
import io.allurx.annotation.parser.type.CascadeTypeParser;
import io.allurx.annotation.parser.type.CollectionTypeParser;
import io.allurx.annotation.parser.type.MapTypeParser;
import io.allurx.annotation.parser.type.ObjectTypeParser;
import io.allurx.annotation.parser.type.Sortable;
import io.allurx.annotation.parser.type.TypeParser;
import io.allurx.annotation.parser.type.TypeVariableParser;
import io.allurx.annotation.parser.type.WildcardTypeParser;
import io.allurx.kit.base.reflection.AnnotatedTypeToken;

import java.lang.reflect.AnnotatedType;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Entry point for annotation parsing, providing various useful helper methods.
 * <ol>
 *     <li>{@link AnnotationParser#parse(Object, AnnotatedTypeToken) Parses an input based on the AnnotatedTypeToken}</li>
 *     <li>{@link AnnotationParser#parse(Object, AnnotatedType) Parses an input based on the AnnotatedType}</li>
 *     <li>{@link AnnotationParser#addTypeParser Adds a custom type parser}</li>
 *     <li>{@link AnnotationParser#removeTypeParser Removes a registered type parser}</li>
 *     <li>{@link AnnotationParser#randomOrder Generates a random order value that does not conflict with registered type parsers' order}</li>
 *     <li>{@link AnnotationParser#typeParsers Retrieves all currently registered type parsers}</li>
 * </ol>
 *
 * @author allurx
 */
public final class AnnotationParser {

    /**
     * A synchronized sorted set of all registered {@link TypeParser} instances.
     */
    private static final SortedSet<TypeParser<?, ? extends AnnotatedType>> TYPE_PARSERS =
            Collections.synchronizedSortedSet(new TreeSet<>());

    // Register default type parsers
    static {
        addTypeParser(new TypeVariableParser());
        addTypeParser(new WildcardTypeParser());
        addTypeParser(new CollectionTypeParser());
        addTypeParser(new MapTypeParser());
        addTypeParser(new ArrayTypeParser());
        addTypeParser(new ObjectTypeParser());
        addTypeParser(new CascadeTypeParser());
    }

    private AnnotationParser() {
    }

    /**
     * A wrapper method for {@link #parse(Object, AnnotatedType)}.
     *
     * @param input              The input object to be parsed.
     * @param annotatedTypeToken The {@link AnnotatedTypeToken} of the object.
     * @param <T>                The type of the object to be parsed.
     * @return The parsed object.
     */
    public static <T> T parse(T input, AnnotatedTypeToken<T> annotatedTypeToken) {
        return parse(input, annotatedTypeToken.getAnnotatedType());
    }

    /**
     * Parses an input that may inherit special data types, such as {@link Collection} or {@link Map}.
     * This method iterates through all registered parsers and uses any parser that supports the given input.
     *
     * @param input         The input object to be parsed.
     * @param annotatedType The {@link AnnotatedType} of the object.
     * @param <T>           The type of the object to be parsed.
     * @param <AT>          The type of the {@link AnnotatedType}.
     * @return The parsed object.
     */
    @SuppressWarnings("unchecked")
    public static <T, AT extends AnnotatedType> T parse(T input, AT annotatedType) {
        return TYPE_PARSERS.stream()
                .filter(tp -> tp.support(input, annotatedType))
                .reduce(input, (v, tp) -> ((TypeParser<T, AT>) tp).parse(v, annotatedType), (v1, v2) -> null);
    }

    /**
     * Registers a custom type parser.
     * <br>
     * Note: If the {@link Sortable#order()} method of the type parser returns a value already used by another parser,
     * this parser will be ignored due to the behavior of {@link TreeSet}.
     *
     * @param typeParser The type parser to register.
     */
    public static void addTypeParser(TypeParser<?, ? extends AnnotatedType> typeParser) {
        TYPE_PARSERS.add(typeParser);
    }

    /**
     * Removes the specified type parser from the registered parsers.
     *
     * @param typeParser The type parser to be removed.
     */
    public static void removeTypeParser(TypeParser<?, ? extends AnnotatedType> typeParser) {
        TYPE_PARSERS.remove(typeParser);
    }

    /**
     * Generates a random order value that does not conflict with the order of registered type parsers.
     *
     * @return A random order value that does not conflict with registered type parsers' order values.
     * <p><strong>Note: Do not return this method directly in the {@link Sortable#order()} method.
     * Instead, assign the return value to an instance or static variable before returning it.
     * If the order is returned directly, it can lead to infinite recursion when other parsers call this method
     * to determine a unique order value.</strong></p>
     */
    public static int randomOrder() {
        int order = ThreadLocalRandom.current().nextInt(Sortable.HIGHEST_PRIORITY, Sortable.LOWEST_PRIORITY);
        synchronized (TYPE_PARSERS) {
            return TYPE_PARSERS.stream().noneMatch(parser -> parser.order() == order) ? order : randomOrder();
        }
    }

    /**
     * Retrieves all currently registered type parsers.
     *
     * @return A set of all registered type parsers.
     * The result is a {@link SortedSet} wrapped by {@link Collections#synchronizedSortedSet(SortedSet)},
     * providing thread-safe access.
     */
    public static SortedSet<TypeParser<?, ? extends AnnotatedType>> typeParsers() {
        return TYPE_PARSERS;
    }
}
