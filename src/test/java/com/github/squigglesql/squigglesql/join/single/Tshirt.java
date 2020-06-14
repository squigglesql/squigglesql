/*
 * Copyright 2020 Egor Nepomnyaschih.
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
package com.github.squigglesql.squigglesql.join.single;

class Tshirt {

    private final int id;
    private final String size;
    private final Integer colorId; // not Color here, because some Records contain a color without a tshirt

    Tshirt(int id, String size, Integer colorId) {
        this.id = id;
        this.size = size;
        this.colorId = colorId;
    }

    int getId() {
        return id;
    }

    String getSize() {
        return size;
    }

    Integer getColorId() {
        return colorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tshirt tshirt = (Tshirt) o;

        if (id != tshirt.id) return false;
        if (size != null ? !size.equals(tshirt.size) : tshirt.size != null) return false;
        return colorId != null ? colorId.equals(tshirt.colorId) : tshirt.colorId == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (colorId != null ? colorId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tshirt{" +
                "id=" + id +
                ", size='" + size + '\'' +
                ", colorId=" + colorId +
                '}';
    }
}
