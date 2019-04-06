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
package com.github.squigglesql.squigglesql.syntax;

/**
 * Abstract SQL syntax. Describes database-specific SQL features necessary to compile a query. You may use
 * {@link SqlSyntax} class to instantiate these objects.
 */
public interface AbstractSqlSyntax {

    /**
     * @return ASCII symbol use to quote a database table name. 0-char if quotation should be skipped.
     */
    char getTableQuote();

    /**
     * @return ASCII symbol use to quote a database table reference name. 0-char if quotation should be skipped.
     */
    char getTableReferenceQuote();

    /**
     * @return ASCII symbol use to quote a database table column name. 0-char if quotation should be skipped.
     */
    char getColumnQuote();

    /**
     * @return ASCII symbol use to quote a result column name. 0-char if quotation should be skipped.
     */
    char getResultColumnQuote();

    /**
     * @return ASCII symbol use to quote an SQL function name. 0-char if quotation should be skipped.
     */
    char getFunctionQuote();

    /**
     * @return ASCII symbol use to quote a string literal.
     */
    char getTextQuote();
}
