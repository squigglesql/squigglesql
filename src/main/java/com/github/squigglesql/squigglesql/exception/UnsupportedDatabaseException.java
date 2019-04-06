package com.github.squigglesql.squigglesql.exception;

/**
 * Exception thrown when an unsupported database is being used. If you see this exception, then you should specify
 * an {@link com.github.squigglesql.squigglesql.syntax.AbstractSqlSyntax} explicitly to compile your query.
 */
public class UnsupportedDatabaseException extends RuntimeException {

    /**
     * Creates an exception.
     * @param protocol the unsupported database protocol.
     */
    public UnsupportedDatabaseException(String protocol) {
        super("Database protocol '" + protocol + "' is not supported by Squiggle SQL. Please specify SQL syntax"
                + " explicitly in your query.toStatement calls.");
    }
}
