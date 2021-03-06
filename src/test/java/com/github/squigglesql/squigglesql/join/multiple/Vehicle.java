/*
 * Copyright 2019-2020 Egor Nepomnyaschih.
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
