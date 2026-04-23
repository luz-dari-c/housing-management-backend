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


        public RentalRequest toDomain(CreateRentalRequestRequest request, Long tenantId, Long ownerId) {
            DateRange period = new DateRange(
                    LocalDate.parse(request.getStartDate()),
                    LocalDate.parse(request.getEndDate())
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
            RentalRequestResponse response = new RentalRequestResponse();
            response.setRequestId(rentalRequest.getId().getValue().toString());
            response.setPropertyId(rentalRequest.getPropertyId());
            response.setTenantId(rentalRequest.getTenantId());
            response.setOwnerId(rentalRequest.getOwnerId());
            response.setStartDate(rentalRequest.getPeriod().getStartDate().atStartOfDay());
            response.setEndDate(rentalRequest.getPeriod().getEndDate().atStartOfDay());
            response.setProposedRent(rentalRequest.getProposedRent());
            response.setStatus(rentalRequest.getStatus());
            response.setCreatedAt(rentalRequest.getCreatedAt());
            response.setRespondedAt(rentalRequest.getRespondedAt());
            return response;
        }

        public CreateRentalRequestCommand toCommand(CreateRentalRequestRequest request, Long tenantId) {
            return new CreateRentalRequestCommand(
                    new PropertyId(UUID.fromString(request.getPropertyId())),
                    tenantId,
                    LocalDate.parse(request.getStartDate()),
                    LocalDate.parse(request.getEndDate()),
                    request.getProposedRent()
            );
        }

        public RequestId toRequestId(String id) {
            return new RequestId(id);
        }
    }
