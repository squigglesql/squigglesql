package com.github.squigglesql.squigglesql.syntax;

import com.github.squigglesql.squigglesql.exception.UnsupportedDatabaseException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlSyntax implements AbstractSqlSyntax {

    public static final AbstractSqlSyntax DEFAULT_SQL_SYNTAX = new SqlSyntax((char) 0, '\'');
    public static final AbstractSqlSyntax MY_SQL_SYNTAX = new SqlSyntax('`', '\'');
    public static final AbstractSqlSyntax POSTGRE_SQL_SYNTAX = new SqlSyntax('"', '\'');

    private final char tableQuote;
    private final char tableReferenceQuote;
    private final char columnQuote;
    private final char resultColumnQuote;
    private final char functionQuote;
    private final char textQuote;

    public SqlSyntax(char identifierQuote, char textQuote) {
        this(identifierQuote, identifierQuote, identifierQuote, identifierQuote, identifierQuote, textQuote);
    }

    public SqlSyntax(char tableQuote, char tableReferenceQuote, char columnQuote, char resultColumnQuote,
                     char functionQuote, char textQuote) {
        this.tableQuote = tableQuote;
        this.tableReferenceQuote = tableReferenceQuote;
        this.columnQuote = columnQuote;
        this.resultColumnQuote = resultColumnQuote;
        this.functionQuote = functionQuote;
        this.textQuote = textQuote;
    }

    @Override
    public char getTableQuote() {
        return tableQuote;
    }

    @Override
    public char getTableReferenceQuote() {
        return tableReferenceQuote;
    }

    @Override
    public char getColumnQuote() {
        return columnQuote;
    }

    @Override
    public char getResultColumnQuote() {
        return resultColumnQuote;
    }

    @Override
    public char getFunctionQuote() {
        return functionQuote;
    }

    @Override
    public char getTextQuote() {
        return textQuote;
    }

    public static AbstractSqlSyntax from(String protocol) {
        switch (protocol) {
            case "":
                return DEFAULT_SQL_SYNTAX;
            case "mysql":
                return MY_SQL_SYNTAX;
            case "postgresql":
                return POSTGRE_SQL_SYNTAX;
            default:
                throw new UnsupportedDatabaseException(protocol);
        }
    }

    public static AbstractSqlSyntax fromUrl(String url) {
        Pattern pattern = Pattern.compile("^jdbc:([^:]+):");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            throw new IllegalArgumentException("JDBC connection URL has unsupported format.");
        }
        return from(matcher.group(1));
    }

    public static AbstractSqlSyntax from(Connection connection) throws SQLException {
        return fromUrl(connection.getMetaData().getURL());
    }
}
