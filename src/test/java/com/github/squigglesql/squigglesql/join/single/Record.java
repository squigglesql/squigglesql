package com.github.squigglesql.squigglesql.join.single;

class Record {

    private final Tshirt tshirt;
    private final Color color;

    Record(Tshirt tshirt, Color color) {
        this.tshirt = tshirt;
        this.color = color;
    }

    Tshirt getTshirt() {
        return tshirt;
    }

    Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        if (tshirt != null ? !tshirt.equals(record.tshirt) : record.tshirt != null) return false;
        return color != null ? color.equals(record.color) : record.color == null;
    }

    @Override
    public int hashCode() {
        int result = tshirt != null ? tshirt.hashCode() : 0;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Record{" +
                "tshirt=" + tshirt +
                ", color=" + color +
                '}';
    }
}
