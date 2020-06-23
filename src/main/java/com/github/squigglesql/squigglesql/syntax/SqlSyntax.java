/*
 * Copyright 2019-2020 Egor Nepomnyaschih and contributors.
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
 * Library of predefined {@link AbstractSqlSyntax} implementations.
 */
public abstract class SqlSyntax {

    /**
     * Default SQL syntax. Doesn't quote any identifiers. Uses single quote symbol to quote string literals. Not to be
     * used in production, as it may fail to work if some table/column/function identifier is a reserved word.
     */
    public static final AbstractSqlSyntax DEFAULT_SQL_SYNTAX = new DefaultSqlSyntax();

    /**
     * MySQL syntax. Uses backtick to quote identifiers and single quote to quote string literals.
     */
    public static final AbstractSqlSyntax MY_SQL_SYNTAX = new MySqlSyntax();

    /**
     * PostgreSQL syntax. Uses double quote to quote identifiers and single quote to quote string literals.
     */
    public static final AbstractSqlSyntax POSTGRE_SQL_SYNTAX = new PostgreSqlSyntax();

    /**
     * H2 syntax. Uses double quote to quote identifiers and single quote to quote string literals.
     * @since 4.1.0
     */
    public static final AbstractSqlSyntax H2_SQL_SYNTAX = new H2Syntax();

    /**
     * Detects an SQL syntax by JDBC protocol name.
     *
     * <ul>
     * <li>Returns {@link SqlSyntax#DEFAULT_SQL_SYNTAX} for "" protocol.</li>
     * <li>Returns {@link SqlSyntax#MY_SQL_SYNTAX} for "mysql" protocol.</li>
     * <li>Returns {@link SqlSyntax#POSTGRE_SQL_SYNTAX} for "postgresql" protocol.</li>
     * <li>Returns {@link SqlSyntax#H2_SQL_SYNTAX} for "h2" protocol.</li>
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
            case "h2":
                return H2_SQL_SYNTAX;
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
     * @throws SQLException if JDBC throws the exception.
     */
    public static AbstractSqlSyntax from(Connection connection) throws SQLException {
        return fromUrl(connection.getMetaData().getURL());
    }
}
