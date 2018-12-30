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
package io.zatarox.squiggle;

import io.zatarox.squiggle.parameter.Parameter;
import io.zatarox.squiggle.statement.StatementBuilder;
import io.zatarox.squiggle.statement.StatementCompiler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Output {

    private final StringBuffer result = new StringBuffer();
    private final StringBuffer currentIndent = new StringBuffer();
    private boolean newLineComing;

    private final String indent;

    private final List<Parameter> parameters = new ArrayList<Parameter>();

    public Output(String indent) {
        this.indent = indent;
    }

    public boolean isEmpty() {
        return result.length() == 0;
    }

    public Output write(char c) {
        writeNewLineIfNeeded();
        result.append(c);
        return this;
    }

    public Output write(String s) {
        writeNewLineIfNeeded();
        result.append(s);
        return this;
    }

    public Output write(Outputable outputable) {
        writeNewLineIfNeeded();
        outputable.write(this);
        return this;
    }

    public Output writeln() {
        newLineComing = true;
        return this;
    }

    public Output writeln(char c) {
        write(c);
        return writeln();
    }

    public Output writeln(String s) {
        write(s);
        return writeln();
    }

    public Output writeln(Outputable outputable) {
        write(outputable);
        return writeln();
    }

    public Output indent() {
        currentIndent.append(indent);
        return this;
    }

    public Output unindent() {
        currentIndent.setLength(currentIndent.length() - indent.length());
        return this;
    }

    public void addParameter(Parameter parameter) {
        parameters.add(parameter);
    }

    @Override
    public String toString() {
        return result.toString();
    }

    public <S> S toStatement(StatementCompiler<S> compiler) throws SQLException {
        StatementBuilder<S> builder = compiler.createStatementBuilder(toString());
        for (Parameter parameter : parameters) {
            parameter.addValue(builder);
        }
        return builder.buildStatement();
    }

    private void writeNewLineIfNeeded() {
        if (newLineComing) {
            result.append('\n').append(currentIndent.toString());
            newLineComing = false;
        }
    }
}
