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

import java.sql.SQLException;

public abstract class Query {

    static final Alphabet TABLE_REFERENCE_ALIAS_ALPHABET = new Alphabet('t', 7);

    protected abstract void compile(Output output);

    @Override
    public String toString() {
        return toString(Output.DEFAULT_INDENT);
    }

    public String toString(String indent) {
        return compile(indent).toString();
    }

    public <S> S toStatement(StatementCompiler<S> compiler) throws SQLException {
        return compile(Output.DEFAULT_INDENT).toStatement(compiler);
    }

    private Output compile(String indent) {
        Output out = new Output(indent);
        compile(out);
        return out;
    }
}
