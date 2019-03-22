package com.github.squigglesql.squigglesql.exception;

public class UnsupportedDatabaseException extends RuntimeException {

    public UnsupportedDatabaseException(String protocol) {
        super("Database protocol '" + protocol + "' is not supported by Squiggle SQL. Please specify SQL syntax"
                + " explicitly in your query.toStatement calls.");
    }
}
