package com.github.squigglesql.squigglesql.util;

import com.github.squigglesql.squigglesql.ResultMapper;
import com.github.squigglesql.squigglesql.query.Query;

import java.sql.Connection;

/**
 * Exception thrown by {@link JdbcUtils#selectOne(Query, Connection, ResultMapper)} on attempt to select two or more
 * rows.
 */
public class TooManyRecordsException extends RuntimeException {

    /**
     * Creates an exception.
     */
    public TooManyRecordsException() {
        super("Expected to retrieve 0 or 1 records from database, got more.");
    }
}
