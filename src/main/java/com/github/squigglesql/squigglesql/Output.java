/*
 * Copyright 2004-2019 Joe Walnes, Guillaume Chauvet, Egor Nepomnyaschih.
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

import com.github.squigglesql.squigglesql.parameter.Parameter;
import com.github.squigglesql.squigglesql.statement.StatementBuilder;
import com.github.squigglesql.squigglesql.syntax.AbstractSqlSyntax;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Object that accumulates part of SQL query code and compiles them as a single string/statement.
 * The whole complex query is being built with a single output instance, while multiple {@link QueryCompiler}
 * instances can be used, one for each nested query.
 */
public class Output {

    /**
     * Default indentation (4 spaces).
     */
    public static final String DEFAULT_INDENT = "    ";

    private final AbstractSqlSyntax syntax;
    private final String indent;

    private final StringBuffer result = new StringBuffer();
    private final StringBuffer currentIndent = new StringBuffer();
    private boolean newLineComing;

    private final List<Parameter> parameters = new ArrayList<>();

    /**
     * Creates an output object for a specific SQL syntax with default indentation.
     *
     * @param syntax SQL syntax to use.
     */
    public Output(AbstractSqlSyntax syntax) {
        this(syntax, DEFAULT_INDENT);
    }

    /**
     * Creates an output object for a specific SQL syntax.
     *
     * @param syntax SQL syntax to use.
     * @param indent indentation string (e.g. tabulation or 4 spaces).
     */
    public Output(AbstractSqlSyntax syntax, String indent) {
        this.syntax = syntax;
        this.indent = indent;
    }

    /**
     * @return SQL syntax of the output object.
     */
    public AbstractSqlSyntax getSyntax() {
        return syntax;
    }

    /**
     * Writes a single ASCII character to the output.
     *
     * @param c character to write.
     */
    public void write(char c) {
        writeNewLineIfNeeded();
        result.append(c);
    }

    /**
     * Writes a string to the output.
     *
     * @param s string to write.
     */
    public void write(String s) {
        writeNewLineIfNeeded();
        result.append(s);
    }

    /**
     * Inserts a line break before the next statement.
     */
    public void writeln() {
        newLineComing = true;
    }

    /**
     * Writes a single ASCII character to the output and inserts a line break before the next statement.
     *
     * @param c character to write.
     */
    public void writeln(char c) {
        write(c);
        writeln();
    }

    /**
     * Writes a string to the output and inserts a line break before the next statement.
     *
     * @param s string to write.
     */
    public void writeln(String s) {
        write(s);
        writeln();
    }

    /**
     * Adds one more indentation level.
     */
    public void indent() {
        currentIndent.append(indent);
    }

    /**
     * Removes one indentation level.
     */
    public void unindent() {
        currentIndent.setLength(currentIndent.length() - indent.length());
    }

    /**
     * Pushes a new parameter value to the output statement.
     *
     * @param parameter parameter to push.
     */
    public void addParameter(Parameter parameter) {
        parameters.add(parameter);
    }

    /**
     * Returns the compiled SQL query as a string.
     *
     * @return SQL query.
     */
    @Override
    public String toString() {
        return result.toString();
    }

    /**
     * Dumps parameter values to a JDBC statement builder.
     *
     * @param builder statement builder to dump parameters to.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public void dumpParameters(StatementBuilder builder) throws SQLException {
        for (Parameter parameter : parameters) {
            parameter.addValue(builder);
        }
    }

    private void writeNewLineIfNeeded() {
        if (newLineComing) {
            result.append('\n').append(currentIndent.toString());
            newLineComing = false;
        }
    }
}
