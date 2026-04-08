package com.backend.housing.domain.entity.properties.valueObjects;

import java.util.Objects;
import java.util.UUID;

public class PropertyId {
    private final UUID value;

    public PropertyId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Property ID cannot be null");
        }
        this.value = value;
    }

    public static PropertyId of(UUID value) {
        return new PropertyId(value);
    }

    public static PropertyId of(String value) {
        return new PropertyId(UUID.fromString(value));
    }

    public static PropertyId generate() {
        return new PropertyId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }

    public String asString() {
        return value.toString();
    }

    public static PropertyId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("PropertyId cannot be null");
        }
        // Convierte Long a UUID (usando el número como base)
        return new PropertyId(new UUID(0, value));
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
        return value.toString();
    }
}