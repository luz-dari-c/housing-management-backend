package com.backend.housing.domain.entity.properties.valueObjects;

import java.util.Objects;

public class PropertyId {
    private final Long value;

    public PropertyId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Property ID must be a positive number");
        }
        this.value = value;
    }

    public static PropertyId of(Long value) {
        return new PropertyId(value);
    }

    public static PropertyId generate() {

        return new PropertyId(System.nanoTime());
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyId that = (PropertyId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}