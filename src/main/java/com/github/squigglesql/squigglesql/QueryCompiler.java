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
package com.github.squigglesql.squigglesql;

import com.github.squigglesql.squigglesql.exception.QueryCompilationException;
import com.github.squigglesql.squigglesql.parameter.Parameter;
import com.github.squigglesql.squigglesql.query.ResultColumn;
import com.github.squigglesql.squigglesql.syntax.AbstractSqlSyntax;

import java.util.Map;

/**
 * Object that writes parts of an SQL query to an output. Extra QueryCompiler gets created fot each nested query,
 * while a single {@link Output} object is being used for the whole complex query.
 */
public class QueryCompiler {

    private final Output output;
    private final Map<TableReference, String> tableReferenceAliases;
    private final Map<ResultColumn, String> resultColumnAliases;

    /**
     * Creates a query compiler. This constructor should be used
     * for {@link com.github.squigglesql.squigglesql.query.InsertQuery}.
     *
     * @param output output object to compile the query to.
     */
    public QueryCompiler(Output output) {
        this(output, null);
    }

    /**
     * Creates a query compiler. This constructor should be used
     * for {@link com.github.squigglesql.squigglesql.query.UpdateQuery}.
     *
     * @param output                output object to compile the query to.
     * @param tableReferenceAliases table reference aliases to use.
     */
    public QueryCompiler(Output output, Map<TableReference, String> tableReferenceAliases) {
        this(output, tableReferenceAliases, null);
    }

    /**
     * Creates a query compiler. This constructor should be used
     * for {@link com.github.squigglesql.squigglesql.query.SelectQuery}.
     *
     * @param output                output object to compile the query to.
     * @param tableReferenceAliases table reference aliases to use.
     * @param resultColumnAliases   result column aliases to use.
     */
    public QueryCompiler(Output output, Map<TableReference, String> tableReferenceAliases,
                         Map<ResultColumn, String> resultColumnAliases) {
        this.output = output;
        this.tableReferenceAliases = tableReferenceAliases;
        this.resultColumnAliases = resultColumnAliases;
    }

    /**
     * @return output object the compiler writes to.
     */
    public Output getOutput() {
        return output;
    }

    /**
     * @return SQL syntax of the output.
     */
    public AbstractSqlSyntax getSyntax() {
        return output.getSyntax();
    }

    /**
     * @param tableReference table reference.
     * @return alias of the table reference.
     */
    public String getAlias(TableReference tableReference) {
        return tableReferenceAliases.get(tableReference);
    }

    /**
     * @param resultColumn result column.
     * @return alias of the result column.
     */
    public String getAlias(ResultColumn resultColumn) {
        return resultColumnAliases.get(resultColumn);
    }

    /**
     * Writes a single ASCII character to the output.
     *
     * @param c character to write.
     * @return this compiler for chaining.
     */
    public QueryCompiler write(char c) {
        output.write(c);
        return this;
    }

    /**
     * Writes a string to the output.
     *
     * @param s string to write.
     * @return this compiler for chaining.
     */
    public QueryCompiler write(String s) {
        output.write(s);
        return this;
    }

    /**
     * Writes a compilable object to the output.
     *
     * @param c object to write.
     * @return this compiler for chaining.
     */
    public QueryCompiler write(Compilable c) {
        c.compile(this);
        return this;
    }

    /**
     * Inserts a line break before the next statement.
     *
     * @return this compiler for chaining.
     */
    public QueryCompiler writeln() {
        output.writeln();
        return this;
    }

    /**
     * Writes a single ASCII character to the output and inserts a line break before the next statement.
     *
     * @param c character to write.
     * @return this compiler for chaining.
     */
    public QueryCompiler writeln(char c) {
        output.writeln(c);
        return this;
    }

    /**
     * Writes a string to the output and inserts a line break before the next statement.
     *
     * @param s string to write.
     * @return this compiler for chaining.
     */
    public QueryCompiler writeln(String s) {
        output.writeln(s);
        return this;
    }

    /**
     * Writes a string to the output, surrounding it with quotes. The method doesn't escape the output, so it assumes
     * that the string doesn't contain such quote symbols. If it does, it throws {@link QueryCompilationException}.
     *
     * @param s  string to write.
     * @param ch quote character to use. Pass 0 character to just write the string without quotes.
     * @return this compiler for chaining.
     */
    public QueryCompiler quote(String s, char ch) {
        if (ch == 0) {
            return write(s);
        }
        if (s.indexOf(ch) != -1) {
            throw new QueryCompilationException("Can't quote a lexem with " + ch + " characters, as it contains these"
                    + " characters. Lexem: " + s);
        }
        return write(ch).write(s).write(ch);
    }

    /**
     * Adds one more indentation level.
     *
     * @return this compiler for chaining.
     */
    public QueryCompiler indent() {
        output.indent();
        return this;
    }

    /**
     * Removes one indentation level.
     *
     * @return this compiler for chaining.
     */
    public QueryCompiler unindent() {
        output.unindent();
        return this;
    }

    /**
     * Pushes a new parameter value to the output statement.
     *
     * @param parameter parameter to push.
     * @return this compiler for chaining.
     */
    public QueryCompiler addParameter(Parameter parameter) {
        output.addParameter(parameter);
        return this;
    }
}
