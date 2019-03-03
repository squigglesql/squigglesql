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
package network.tide.squiggle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Any database tests rely on the following configuration:
 * <p>
 * * Install PostgreSQL
 * * Create user "squiggletest" with password "1" and permission to login
 * * Create database "squiggletest" with "squiggletest" owner
 */
public abstract class TestUtils {

    private static final String USER = "squiggletest";
    private static final String PASSWORD = "1";
    private static final String URL = "jdbc:postgresql://localhost:5432/squiggletest";

    public interface Mapper<T> {

        T apply(Connection connection) throws SQLException;
    }

    public interface Supplier<T> {

        T get() throws SQLException;
    }

    public interface Consumer {

        void accept(Connection connection) throws SQLException;
    }

    public static <T> T withSequence(Connection connection, String name, Supplier<T> supplier) throws SQLException {
        String createQuery = "CREATE SEQUENCE " + name + "\n" +
                "START WITH 1\n" +
                "INCREMENT BY 1\n" +
                "NO MINVALUE\n" +
                "NO MAXVALUE\n" +
                "CACHE 1";
        String dropQuery = "DROP SEQUENCE IF EXISTS " + name + " CASCADE";
        return with(connection, createQuery, dropQuery, supplier);
    }

    public static <T> T withTable(Connection connection, String name, String createQuery,
                                  Supplier<T> supplier) throws SQLException {
        String dropQuery = "DROP TABLE IF EXISTS \"" + name + "\" CASCADE";
        return with(connection, createQuery, dropQuery, supplier);
    }

    public static <T> T with(Connection connection, String createQuery, String dropQuery,
                             Supplier<T> supplier) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(dropQuery);
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute(createQuery);
        }

        T result = supplier.get();

        // We drop the table only if the test succeeds. Otherwise we preserve it for debugging purposes.
        try (Statement statement = connection.createStatement()) {
            statement.execute(dropQuery);
        }
        return result;
    }

    public static <T> T withDatabase(Mapper<T> mapper) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", USER);
        props.setProperty("password", PASSWORD);
        try (Connection connection = DriverManager.getConnection(URL, props)) {
            return mapper.apply(connection);
        }
    }
}
