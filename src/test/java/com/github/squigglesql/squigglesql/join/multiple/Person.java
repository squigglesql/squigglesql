package com.github.squigglesql.squigglesql.join.multiple;

class Person {

    private final int id;
    private final String lastName;

    Person(int id, String lastName) {
        this.id = id;
        this.lastName = lastName;
    }

    int getId() {
        return id;
    }

    String getLastName() {
        return lastName;
    }
}
