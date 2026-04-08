package com.backend.housing.application.mapper.rentalcontracts;

import com.backend.housing.application.commands.rentalcontracts.CreateContractCommand;
import com.backend.housing.application.dto.request.rentalcontracts.CreateContractRequest;
import com.backend.housing.application.dto.response.rentalcontracts.ContractResponse;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import org.springframework.stereotype.Component;

@Component
public class ContractMapper {

    public CreateContractCommand toCommand(CreateContractRequest request, Long ownerId) {
        if (request == null) return null;

        return CreateContractCommand.builder()
                .propertyId(PropertyId.of(request.getPropertyId()))
                .tenantId(request.getTenantId())
                .ownerId(ownerId)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .monthlyRent(request.getMonthlyRent())
                .paymentFrequency(request.getPaymentFrequency())
                .build();
    }

    public ContractResponse toResponse(RentalContract contract, Property property,
                                       String tenantName, String ownerName) {

        if (contract == null || property == null) return null;

        return new ContractResponse(
                contract.getId().getValue(),
                contract.getPropertyId().getValue(),
                property.getTitle(),
                contract.getTenantId(),
                tenantName,
                contract.getOwnerId(),
                ownerName,
                contract.getPeriod().getStartDate(),
                contract.getPeriod().getEndDate(),
                contract.getMonthlyRent().getAmount(),
                contract.getStatus(),
                contract.getCreatedAt()
        );
    }
}