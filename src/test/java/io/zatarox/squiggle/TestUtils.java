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
package io.zatarox.squiggle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Any database tests rely on the following configuration:
 *
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

    public static <T> T withDatabase(Mapper<T> mapper) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", USER);
        props.setProperty("password", PASSWORD);
        Connection connection;
        try {
            connection = DriverManager.getConnection(URL, props);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            return mapper.apply(connection);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
