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
package io.zatarox.squiggle;

import java.util.HashMap;
import java.util.Map;

public class Table {

    private final String name;

    private final Map<String, TableColumn> columnCache = new HashMap<String, TableColumn>();

    public Table(String name) {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Table name can not be empty.");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

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

    public TableReference refer() {
        return new TableReference(this);
    }
}
