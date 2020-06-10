/*
 * Copyright 2019 Egor Nepomnyaschih.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.squigglesql.squigglesql.databases;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class H2TestDatabase implements TestDatabase {

  @Override
  public String getUrl() {
    return "jdbc:h2:mem:squiggletest";
  }

  @Override
  public void createTable(Connection connection, String name, TestDatabaseColumn[] columns) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      statement.execute("CREATE SEQUENCE \"" + name + "_id_seq\"\n" + "START WITH 1\n" + "INCREMENT BY 1\n" + "NO MINVALUE\n" + "NO MAXVALUE\n" + "CACHE 1");
    }
    try (Statement statement = connection.createStatement()) {
      StringBuilder builder = new StringBuilder("CREATE TABLE \"" + name + "\" (\n" + "\"id\" INTEGER DEFAULT NEXT VALUE FOR \"" + name + "_id_seq\" NOT NULL PRIMARY KEY");
      for (TestDatabaseColumn column : columns) {
        builder.append(",\n");
        builder.append('"').append(column.getName()).append("\" ").append(column.getType(this));
        if (column.isNotNull()) {
          builder.append(" NOT NULL");
        }
      }
      for (TestDatabaseColumn column : columns) {
        if (column.getReference() != null) {
          builder.append(",\n");
          builder.append("FOREIGN KEY (\"").append(column.getName()).append("\") REFERENCES \"").append(column.getReference()).append("\" (\"id\")").append(" ON UPDATE CASCADE ON DELETE CASCADE");
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
      statement.execute("DROP SEQUENCE IF EXISTS " + name + "_id_seq");
    }
  }

  @Override
  public boolean supportsInfinity() {
    return true;
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
    return "TIMESTAMP WITH TIME ZONE";
  }
}
