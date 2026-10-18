/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */
/**
 * annotation-parser test module
 *
 * @author allurx
 */
module io.allurx.annotation.parser.test {
    requires org.junit.jupiter.api;
    requires io.allurx.annotation.parser;
    exports io.allurx.annotation.parser.test;
    exports io.allurx.annotation.parser.test.annotation;
    exports io.allurx.annotation.parser.test.handler;
    opens io.allurx.annotation.parser.test;
    opens io.allurx.annotation.parser.test.annotation;
    opens io.allurx.annotation.parser.test.handler;
}