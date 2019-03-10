package com.github.squigglesql.squigglesql.databases;

import java.sql.Connection;
import java.sql.SQLException;

public interface TestDatabase {

    String getUrl();

    void createTable(Connection connection, String name, TestDatabaseColumn[] columns) throws SQLException;

    void dropTable(Connection connection, String name) throws SQLException;
}
