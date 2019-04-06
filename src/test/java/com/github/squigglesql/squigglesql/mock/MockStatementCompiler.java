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
package com.github.squigglesql.squigglesql.mock;

import com.github.squigglesql.squigglesql.statement.StatementBuilder;
import com.github.squigglesql.squigglesql.statement.StatementCompiler;
import com.github.squigglesql.squigglesql.syntax.AbstractSqlSyntax;
import com.github.squigglesql.squigglesql.syntax.SqlSyntax;

public class MockStatementCompiler implements StatementCompiler<MockStatement> {

    @Override
    public AbstractSqlSyntax detectDefaultSyntax() {
        return SqlSyntax.DEFAULT_SQL_SYNTAX;
    }

    @Override
    public StatementBuilder<MockStatement> createStatementBuilder(String query) {
        return new MockStatement(query);
    }

    @Override
    public StatementBuilder<MockStatement> createInsertStatementBuilder(String query) {
        return new MockStatement(query);
    }
}
