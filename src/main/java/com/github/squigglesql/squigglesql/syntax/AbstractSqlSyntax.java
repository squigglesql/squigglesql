package com.github.squigglesql.squigglesql.syntax;

public interface AbstractSqlSyntax {

    char getTableQuote();

    char getTableReferenceQuote();

    char getColumnQuote();

    char getResultColumnQuote();

    char getFunctionQuote();

    char getTextQuote();
}
