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
package network.tide.squiggle;

import network.tide.squiggle.parameter.Parameter;
import network.tide.squiggle.query.ResultColumn;

import java.util.Collections;
import java.util.Map;

/**
 * Extra QueryCompiler gets created fot each nested query.
 */
public class QueryCompiler {

    private final Output output;
    private final Map<TableReference, String> tableReferenceAliases;
    private final Map<ResultColumn, String> resultColumnAliases;

    public QueryCompiler(Output output) {
        this(output, Collections.emptyMap());
    }

    public QueryCompiler(Output output, Map<TableReference, String> tableReferenceAliases) {
        this(output, tableReferenceAliases, null);
    }

    public QueryCompiler(Output output, Map<TableReference, String> tableReferenceAliases,
                         Map<ResultColumn, String> resultColumnAliases) {
        this.output = output;
        this.tableReferenceAliases = tableReferenceAliases;
        this.resultColumnAliases = resultColumnAliases;
    }

    public Output getOutput() {
        return output;
    }

    public String getAlias(TableReference tableReference) {
        return tableReferenceAliases.get(tableReference);
    }

    public String getAlias(ResultColumn resultColumn) {
        return resultColumnAliases.get(resultColumn);
    }

    public QueryCompiler write(char c) {
        output.write(c);
        return this;
    }

    public QueryCompiler write(String s) {
        output.write(s);
        return this;
    }

    public QueryCompiler write(Compilable c) {
        c.compile(this);
        return this;
    }

    public QueryCompiler writeln() {
        output.writeln();
        return this;
    }

    public QueryCompiler writeln(char c) {
        output.writeln(c);
        return this;
    }

    public QueryCompiler writeln(String s) {
        output.writeln(s);
        return this;
    }

    public QueryCompiler indent() {
        output.indent();
        return this;
    }

    public QueryCompiler unindent() {
        output.unindent();
        return this;
    }

    public QueryCompiler addParameter(Parameter parameter) {
        output.addParameter(parameter);
        return this;
    }
}
