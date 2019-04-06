package com.github.squigglesql.squigglesql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Callback for {@link ResultSet} mapping to a Java model instance.
 *
 * @param <T> Java model class.
 */
public interface ResultMapper<T> {

    /**
     * Maps a single {@link ResultSet} row to a Java model instance.
     *
     * @param rs JDBC result set.
     * @return Java model instance.
     * @throws SQLException if JDBC driver throws the exception.
     */
    T apply(ResultSet rs) throws SQLException;
}
