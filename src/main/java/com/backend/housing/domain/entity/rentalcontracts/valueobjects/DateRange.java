package com.backend.housing.domain.entity.rentalcontracts.valueobjects;

import java.time.LocalDate;
import java.util.Objects;

public class DateRange {
    private final LocalDate startDate;
    private final LocalDate endDate;

    private DateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Para CREAR NUEVOS (con validación de fecha futura)
    public static DateRange crear(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser nula");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("La fecha de fin no puede ser nula");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser en el pasado");
        }
        return new DateRange(startDate, endDate);
    }

    // Para RECONSTRUIR desde BD (SIN validación de fecha pasada)
    public static DateRange of(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser nula");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("La fecha de fin no puede ser nula");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
        return new DateRange(startDate, endDate);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    public boolean contains(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateRange that = (DateRange) o;
        return Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }

    @Override
    public String toString() {
        return startDate + " → " + endDate;
    }
}