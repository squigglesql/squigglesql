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
package com.github.squigglesql.squigglesql;

import com.github.squigglesql.squigglesql.exception.QueryCompilationException;
import com.github.squigglesql.squigglesql.exception.UnsupportedDatabaseException;
import com.github.squigglesql.squigglesql.syntax.SqlSyntax;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SyntaxTest {

    @Test
    public void testSupportedProtocols() {
        assertEquals(SqlSyntax.DEFAULT_SQL_SYNTAX, SqlSyntax.from(""));
        assertEquals(SqlSyntax.MY_SQL_SYNTAX, SqlSyntax.from("mysql"));
        assertEquals(SqlSyntax.POSTGRE_SQL_SYNTAX, SqlSyntax.from("postgresql"));
        assertEquals(SqlSyntax.H2_SQL_SYNTAX, SqlSyntax.from("h2"));
    }

    @Test(expected = UnsupportedDatabaseException.class)
    public void testUnsupportedDatabase() {
        SqlSyntax.from("unsupported");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedUrl() {
        SqlSyntax.fromUrl("http://github.com");
    }

    @Test(expected = QueryCompilationException.class)
    public void testQuotesInLexem() {
        new QueryCompiler(new Output(SqlSyntax.DEFAULT_SQL_SYNTAX)).quote("who's bad", '\'');
    }
}
