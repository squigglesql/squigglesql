package network.tide.squiggle.statement;

import java.sql.SQLException;

public interface StatementCompiler<S> {

    StatementBuilder<S> createStatementBuilder(String query) throws SQLException;
}
