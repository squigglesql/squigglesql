package com.github.squigglesql.squigglesql.join.multiple;

class Record {

    private final String vehicleName;
    private final String colorName;
    private final String lastName;

    Record(String vehicleName, String colorName, String lastName) {
        this.vehicleName = vehicleName;
        this.colorName = colorName;
        this.lastName = lastName;
    }

    String getVehicleName() {
        return vehicleName;
    }

    String getColorName() {
        return colorName;
    }

    String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        if (vehicleName != null ? !vehicleName.equals(record.vehicleName) : record.vehicleName != null) return false;
        if (colorName != null ? !colorName.equals(record.colorName) : record.colorName != null) return false;
        return lastName != null ? lastName.equals(record.lastName) : record.lastName == null;
    }

    @Override
    public int hashCode() {
        int result = vehicleName != null ? vehicleName.hashCode() : 0;
        result = 31 * result + (colorName != null ? colorName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Record{" +
                "vehicleName='" + vehicleName + '\'' +
                ", colorName='" + colorName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
