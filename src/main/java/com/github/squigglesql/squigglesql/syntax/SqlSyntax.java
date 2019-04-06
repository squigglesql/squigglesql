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
package com.github.squigglesql.squigglesql.syntax;

import com.github.squigglesql.squigglesql.exception.UnsupportedDatabaseException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of {@link AbstractSqlSyntax}.
 */
public class SqlSyntax implements AbstractSqlSyntax {

    /**
     * Default SQL syntax. Doesn't quote any identifiers. Uses single quote symbol to quote string literals. Not to be
     * used in production, as it may fail to work if some table/column/function identifier is a reserved word.
     */
    public static final AbstractSqlSyntax DEFAULT_SQL_SYNTAX = new SqlSyntax((char) 0, '\'');

    /**
     * MySQL syntax. Uses backtick to quote identifiers and single quote to quote string literals.
     */
    public static final AbstractSqlSyntax MY_SQL_SYNTAX = new SqlSyntax('`', '\'');

    /**
     * PostgreSQL syntax. Uses double quote to quote identifiers and single quote to quote string literals.
     */
    public static final AbstractSqlSyntax POSTGRE_SQL_SYNTAX = new SqlSyntax('"', '\'');

    private final char tableQuote;
    private final char tableReferenceQuote;
    private final char columnQuote;
    private final char resultColumnQuote;
    private final char functionQuote;
    private final char textQuote;

    /**
     * Creates an SQL syntax.
     *
     * @param identifierQuote ASCII character to use to quote identifiers. 0-char if quotation should be skipped.
     * @param textQuote       ASCII character to use to quote string literals.
     */
    public SqlSyntax(char identifierQuote, char textQuote) {
        this(identifierQuote, identifierQuote, identifierQuote, identifierQuote, identifierQuote, textQuote);
    }

    /**
     * Creates an SQL syntax.
     *
     * @param tableQuote          ASCII character to use to quote a table name. 0-char if quotation should be skipped.
     * @param tableReferenceQuote ASCII character to use to quote a table reference name. 0-char if quotation should be skipped.
     * @param columnQuote         ASCII character to use to quote a table column name. 0-char if quotation should be skipped.
     * @param resultColumnQuote   ASCII character to use to quote a result column name. 0-char if quotation should be skipped.
     * @param functionQuote       ASCII character to use to quote an SQL function name. 0-char if quotation should be skipped.
     * @param textQuote           ASCII symbol use to quote a string literal.
     */
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

    /**
     * Detects an SQL syntax by JDBC protocol name.
     *
     * <ul>
     * <li>Returns {@link SqlSyntax#DEFAULT_SQL_SYNTAX} for "" protocol.</li>
     * <li>Returns {@link SqlSyntax#MY_SQL_SYNTAX} for "mysql" protocol.</li>
     * <li>Returns {@link SqlSyntax#POSTGRE_SQL_SYNTAX} for "postgresql" protocol.</li>
     * <li>Else throws {@link UnsupportedDatabaseException}.</li>
     * </ul>
     *
     * @param protocol JDBC protocol name.
     * @return SQL syntax instance.
     */
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

    /**
     * Detects an SQL syntax by JDBC protocol name written in the URL. See {@link SqlSyntax#from(String)} for details.
     *
     * @param url JDBC URL.
     * @return SQL syntax instance.
     */
    public static AbstractSqlSyntax fromUrl(String url) {
        Pattern pattern = Pattern.compile("^jdbc:([^:]+):");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            throw new IllegalArgumentException("JDBC connection URL has unsupported format.");
        }
        return from(matcher.group(1));
    }

    /**
     * Detects an SQL syntax by JDBC protocol name written in the URL obtained from JDBC connection metadata.
     * See {@link SqlSyntax#fromUrl(String)} for details.
     *
     * @param connection JDBC connection.
     * @return SQL syntax instance.
     */
    public static AbstractSqlSyntax from(Connection connection) throws SQLException {
        return fromUrl(connection.getMetaData().getURL());
    }
}
