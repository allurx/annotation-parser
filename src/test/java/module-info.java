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
/**
 * @author allurx
 */
module red.zyc.annotation.parser.test {
    requires red.zyc.annotation.parser;
    requires org.junit.jupiter;
    requires org.junit.jupiter.api;
    exports red.zyc.annotation.parser.test;
    exports red.zyc.annotation.parser.test.annotation;
    exports red.zyc.annotation.parser.test.handler;
    opens red.zyc.annotation.parser.test;
    opens red.zyc.annotation.parser.test.annotation;
    opens red.zyc.annotation.parser.test.handler;
}