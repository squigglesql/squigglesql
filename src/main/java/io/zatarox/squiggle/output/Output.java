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
package io.zatarox.squiggle.output;

import io.zatarox.squiggle.Parameter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Output is where the elements of the query output their bits of SQL to.
 */
public class Output {

    /**
     * @param indent String to be used for indenting (e.g. "", " ", " ", "\t")
     */
    public Output(String indent) {
        this.indent = indent;
    }

    private final StringBuffer result = new StringBuffer();
    private final StringBuffer currentIndent = new StringBuffer();
    private boolean newLineComing;

    private final String indent;

    private final List<Parameter> parameters = new ArrayList<Parameter>();

    @Override
    public String toString() {
        return result.toString();
    }

    public PreparedStatement toStatement(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(toString());
        for (int i = 0; i < parameters.size(); ++i) {
            parameters.get(i).setValue(statement, i + 1);
        }
        return statement;
    }

    public Output print(Object o) {
        writeNewLineIfNeeded();
        result.append(o);
        return this;
    }

    public Output print(char c) {
        writeNewLineIfNeeded();
        result.append(c);
        return this;
    }

    public Output println(Object o) {
        writeNewLineIfNeeded();
        result.append(o);
        newLineComing = true;
        return this;
    }

    public Output println() {
        newLineComing = true;
        return this;
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

    private void writeNewLineIfNeeded() {
        if (newLineComing) {
            result.append('\n').append(currentIndent);
            newLineComing = false;
        }
    }
}
