package com.backend.housing.application.services.rentalcontracts;

import com.backend.housing.application.commands.rentalcontracts.CreateContractCommand;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.enums.PaymentFrequency;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.DateRange;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.PeriodRent;
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


        DateRange period = DateRange.crear(command.getStartDate(), command.getEndDate());

        PeriodRent periodRent = resolvePeriodRent(property, command);

        PaymentFrequency paymentFrequency = property.getRentalTerms().getPaymentFrequency();

        RentalContract contract = RentalContract.create(
                command.getPropertyId(),
                command.getTenantId(),
                owner.getId(),
                command.getStartDate(),
                command.getEndDate(),
                periodRent.getAmount(),
                paymentFrequency
        );

        RentalContract savedContract = contractRepository.save(contract);

        propertyService.markAsRented(command.getPropertyId());

        return savedContract;
    }

    private void validateTenant(Long tenantId) {
        if (!userService.userExists(tenantId)) {
            throw new IllegalArgumentException("El arrendatario no existe: " + tenantId);
        }
    }

    private Property getValidProperty(PropertyId propertyId, User owner) {
        Property property = propertyService.getPropertyBasicInfo(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada"));

        if (!property.getOwnerId().equals(owner.getId())) {
            throw new SecurityException("Usuario no autorizado para crear contrato en esta propiedad");
        }

        if (!propertyService.isAvailableForRent(propertyId)) {
            throw new IllegalStateException("La propiedad no está disponible para arriendo");
        }

        return property;
    }

    private void validateBusinessRules(PropertyId propertyId) {
        if (contractRepository.existsActiveByPropertyId(propertyId)) {
            throw new IllegalStateException("La propiedad ya tiene un contrato activo");
        }
    }

    private PeriodRent resolvePeriodRent(Property property, CreateContractCommand command) {

        if (!property.getPrice().isForRent()) {
            throw new IllegalStateException("La propiedad no tiene un precio de arriendo válido");
        }

        BigDecimal basePrice = property.getPriceAmount();

        BigDecimal finalPrice = command.getMonthlyRent() != null
                ? command.getMonthlyRent()
                : basePrice;

        if (finalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio de arriendo debe ser mayor que cero");
        }

        return PeriodRent.of(finalPrice);
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