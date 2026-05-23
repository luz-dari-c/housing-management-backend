package com.backend.housing.application.dto.request.rentalcontracts;

import com.backend.housing.domain.entity.properties.enums.PaymentFrequency;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CreateContractRequest {

    @NotNull(message = "El ID de la propiedad es obligatorio")
    private UUID propertyId;

    @NotNull(message = "El ID del arrendatario es obligatorio")
    private Long tenantId;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser anterior a hoy")
    private LocalDate startDate;

    @NotNull(message = "La fecha de finalización es obligatoria")
    private LocalDate endDate;

    @NotNull(message = "El valor del periodo del arriendo es obligatorio")
    @DecimalMin(value = "0.01", message = "El valor del periodo del arriendo debe ser mayor que cero")
    private BigDecimal monthlyRent;

    @NotNull(message = "La frecuencia de pago es obligatoria")
    private PaymentFrequency paymentFrequency;

    @AssertTrue(message = "La fecha de finalización debe ser posterior a la fecha de inicio")
    public boolean isDateRangeValid() {
        if (startDate == null || endDate == null) return true;
        return endDate.isAfter(startDate);
    }
}