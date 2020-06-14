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
import com.github.squigglesql.squigglesql.statement.StatementBuilder;
import com.github.squigglesql.squigglesql.statement.StatementCompiler;
import com.github.squigglesql.squigglesql.syntax.AbstractSqlSyntax;
import com.github.squigglesql.squigglesql.syntax.SqlSyntax;

import java.sql.SQLException;

/**
 * Base SQL query.
 */
public abstract class Query {

    static final Alphabet TABLE_REFERENCE_ALIAS_ALPHABET = new Alphabet('t', 7);

    /**
     * Compiles the query by writing SQL code to the output.
     *
     * @param output output to compile the query to.
     */
    protected abstract void compile(Output output);

    /**
     * Creates a statement builder for this query.
     *
     * @param compiler compiler to create a statement builder with.
     * @param query    pre-computed SQL query code.
     * @param <S>      statement class of the compiler.
     * @return statement builder.
     * @throws SQLException if JDBC throws the exception.
     */
    protected abstract <S> StatementBuilder<S> createStatementBuilder(StatementCompiler<S> compiler, String query)
            throws SQLException;

    /**
     * Compiles the query to a string using default SQL syntax. The syntax may not be compatible with your particular
     * database, so it is recommended to use this method only for logging, testing, debugging and demonstration
     * purposes. Use {@link Query#toString(AbstractSqlSyntax)} in production.
     * If you use a parametrized query, consider using {@link Query#toStatement(StatementCompiler)}
     * method, or parameter values won't be used.
     *
     * @return SQL code representing the query.
     */
    @Override
    public String toString() {
        return toString(SqlSyntax.DEFAULT_SQL_SYNTAX);
    }

    /**
     * Compiles the query to a string using the specified SQL syntax.
     * If you use a parametrized query, consider using {@link Query#toStatement(AbstractSqlSyntax, StatementCompiler)}
     * method, or parameter values won't be used.
     *
     * @param syntax syntax to compile the query with.
     * @return SQL code representing the query.
     */
    public String toString(AbstractSqlSyntax syntax) {
        return toString(syntax, Output.DEFAULT_INDENT);
    }

    /**
     * Compiles the query to a string using the specified SQL syntax and indentation.
     * If you use a parametrized query, consider using {@link Query#toStatement(AbstractSqlSyntax, StatementCompiler)}
     * method, or parameter values won't be used.
     *
     * @param syntax syntax to compile the query with.
     * @param indent indentation to use in the query.
     * @return SQL code representing the query.
     */
    public String toString(AbstractSqlSyntax syntax, String indent) {
        Output out = new Output(syntax, indent);
        compile(out);
        return out.toString();
    }

    /**
     * Compiles the query to a statement (e.g. {@link java.sql.PreparedStatement}) using the specified compiler.
     * The SQL syntax should be detected by the compiler automatically. If the detection fails, you should use
     * {@link Query#toStatement(AbstractSqlSyntax, StatementCompiler)} and specify the syntax explicitly.
     *
     * @param compiler compiler to use to compile the query.
     * @param <S>      statement class.
     * @return statement representing the query.
     * @throws SQLException if JDBC throws the exception.
     */
    public <S> S toStatement(StatementCompiler<S> compiler) throws SQLException {
        return toStatement(compiler.detectDefaultSyntax(), compiler);
    }

    /**
     * Compiles the query to a statement (e.g. {@link java.sql.PreparedStatement}) using the specified compiler and
     * SQL syntax.
     *
     * @param compiler compiler to use to compile the query.
     * @param syntax   syntax to compile the query with.
     * @param <S>      statement class.
     * @return statement representing the query.
     * @throws SQLException if JDBC throws the exception.
     */
    public <S> S toStatement(AbstractSqlSyntax syntax, StatementCompiler<S> compiler) throws SQLException {
        Output out = new Output(syntax);
        compile(out);
        StatementBuilder<S> builder = createStatementBuilder(compiler, out.toString());
        out.dumpParameters(builder);
        return builder.buildStatement();
    }
}
