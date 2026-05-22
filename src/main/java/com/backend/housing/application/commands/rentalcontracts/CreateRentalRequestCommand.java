package com.backend.housing.application.commands.rentalcontracts;

import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
public class CreateRentalRequestCommand {

    private final PropertyId propertyId;
    private final Long tenantId;
    private final LocalDate startDate;
    private final Integer duration;
    private final BigDecimal proposedRent;

    public CreateRentalRequestCommand(PropertyId propertyId,
                                      Long tenantId,
                                      LocalDate startDate,
                                      Integer duration,
                                      BigDecimal proposedRent) {

        this.propertyId = Objects.requireNonNull(propertyId, "El id de la propiedad es requerido");
        this.tenantId = Objects.requireNonNull(tenantId, "El id del inquilino es requerido");
        this.startDate = Objects.requireNonNull(startDate, "Fecha de inicio es requerida");
        this.duration = Objects.requireNonNull(duration, "Duracion es requerida");
        this.proposedRent = proposedRent;

        validateStartDate();
    }

    private void validateStartDate() {
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Fecha de inicio no puede ser en el pasado");
        }
    }
}