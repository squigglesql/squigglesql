package com.github.squigglesql.squigglesql.join.single;

class Color {

    private final int id;
    private final String color;

    Color(int id, String color) {
        this.id = id;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Color color1 = (Color) o;

        if (id != color1.id) return false;
        return color != null ? color.equals(color1.color) : color1.color == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Color{" +
                "id=" + id +
                ", color='" + color + '\'' +
                '}';
    }
}
