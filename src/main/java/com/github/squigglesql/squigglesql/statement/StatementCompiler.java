package com.github.squigglesql.squigglesql.statement;

import java.sql.SQLException;

public interface StatementCompiler<S> {

    StatementBuilder<S> createStatementBuilder(String query) throws SQLException;
}
