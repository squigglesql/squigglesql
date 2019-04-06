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

/**
 * Database table column model. You can obtain this model from a table by calling {@link Table#get(String)} method.
 * To use this table in particular queries, you might need to create references to it by calling
 * {@link TableReference#get(TableColumn)} method.
 */
public class TableColumn implements Compilable {

    private final Table table;
    private final String name;

    /**
     * Creates a table column model. Instead of constructing this object directly, you should call
     * {@link Table#get(String)} method.
     *
     * @param table table to obtain a column from.
     * @param name  column name.
     */
    TableColumn(Table table, String name) {
        this.table = table;
        this.name = name;
    }

    /**
     * @return table this column is defined in.
     */
    public Table getTable() {
        return table;
    }

    /**
     * @return column name.
     */
    public String getName() {
        return name;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.quote(name, compiler.getSyntax().getTableQuote());
    }
}
