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
package com.github.squigglesql.squigglesql.query;

import com.github.squigglesql.squigglesql.Output;
import com.github.squigglesql.squigglesql.alias.Alphabet;
import com.github.squigglesql.squigglesql.statement.StatementCompiler;
import com.github.squigglesql.squigglesql.syntax.AbstractSqlSyntax;
import com.github.squigglesql.squigglesql.syntax.SqlSyntax;

import java.sql.SQLException;

public abstract class Query {

    static final Alphabet TABLE_REFERENCE_ALIAS_ALPHABET = new Alphabet('t', 7);

    protected abstract void compile(Output output);

    @Override
    public String toString() {
        return toString(SqlSyntax.DEFAULT_SQL_SYNTAX);
    }

    public String toString(AbstractSqlSyntax syntax) {
        return toString(syntax, Output.DEFAULT_INDENT);
    }

    public String toString(AbstractSqlSyntax syntax, String indent) {
        return compile(syntax, indent).toString();
    }

    public <S> S toStatement(StatementCompiler<S> compiler) throws SQLException {
        return toStatement(compiler.detectDefaultSyntax(), compiler);
    }

    public <S> S toStatement(AbstractSqlSyntax syntax, StatementCompiler<S> compiler) throws SQLException {
        return compile(syntax, Output.DEFAULT_INDENT).toStatement(compiler);
    }

    private Output compile(AbstractSqlSyntax syntax, String indent) {
        Output out = new Output(syntax, indent);
        compile(out);
        return out;
    }
}
