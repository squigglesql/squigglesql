package com.github.squigglesql.squigglesql.syntax;

/**
 * Abstract SQL syntax. Describes database-specific SQL features necessary to compile a query. You may use
 * {@link SqlSyntax} class to instantiate these objects.
 */
public interface AbstractSqlSyntax {

    /**
     * @return ASCII symbol use to quote a database table name. 0-char if quotation should be skipped.
     */
    char getTableQuote();

    /**
     * @return ASCII symbol use to quote a database table reference name. 0-char if quotation should be skipped.
     */
    char getTableReferenceQuote();

    /**
     * @return ASCII symbol use to quote a database table column name. 0-char if quotation should be skipped.
     */
    char getColumnQuote();

    /**
     * @return ASCII symbol use to quote a result column name. 0-char if quotation should be skipped.
     */
    char getResultColumnQuote();

    /**
     * @return ASCII symbol use to quote an SQL function name. 0-char if quotation should be skipped.
     */
    char getFunctionQuote();

    /**
     * @return ASCII symbol use to quote a string literal.
     */
    char getTextQuote();
}
