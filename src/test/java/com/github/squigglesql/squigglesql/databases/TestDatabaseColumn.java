package com.github.squigglesql.squigglesql.databases;

public class TestDatabaseColumn {

    private final String name;
    private final String type;
    private final boolean notNull;
    private final String references;

    public TestDatabaseColumn(String name, String type, boolean notNull, String references) {
        this.name = name;
        this.type = type;
        this.notNull = notNull;
        this.references = references;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public String getReference() {
        return references;
    }
}
