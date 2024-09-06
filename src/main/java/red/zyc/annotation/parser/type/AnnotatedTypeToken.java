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

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 用来获取{@link ParameterizedType}类型对象运行时泛型参数上的注解。<br>
 * 由于java泛型擦除机制，如果我们想获取{@code new ArrayList<@MyAnnotation String>}这个对象运行时的泛型参数
 * 上的@MyAnnotation注解，这几乎是很难做到的。而通过{@link AnnotatedTypeToken}我们只需要构造一个它的匿名子类就能很容易获取：
 * <pre>
 *    {@code var list = new AnnotatedTypeToken<List<@MyAnnotation String>>(){}}
 *    {@code list.getType() = java.util.List<java.lang.String>}
 *    {@code list.getAnnotatedType() = java.util.List<@MyAnnotation java.lang.String>}
 * </pre>
 *
 * @param <T> 需要捕获的明确类型
 * @author allurx
 */
public abstract class AnnotatedTypeToken<T> {

    /**
     * {@link T}运行时的类型
     */
    private final Type type;

    /**
     * {@link T}运行时被注解的类型
     */
    private final AnnotatedType annotatedType;

    protected AnnotatedTypeToken() {
        annotatedType = capture();
        type = annotatedType.getType();
    }

    private AnnotatedTypeToken(AnnotatedType annotatedType) {
        this.annotatedType = annotatedType;
        this.type = annotatedType.getType();
    }

    /**
     * 通过已知对象的{@link AnnotatedType}实例化{@link AnnotatedTypeToken}
     *
     * @param annotatedType 对象的{@link AnnotatedType}
     * @param <T>           对象的运行时类型
     * @return 该对象的 {@link AnnotatedTypeToken}
     */
    public static <T> AnnotatedTypeToken<T> of(AnnotatedType annotatedType) {
        return new AnnotatedTypeToken<>(annotatedType) {
        };
    }

    /**
     * @return 对象运行时的类型
     */
    public final Type getType() {
        return type;
    }

    /**
     * @return 对象运行时被注解的类型
     */
    public final AnnotatedType getAnnotatedType() {
        return annotatedType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnnotatedTypeToken<?> annotatedTypeToken = (AnnotatedTypeToken<?>) o;
        return type.equals(annotatedTypeToken.type) &&
                annotatedType.equals(annotatedTypeToken.annotatedType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, annotatedType);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AnnotatedTypeToken.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("annotatedType=" + annotatedType)
                .toString();
    }

    /**
     * @return {@link T}运行时被注解的类型
     */
    private AnnotatedType capture() {
        Class<?> clazz = getClass();
        Type superclass = clazz.getGenericSuperclass();
        if (!(superclass instanceof ParameterizedType)) {
            throw new IllegalArgumentException(String.format("%s必须是参数化类型", superclass));
        }
        return ((AnnotatedParameterizedType) clazz.getAnnotatedSuperclass()).getAnnotatedActualTypeArguments()[0];
    }

}
