package com.github.squigglesql.squigglesql.statement;

/**
 * Statement builder for SQL query compilation to a {@link java.sql.PreparedStatement}. While {@link StatementCompiler}
 * is independent on a query, builder takes care of real compilation process. Formally, builder is an adapter over
 * {@link java.sql.PreparedStatement} allowing mock object implementation.
 *
 * @param <S> Statement class, e.g. {@link java.sql.PreparedStatement}.
 */
public interface StatementBuilder<S> extends Parametrized {

    /**
     * @return statement instance that you may finally commit to JDBC driver.
     */
    S buildStatement();
}
