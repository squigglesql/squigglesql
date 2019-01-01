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

public class Table {

    private final String name;

    public Table(String name) {
        if (name == null || name.equals("")) {
            throw new RuntimeException("Table name can not be empty.");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TableColumn get(String columnName) {
        if (columnName == null || columnName.equals("")) {
            throw new RuntimeException("Table column name can not be empty.");
        }
        return new TableColumn(this, columnName);
    }

    public TableReference refer() {
        return new TableReference(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table table = (Table) o;

        return name.equals(table.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
