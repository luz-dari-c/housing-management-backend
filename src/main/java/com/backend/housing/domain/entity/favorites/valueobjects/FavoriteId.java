package com.backend.housing.domain.entity.favorites.valueobjects;

import java.util.Objects;
import java.util.UUID;

public class FavoriteId {

    private final UUID value;

    private FavoriteId(UUID value) {
        this.value = Objects.requireNonNull(value);
    }

    public static FavoriteId generate() {
        return new FavoriteId(UUID.randomUUID());
    }

    public static FavoriteId of(UUID value) {
        return new FavoriteId(value);
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteId that)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}