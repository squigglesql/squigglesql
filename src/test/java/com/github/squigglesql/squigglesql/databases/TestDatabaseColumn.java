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
