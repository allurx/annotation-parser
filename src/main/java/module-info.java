/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */
/**
 * annotation-parser module
 *
 * @author allurx
 */
module io.allurx.annotation.parser {
    requires transitive io.allurx.kit.base;
    exports io.allurx.annotation.parser;
    exports io.allurx.annotation.parser.handler;
    exports io.allurx.annotation.parser.type;
    exports io.allurx.annotation.parser.util;
}