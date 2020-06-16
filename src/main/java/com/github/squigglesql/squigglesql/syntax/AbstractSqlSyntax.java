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

import com.github.squigglesql.squigglesql.Matchable;
import com.github.squigglesql.squigglesql.QueryCompiler;

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

    /**
     * Compiles a part of empty row insertion query after "INSERT INTO `table_name` ".
     * @param compiler compiler to compile the query with.
     */
    void compileEmptyInsert(QueryCompiler compiler);

    /**
     * Compiles "left IS DISTINCT FROM right" criteria. Some databases don't support this operator directly, so
     * their implementations of this method should provide a logically equivalent alternative.
     *
     * @param compiler compiler to compile the query with.
     * @param left     left operand.
     * @param right    right operand.
     * @since 4.1.0
     */
    void compileIsDistinctFrom(QueryCompiler compiler, Matchable left, Matchable right);

    /**
     * Compiles "left IS NOT DISTINCT FROM right" criteria. Some databases don't support this operator directly, so
     * their implementations of this method should provide a logically equivalent alternative.
     *
     * @param compiler compiler to compile the query with.
     * @param left     left operand.
     * @param right    right operand.
     * @since 4.1.0
     */
    void compileIsNotDistinctFrom(QueryCompiler compiler, Matchable left, Matchable right);
}
