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

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Array;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.Map;

/**
 * 类型解析器，用来解析不同的类型。例如{@link Collection}，{@link Map}，{@link Array}。
 * 用户可以实现该接口定义特定类型的解析器，然后调用{@link AnnotationParser#addTypeParser}方法注册自己的类型解析器。
 * 对于任何需要解析的Object来说，本质上都可以通过{@link TypeVariable 类型变量}或者{@link WildcardType 通配符}来代表它，
 * 同时object本身也可能需要被解析或者需要解析Object内部的域，因此在注册类型解析器时，需要遵守以下两个约定：
 *
 * <ol>
 *     <li>
 *         类型解析器的执行顺序应该晚于{@link TypeVariableParser}和{@link WildcardTypeParser}这两个解析器。
 *     </li>
 *     <li>
 *         类型解析器的执行顺序应该早于{@link ObjectTypeParser}和{@link CascadeTypeParser}这两个解析器。
 *     </li>
 * </ol>
 * 否则解析的结果可能会和预期不一致。
 *
 * @param <T>  被解析对象的类型
 * @param <AT> {@link AnnotatedType}
 * @author zyc
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
     * 解析对象，实现该方法的子类如果能够解析目标对象，应当<b>尽可能</b>返回一个新的{@link T}实例
     *
     * @param value         被解析的对象
     * @param annotatedType 被解析对象的{@link AnnotatedType}
     * @return 解析后的新对象
     */
    T parse(T value, AT annotatedType);

    /**
     * 是否支持解析目标对象
     *
     * @param value         被解析的对象
     * @param annotatedType 被解析对象的{@link AnnotatedType}
     * @return 是否支持解析目标对象
     */
    boolean support(Object value, AnnotatedType annotatedType);

    /**
     * 解析器执行顺序
     *
     * @param typeParser 解析器
     * @return 解析器执行顺序
     */
    @Override
    default int compareTo(TypeParser<?, ? extends AnnotatedType> typeParser) {
        return Integer.compare(order(), typeParser.order());
    }

}
