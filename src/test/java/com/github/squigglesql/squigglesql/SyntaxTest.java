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
