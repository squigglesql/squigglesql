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

public interface TestDatabase {

    String getUrl();

    void createTable(Connection connection, String name, TestDatabaseColumn[] columns) throws SQLException;

    void dropTable(Connection connection, String name) throws SQLException;

    boolean supportsInfinity();

    boolean supportsArrays();

    boolean supportsTimeMs();

    String getTimestampWithTimeZoneType();
}
