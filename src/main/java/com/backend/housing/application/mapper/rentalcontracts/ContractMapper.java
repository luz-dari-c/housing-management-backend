package com.backend.housing.application.mapper.rentalcontracts;

import com.backend.housing.application.commands.rentalcontracts.CreateContractCommand;
import com.backend.housing.application.dto.request.rentalcontracts.CreateContractRequest;
import com.backend.housing.application.dto.response.rentalcontracts.ContractResponse;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.users.User;
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
                                       User tenant, User owner) {

        if (contract == null || property == null || tenant == null || owner == null) return null;

        String tenantFullName = buildFullName(tenant);
        String ownerFullName = buildFullName(owner);

        return new ContractResponse(
                contract.getId().getValue(),
                contract.getPropertyId().getValue(),
                property.getTitle(),
                contract.getTenantId(),
                tenantFullName,
                tenant.getCedula(),
                contract.getOwnerId(),
                ownerFullName,
                owner.getCedula(),
                contract.getPeriod().getStartDate(),
                contract.getPeriod().getEndDate(),
                contract.getPeriodRent().getAmount(),
                contract.getStatus(),
                contract.getCreatedAt(),
                contract.getPaymentFrequency()
        );
    }

    private String buildFullName(User user) {
        String firstName = user.getPrimerNombre() != null ? user.getPrimerNombre() : "";
        String secondName = user.getSegundoNombre() != null ? user.getSegundoNombre() : "";
        String firstLastName = user.getPrimerApellido() != null ? user.getPrimerApellido() : "";
        String secondLastName = user.getSegundoApellido() != null ? user.getSegundoApellido() : "";

        String fullName = firstName;
        if (!secondName.isEmpty()) fullName += " " + secondName;
        fullName += " " + firstLastName;
        if (!secondLastName.isEmpty()) fullName += " " + secondLastName;

        return fullName.trim();
    }
}