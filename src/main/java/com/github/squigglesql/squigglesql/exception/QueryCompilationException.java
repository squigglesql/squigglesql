package com.github.squigglesql.squigglesql.exception;

/**
 * Exception thrown when a query can not be compiled.
 */
public class QueryCompilationException extends RuntimeException {

    /**
     * Creates an exception.
     * @param message exception message.
     */
    public QueryCompilationException(String message) {
        super(message);
    }
}
