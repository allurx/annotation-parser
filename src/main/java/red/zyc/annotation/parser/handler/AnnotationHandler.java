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
package red.zyc.annotation.parser.handler;

import red.zyc.annotation.parser.util.Singleton;

import java.lang.annotation.Annotation;

/**
 * 注解处理程序
 *
 * @param <T> 待处理的对象类型
 * @param <A> 标注在对象上的注解类型
 * @param <R> 处理的结果类型
 * @author allurx
 * @see Parse
 */
@Singleton
public interface AnnotationHandler<T, A extends Annotation, R> {

    /**
     * 处理目标对象与该对象上的注解
     *
     * @param target     目标对象
     * @param annotation 对象上的注解
     * @return 处理后的结果
     */
    R handle(T target, A annotation);
}
