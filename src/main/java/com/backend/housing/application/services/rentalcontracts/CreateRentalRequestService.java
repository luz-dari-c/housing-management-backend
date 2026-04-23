package com.backend.housing.application.services.rentalcontracts;

import com.backend.housing.application.commands.rentalcontracts.CreateRentalRequestCommand;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.rentalcontracts.RentalRequest;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.DateRange;
import com.backend.housing.domain.ports.in.rentalcontracts.CreateRentalRequestUseCase;
import com.backend.housing.domain.ports.out.external.PropertyServicePort;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalRequestRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CreateRentalRequestService implements CreateRentalRequestUseCase {

    private final RentalRequestRepository repository;
    private final PropertyServicePort propertyService;
    private final UserValidationPort userService;

    public CreateRentalRequestService(RentalRequestRepository repository,
                                      PropertyServicePort propertyService,
                                      UserValidationPort userService) {
        this.repository = repository;
        this.propertyService = propertyService;
        this.userService = userService;
    }

    @Override
    public RentalRequest create(CreateRentalRequestCommand command) {

        if (!userService.userExists(command.getTenantId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario (tenant) no existe");
        }

        Property property = propertyService.getPropertyBasicInfo(command.getPropertyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La propiedad no fue encontrada"));

        if (property.getOwnerId().equals(command.getTenantId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes solicitar arriendo de tu propia propiedad");
        }

        if (!property.isAvailableForRent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La propiedad no está disponible para arriendo");
        }

        DateRange period;
        try {
            period = DateRange.of(command.getStartDate(), command.getEndDate());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rango de fechas inválido: " + e.getMessage());
        }

        RentalRequest request = RentalRequest.create(
                command.getPropertyId(),
                command.getTenantId(),
                property.getOwnerId(),
                period,
                command.getProposedRent()
        );

        return repository.save(request);
    }
}