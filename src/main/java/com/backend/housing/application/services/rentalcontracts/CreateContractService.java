package com.backend.housing.application.services.rentalcontracts;

import com.backend.housing.application.commands.rentalcontracts.CreateContractCommand;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.DateRange;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.MonthlyRent;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.rentalcontracts.CreateContractUseCase;
import com.backend.housing.domain.ports.out.external.PropertyServicePort;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CreateContractService implements CreateContractUseCase {

    private final RentalContractRepository contractRepository;
    private final PropertyServicePort propertyService;
    private final UserValidationPort userService;

    public CreateContractService(RentalContractRepository contractRepository,
                                 PropertyServicePort propertyService,
                                 UserValidationPort userService) {
        this.contractRepository = contractRepository;
        this.propertyService = propertyService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public RentalContract createContract(CreateContractCommand command) {

        User owner = getAuthenticatedUser();

        validateTenant(command.getTenantId());

        Property property = getValidProperty(command.getPropertyId(), owner);

        validateBusinessRules(command.getPropertyId());

        DateRange period = DateRange.of(command.getStartDate(), command.getEndDate());

        MonthlyRent monthlyRent = resolveMonthlyRent(property, command);

        RentalContract contract = RentalContract.create(
                command.getPropertyId(),
                command.getTenantId(),
                owner.getId(),
                period,
                monthlyRent
        );

        RentalContract savedContract = contractRepository.save(contract);

        propertyService.markAsRented(command.getPropertyId());

        return savedContract;
    }

    private void validateTenant(Long tenantId) {
        if (!userService.userExists(tenantId)) {
            throw new IllegalArgumentException("Tenant does not exist: " + tenantId);
        }
    }

    private Property getValidProperty(PropertyId propertyId, User owner) {
        Property property = propertyService.getPropertyBasicInfo(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found"));

        if (!property.getOwnerId().equals(owner.getId())) {
            throw new SecurityException("User is not the owner of the property");
        }

        if (!propertyService.isAvailableForRent(propertyId)) {
            throw new IllegalStateException("Property is not available for rent");
        }

        return property;
    }

    private void validateBusinessRules(PropertyId propertyId) {
        if (contractRepository.existsActiveByPropertyId(propertyId)) {
            throw new IllegalStateException("Property already has an active contract");
        }
    }

    private MonthlyRent resolveMonthlyRent(Property property, CreateContractCommand command) {

        if (!property.getPrice().isForRent()) {
            throw new IllegalStateException("Property is not configured for rent");
        }

        BigDecimal basePrice = property.getPriceAmount();

        BigDecimal finalPrice = command.getMonthlyRent() != null
                ? command.getMonthlyRent()
                : basePrice;

        if (finalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monthly rent must be greater than zero");
        }

        return MonthlyRent.of(finalPrice);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        String email = authentication.getName();

        return userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}