package com.github.squigglesql.squigglesql.statement;

import com.github.squigglesql.squigglesql.syntax.AbstractSqlSyntax;

import java.sql.SQLException;

/**
 * Compiler allowing to compile a {@link com.github.squigglesql.squigglesql.query.Query} to a
 * {@link java.sql.PreparedStatement} or a similar object.
 *
 * @param <S> Statement class, e.g. {@link java.sql.PreparedStatement}.
 */
public interface StatementCompiler<S> {

    /**
     * Detects SQL syntax to use with this compiler by default. Throws
     * {@link com.github.squigglesql.squigglesql.exception.UnsupportedDatabaseException} if the database is not
     * supported. If that's the case, you might want to specify an SQL syntax explicitly.
     *
     * @return default SQL syntax.
     * @throws SQLException if the compiler failed to retrieve database metadata from JDBC driver.
     */
    AbstractSqlSyntax detectDefaultSyntax() throws SQLException;

    /**
     * Creates a new statement builder to compile a selection or update query.
     *
     * @param query SQL query to compile.
     * @return Statement builder.
     * @throws SQLException if JDBC driver throws the exception during statement preparation.
     */
    StatementBuilder<S> createStatementBuilder(String query) throws SQLException;

    /**
     * Creates a new statement builder to compile an insertion query.
     *
     * @param query SQL query to compile.
     * @return Statement builder.
     * @throws SQLException if JDBC driver throws the exception during statement preparation.
     */
    StatementBuilder<S> createInsertStatementBuilder(String query) throws SQLException;
}
