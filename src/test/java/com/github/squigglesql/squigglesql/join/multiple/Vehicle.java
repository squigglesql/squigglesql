package com.github.squigglesql.squigglesql.join.multiple;

class Vehicle {

    private final int id;
    private final String name;
    private final Integer colorId; // not Color here, because some Records contain a color without a tshirt
    private final Integer personId; // same

    Vehicle(int id, String name, Integer colorId, Integer personId) {
        this.id = id;
        this.name = name;
        this.colorId = colorId;
        this.personId = personId;
    }

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }

    Integer getColorId() {
        return colorId;
    }

    Integer getPersonId() {
        return personId;
    }
}
