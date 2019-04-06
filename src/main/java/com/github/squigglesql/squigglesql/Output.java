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
 * The whole complex query is being built with a single output instance, while multiple QueryCompilers can be used.
 */
public class Output {

    public static final String DEFAULT_INDENT = "    ";

    private final AbstractSqlSyntax syntax;
    private final String indent;

    private final StringBuffer result = new StringBuffer();
    private final StringBuffer currentIndent = new StringBuffer();
    private boolean newLineComing;

    private final List<Parameter> parameters = new ArrayList<>();

    public Output(AbstractSqlSyntax syntax) {
        this(syntax, DEFAULT_INDENT);
    }

    public Output(AbstractSqlSyntax syntax, String indent) {
        this.syntax = syntax;
        this.indent = indent;
    }

    public AbstractSqlSyntax getSyntax() {
        return syntax;
    }

    public void write(char c) {
        writeNewLineIfNeeded();
        result.append(c);
    }

    public void write(String s) {
        writeNewLineIfNeeded();
        result.append(s);
    }

    public void writeln() {
        newLineComing = true;
    }

    public void writeln(char c) {
        write(c);
        writeln();
    }

    public void writeln(String s) {
        write(s);
        writeln();
    }

    public void indent() {
        currentIndent.append(indent);
    }

    public void unindent() {
        currentIndent.setLength(currentIndent.length() - indent.length());
    }

    public void addParameter(Parameter parameter) {
        parameters.add(parameter);
    }

    @Override
    public String toString() {
        return result.toString();
    }

    public void addParameters(StatementBuilder builder) throws SQLException {
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
