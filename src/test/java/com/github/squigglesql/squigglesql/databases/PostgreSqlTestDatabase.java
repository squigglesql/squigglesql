package com.github.squigglesql.squigglesql.databases;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgreSqlTestDatabase implements TestDatabase {

    @Override
    public String getUrl() {
        return "jdbc:postgresql://localhost:5432/squiggletest";
    }

    @Override
    public void createTable(Connection connection, String name, TestDatabaseColumn[] columns) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE SEQUENCE " + name + "_id_seq\n" +
                    "START WITH 1\n" +
                    "INCREMENT BY 1\n" +
                    "NO MINVALUE\n" +
                    "NO MAXVALUE\n" +
                    "CACHE 1");
        }
        try (Statement statement = connection.createStatement()) {
            StringBuilder builder = new StringBuilder("CREATE TABLE \"" + name + "\" (\n" +
                    "id INTEGER DEFAULT nextval('" + name + "_id_seq'::regclass) NOT NULL PRIMARY KEY");
            for (TestDatabaseColumn column : columns) {
                builder.append(",\n");
                builder.append(column.getName()).append(' ').append(column.getType());
                if (column.isNotNull()) {
                    builder.append(" NOT NULL");
                }
                if (column.getReference() != null) {
                    builder.append(" REFERENCES ").append('"').append(column.getReference()).append('"');
                }
            }
            builder.append(')');
            statement.execute(builder.toString());
        }
    }

    @Override
    public void dropTable(Connection connection, String name) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS \"" + name + "\" CASCADE");
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP SEQUENCE IF EXISTS " + name + "_id_seq CASCADE");
        }
    }
}
