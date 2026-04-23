package com.backend.housing.domain.entity.rentalcontracts.valueobjects;


import java.util.Objects;
import java.util.UUID;

public class RequestId {

    private final String value;

    public RequestId(String value) {
        this.value = value;
    }

    public static RequestId generate() {
        return new RequestId(UUID.randomUUID().toString());
    }

    public static RequestId of(String value) {
        return new RequestId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestId)) return false;
        RequestId that = (RequestId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}