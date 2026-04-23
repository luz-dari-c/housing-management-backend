package com.backend.housing.domain.entity.rentalcontracts.valueobjects;

import java.util.Objects;
import java.util.UUID;

public class ContractId {
    private final UUID value;

    private ContractId(UUID value){
        this.value = value;
    }

    public static ContractId of(UUID value){
        if (value == null) {
            throw new IllegalArgumentException("Contract ID can't be null for existing contracts");
        }
        return new ContractId(value);
    }

    public static ContractId of(String value){
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Contract ID can't be null or blank for existing contracts");
        }
        return new ContractId(UUID.fromString(value));
    }

    public static ContractId generate(){
        return new ContractId(UUID.randomUUID());
    }




    public static ContractId empty(){
        return new ContractId(null);
    }

    public UUID getValue() {
        if (value == null) {
            throw new IllegalStateException("Cannot get value of empty ContractId");
        }
        return value;
    }

    public boolean isEmpty() {
        return value == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractId that = (ContractId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : "null";
    }
}