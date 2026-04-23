package com.backend.housing.domain.entity.notifications.valueobjects;

import java.util.Objects;
import java.util.UUID;

public class NotificationId {

    private final UUID value;

    private NotificationId(UUID value) {
        this.value = value;
    }

    public static NotificationId of(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("NotificationId value cannot be null");
        }
        return new NotificationId(value);
    }

    public static NotificationId generate() {
        return new NotificationId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationId that = (NotificationId) o;
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