package com.backend.housing.application.mapper.rentalcontracts;

import com.backend.housing.application.commands.rentalcontracts.CreateRentalRequestCommand;
import com.backend.housing.application.dto.request.rentalcontracts.CreateRentalRequestRequest;
import com.backend.housing.application.dto.response.rentalcontracts.RentalRequestResponse;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.RentalRequest;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.DateRange;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.RequestId;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class RentalRequestMapper {

    public RentalRequest toDomain(CreateRentalRequestRequest request, Long tenantId, Long ownerId, LocalDate endDate) {
        DateRange period = new DateRange(
                LocalDate.parse(request.getStartDate()),
                endDate
        );

        return RentalRequest.create(
                new PropertyId(UUID.fromString(request.getPropertyId())),
                tenantId,
                ownerId,
                period,
                request.getProposedRent()
        );
    }

    public RentalRequestResponse toResponse(RentalRequest rentalRequest) {
        String message = "Tu solicitud de arriendo ha sido enviada exitosamente";
        String nextStep = "Espera a que el propietario responda tu solicitud. Puedes cancelarla en cualquier momento mientras esté pendiente.";

        return new RentalRequestResponse(
                rentalRequest.getId().getValue().toString(),
                rentalRequest.getPropertyId(),
                rentalRequest.getTenantId(),
                rentalRequest.getOwnerId(),
                rentalRequest.getPeriod().getStartDate().atStartOfDay(),
                rentalRequest.getPeriod().getEndDate().atStartOfDay(),
                rentalRequest.getProposedRent(),
                rentalRequest.getStatus(),
                rentalRequest.getCreatedAt(),
                rentalRequest.getRespondedAt(),
                message,
                nextStep
        );
    }

    public CreateRentalRequestCommand toCommand(CreateRentalRequestRequest request, Long tenantId) {
        return new CreateRentalRequestCommand(
                new PropertyId(UUID.fromString(request.getPropertyId())),
                tenantId,
                LocalDate.parse(request.getStartDate()),
                request.getDuration(),
                request.getProposedRent()
        );
    }

    public RequestId toRequestId(String id) {
        return new RequestId(id);
    }
}