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
package red.zyc.annotation.parser;

import red.zyc.annotation.parser.type.AnnotatedTypeToken;
import red.zyc.annotation.parser.type.ArrayTypeParser;
import red.zyc.annotation.parser.type.CascadeTypeParser;
import red.zyc.annotation.parser.type.CollectionTypeParser;
import red.zyc.annotation.parser.type.MapTypeParser;
import red.zyc.annotation.parser.type.ObjectTypeParser;
import red.zyc.annotation.parser.type.Sortable;
import red.zyc.annotation.parser.type.TypeParser;
import red.zyc.annotation.parser.type.TypeVariableParser;
import red.zyc.annotation.parser.type.WildcardTypeParser;

import java.lang.reflect.AnnotatedType;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 注解解析器入口，提供了很多有用的帮助方法
 * <ol>
 *     <li>{@link AnnotationParser#parse(Object, AnnotatedTypeToken) 根据AnnotatedTypeToken解析对象}</li>
 *     <li>{@link AnnotationParser#parse(Object, AnnotatedType) 根据AnnotatedType解析对象}</li>
 *     <li>{@link AnnotationParser#addTypeParser 添加类型解析器}</li>
 *     <li>{@link AnnotationParser#removeTypeParser 移除类型解析器}</li>
 *     <li>{@link AnnotationParser#randomOrder 生成一个不会与已注册的类型解析器解析顺序冲突的随机顺序值}</li>
 *     <li>{@link AnnotationParser#typeParsers 获取当前所有已注册的类型解析器}</li>
 * </ol>
 *
 * @author allurx
 */
public final class AnnotationParser {

    /**
     * 所有注册的{@link TypeParser}
     */
    private static final SortedSet<TypeParser<?, ? extends AnnotatedType>> TYPE_PARSERS = Collections.synchronizedSortedSet(new TreeSet<>());

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
     * {@link #parse(Object, AnnotatedType)}的包装方法
     *
     * @param value              待解析的对象
     * @param annotatedTypeToken 待解析对象的{@link AnnotatedTypeToken}
     * @param <T>                待解析对象的类型
     * @return 解析后的对象
     */
    public static <T> T parse(T value, AnnotatedTypeToken<T> annotatedTypeToken) {
        return parse(value, annotatedTypeToken.getAnnotatedType());
    }

    /**
     * 对于任何需要解析的Object，它可能继承了一些特殊的数据类型，例如{@link Collection}、{@link Map}等等，
     * 因此我们在解析对象时需要遍历所有已注册的解析器，只要解析器支持解析该Object，都应该使用该解析器解析对象。
     *
     * @param value         待解析的对象
     * @param annotatedType 待解析对象的{@link AnnotatedType}
     * @param <T>           待解析对象的类型
     * @param <AT>          待解析对象的{@link AnnotatedType}的类型
     * @return 解析后的对象
     */
    @SuppressWarnings("unchecked")
    public static <T, AT extends AnnotatedType> T parse(T value, AT annotatedType) {
        return TYPE_PARSERS.stream().filter(tp -> tp.support(value, annotatedType))
                .reduce(value, (v, tp) -> ((TypeParser<T, AT>) tp).parse(v, annotatedType), (v1, v2) -> null);
    }

    /**
     * 注册自己的类型解析器。<br>
     * 注意：如果类型解析器的{@link Sortable#order()}方法返回值已经被其它解析器占用了，
     * 那么该解析器将会被忽略。这是由{@link TreeSet}类的特性所导致的。
     *
     * @param typeParser 目标类型解析器
     * @see TreeSet
     */
    public static void addTypeParser(TypeParser<?, ? extends AnnotatedType> typeParser) {
        TYPE_PARSERS.add(typeParser);
    }

    /**
     * 从已注册的类型解析器中移除指定的解析器
     *
     * @param typeParser 需要移除的类型解析器
     */
    public static void removeTypeParser(TypeParser<?, ? extends AnnotatedType> typeParser) {
        TYPE_PARSERS.remove(typeParser);
    }

    /**
     * @return 一个不会与已注册的类型解析器顺序冲突的随机顺序值。
     * <p><strong>注意：不要在类型解析器的{@link Sortable#order()}方法中直接返回该方法。
     * 应该将该方法的返回值赋值给一个实例或静态变量，然后再返回这个变量。
     * 因为如果有解析器的顺序是直接返回该方法的话，由于在其它解析器调用这个方法的时候
     * 是通过遍历已注册解析器的{@link Sortable#order()}方法返回值来确定一个唯一顺序值的，
     * 此时就会产生无限递归。
     * </strong></p>
     */
    public static int randomOrder() {
        int order = ThreadLocalRandom.current().nextInt(Sortable.HIGHEST_PRIORITY, Sortable.LOWEST_PRIORITY);
        synchronized (TYPE_PARSERS) {
            return TYPE_PARSERS.stream().noneMatch(parser -> parser.order() == order) ? order : randomOrder();
        }
    }

    /**
     * @return 所有已注册的类型解析器，返回结果是一个通过{@link Collections#synchronizedSortedSet(SortedSet)}
     * 方法包装的{@link SortedSet}，有关线程安全需要注意的事项请自行参照该包装方法。
     */
    public static SortedSet<TypeParser<?, ? extends AnnotatedType>> typeParsers() {
        return TYPE_PARSERS;
    }

}
