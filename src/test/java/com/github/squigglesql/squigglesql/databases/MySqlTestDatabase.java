package com.github.squigglesql.squigglesql.databases;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlTestDatabase implements TestDatabase {

    @Override
    public String getUrl() {
        return "jdbc:mysql://localhost:3306/squiggletest?serverTimezone=UTC";
    }

    @Override
    public void createTable(Connection connection, String name, TestDatabaseColumn[] columns) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            StringBuilder builder = new StringBuilder("CREATE TABLE `" + name + "` (\n" +
                    "id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY");
            for (TestDatabaseColumn column : columns) {
                builder.append(",\n");
                builder.append(column.getName()).append(' ').append(column.getType(this));
                if (column.isNotNull()) {
                    builder.append(" NOT");
                }
                builder.append(" NULL");
            }
            for (TestDatabaseColumn column : columns) {
                if (column.getReference() != null) {
                    builder.append(",\n");
                    builder.append("FOREIGN KEY (`").append(column.getName())
                            .append("`) REFERENCES `").append(column.getReference()).append("`(id)")
                            .append(" ON UPDATE CASCADE ON DELETE CASCADE");
                }
            }
            builder.append(')');
            statement.execute(builder.toString());
        }
    }

    @Override
    public void dropTable(Connection connection, String name) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS `" + name + "` CASCADE");
        }
    }

    @Override
    public boolean supportsInfinity() {
        return false;
    }

    @Override
    public boolean supportsArrays() {
        return false;
    }

    @Override
    public boolean supportsTimeMs() {
        return false;
    }

    @Override
    public String getTimestampWithTimeZoneType() {
        return "TIMESTAMP(3)";
    }
}
