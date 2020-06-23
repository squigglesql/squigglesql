/*
 * Copyright 2019-2020 Egor Nepomnyaschih and contributors.
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
package com.github.squigglesql.squigglesql;

import com.github.squigglesql.squigglesql.databases.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Any database tests rely on the following configuration:
 * <p>
 * * Install PostgreSQL and MySQL
 * * In both, create database "squiggletest"
 * * In both, create user "squiggletest" with password "1", permission to login to "squiggletest" database
 * and full access to it.
 */
public abstract class TestUtils {

    private static final String USER = "squiggletest";
    private static final String PASSWORD = "1";

    public interface Supplier<T> {

        T get() throws SQLException;
    }

    public interface Consumer {

        void accept(Connection connection, TestDatabase database) throws SQLException;
    }

    public interface Runnable {

        void run() throws SQLException;
    }

    public static <T> T withTable(Connection connection, TestDatabase database, String name,
                                  TestDatabaseColumn[] columns, Supplier<T> supplier) throws SQLException {
        return with(
                () -> database.createTable(connection, name, columns),
                () -> database.dropTable(connection, name),
                supplier);
    }

    public static void withDatabase(Consumer consumer) throws SQLException {
        withPostgreSqlDatabase(consumer);
        withMySqlDatabase(consumer);
        withH2Database(consumer);
    }

    private static void withPostgreSqlDatabase(Consumer consumer) throws SQLException {
        withDatabase(new PostgreSqlTestDatabase(), consumer);
    }

    private static void withMySqlDatabase(Consumer consumer) throws SQLException {
        withDatabase(new MySqlTestDatabase(), consumer);
    }

    private static void withH2Database(Consumer consumer) throws SQLException {
        withDatabase(new H2TestDatabase(), consumer);
    }

    private static void withDatabase(TestDatabase database, Consumer consumer) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", USER);
        props.setProperty("password", PASSWORD);
        try (Connection connection = DriverManager.getConnection(database.getUrl(), props)) {
            consumer.accept(connection, database);
        }
    }

    private static <T> T with(Runnable creator, Runnable dropper, Supplier<T> supplier) throws SQLException {
        dropper.run();
        creator.run();

        T result = supplier.get();

        // We drop the table only if the test succeeds. Otherwise we preserve it for debugging purposes.
        dropper.run();
        return result;
    }
}
