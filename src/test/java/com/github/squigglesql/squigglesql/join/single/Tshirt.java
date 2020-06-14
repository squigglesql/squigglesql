package com.github.squigglesql.squigglesql.join.single;

class Tshirt {

    private final int id;
    private final String size;
    private final Integer colorId;

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
