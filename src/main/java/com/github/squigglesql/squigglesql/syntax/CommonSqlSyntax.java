/*
 * Copyright 2019-2020 Egor Nepomnyaschih.
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
import com.github.squigglesql.squigglesql.TableReference;

abstract class CommonSqlSyntax implements AbstractSqlSyntax {

    abstract char getIdentifierQuote();

    @Override
    public char getTableQuote() {
        return getIdentifierQuote();
    }

    @Override
    public char getTableReferenceQuote() {
        return getIdentifierQuote();
    }

    @Override
    public char getColumnQuote() {
        return getIdentifierQuote();
    }

    @Override
    public char getResultColumnQuote() {
        return getIdentifierQuote();
    }

    @Override
    public char getFunctionQuote() {
        return getIdentifierQuote();
    }

    @Override
    public char getTextQuote() {
        return '\'';
    }

    @Override
    public void compileIsDistinctFrom(QueryCompiler compiler, Matchable left, Matchable right) {
        compiler.write(left).write(" IS DISTINCT FROM ").write(right);
    }

    @Override
    public void compileIsNotDistinctFrom(QueryCompiler compiler, Matchable left, Matchable right) {
        compiler.write(left).write(" IS NOT DISTINCT FROM ").write(right);
    }

    @Override
    public void compileDeleteFrom(QueryCompiler compiler, TableReference tableReference) {
        compiler.writeln("DELETE FROM").indent().writeln(tableReference).unindent();
    }
}
