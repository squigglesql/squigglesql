package com.github.squigglesql.squigglesql.statement;

import com.github.squigglesql.squigglesql.syntax.AbstractSqlSyntax;

import java.sql.SQLException;

public interface StatementCompiler<S> {

    AbstractSqlSyntax detectDefaultSyntax() throws SQLException;

    StatementBuilder<S> createStatementBuilder(String query) throws SQLException;
}
