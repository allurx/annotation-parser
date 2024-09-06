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

import java.lang.reflect.AnnotatedElement;

/**
 * 注解出现在目标对象上的位置
 *
 * @author allurx
 * @see AnnotatedElement
 */
public enum Location {

    /**
     * 直接存在
     */
    DIRECTLY_PRESENT,

    /**
     * 间接存在
     */
    INDIRECTLY_PRESENT,

    /**
     * 存在
     */
    PRESENT,

    /**
     * 关联的
     */
    ASSOCIATED

}
