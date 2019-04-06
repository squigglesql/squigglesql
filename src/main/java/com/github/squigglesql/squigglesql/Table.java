/*
 * Copyright 2004-2019 Joe Walnes, Guillaume Chauvet, Egor Nepomnyaschih.
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

import java.util.HashMap;
import java.util.Map;

/**
 * Database table model. You should create a single table model for every table that you have in your database. Then
 * you should define its columns by calling {@link Table#get(String)} method. To use this table in particular queries,
 * you might need to create references to it by calling {@link Table#refer()} method.
 */
public class Table {

    private final String name;

    private final Map<String, TableColumn> columnCache = new HashMap<>();

    /**
     * Creates a table model.
     *
     * @param name table name.
     */
    public Table(String name) {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Table name can not be empty.");
        }
        this.name = name;
    }

    /**
     * @return table name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a table column model.
     *
     * @param columnName column name.
     * @return column model.
     */
    public TableColumn get(String columnName) {
        if (columnName == null || columnName.equals("")) {
            throw new IllegalArgumentException("Table column name can not be empty.");
        }
        TableColumn column = columnCache.get(columnName);
        if (column == null) {
            column = new TableColumn(this, columnName);
            columnCache.put(columnName, column);
        }
        return column;
    }

    /**
     * Creates a new table reference that you can further use in an SQL query. You may create multiple references to
     * a single table to build complex SQL queries, and every reference will obtain an unique alias.
     *
     * @return new table reference.
     */
    public TableReference refer() {
        return new TableReference(this);
    }
}
