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

import java.util.function.Function;

public class TestDatabaseColumn {

    private final String name;
    private final Function<TestDatabase, String> type;
    private final boolean notNull;
    private final String references;

    public TestDatabaseColumn(String name, Function<TestDatabase, String> type, boolean notNull, String references) {
        this.name = name;
        this.type = type;
        this.notNull = notNull;
        this.references = references;
    }

    public TestDatabaseColumn(String name, String type, boolean notNull, String references) {
        this(name, db -> type, notNull, references);
    }

    public String getName() {
        return name;
    }

    public String getType(TestDatabase database) {
        return type.apply(database);
    }

    public boolean isNotNull() {
        return notNull;
    }

    public String getReference() {
        return references;
    }
}
