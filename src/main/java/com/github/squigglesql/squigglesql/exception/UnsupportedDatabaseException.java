/*
 * Copyright 2019 Egor Nepomnyaschih.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.squigglesql.squigglesql.exception;

/**
 * Exception thrown when an unsupported database is being used. If you see this exception, then you should specify
 * an {@link com.github.squigglesql.squigglesql.syntax.AbstractSqlSyntax} explicitly to compile your query.
 */
public class UnsupportedDatabaseException extends RuntimeException {

    /**
     * Creates an exception.
     *
     * @param protocol the unsupported database protocol.
     */
    public UnsupportedDatabaseException(String protocol) {
        super("Database protocol '" + protocol + "' is not supported by Squiggle SQL. Please specify SQL syntax"
                + " explicitly in your query.toStatement calls.");
    }
}
