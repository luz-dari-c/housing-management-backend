package com.backend.housing.application.services.properties;

import com.backend.housing.application.commands.properties.CreatePropertyCommand;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import com.backend.housing.domain.entity.properties.enums.TransactionType;
import com.backend.housing.domain.entity.properties.valueObjects.Price;
import com.backend.housing.domain.entity.properties.valueObjects.RentalTerms;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.exceptions.UserNotFoundException;
import com.backend.housing.domain.ports.in.properties.CreatePropertyUseCase;
import com.backend.housing.domain.ports.out.properties.PropertyRepository;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import com.backend.housing.domain.ports.out.users.UserRoleServicePort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CreatePropertyService implements CreatePropertyUseCase {

    private final PropertyRepository propertyRepository;
    private final UserValidationPort userValidationPort;
    private final UserRoleServicePort userRoleServicePort;

    public CreatePropertyService(
            PropertyRepository propertyRepository,
            UserValidationPort userValidationPort,
            UserRoleServicePort userRoleServicePort
    ) {
        this.propertyRepository = propertyRepository;
        this.userValidationPort = userValidationPort;
        this.userRoleServicePort = userRoleServicePort;
    }

    @Override
    public Property createProperty(CreatePropertyCommand command) {

        String email = getUserEmailFromToken();

        User user = userValidationPort.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        Long ownerId = user.getId();

        ensureOwnerRole(ownerId);

        Price price = resolvePrice(command);

        RentalTerms rentalTerms = null;
        if (command.getTransactionType() == TransactionType.RENT) {
            rentalTerms = new RentalTerms(
                    command.getPaymentFrequency(),
                    command.getPetsAllowed(),
                    command.getFurnished()
            );
        }

        Property property = new Property(
                command.getPropertyId(),
                command.getTitle(),
                command.getDescription(),
                command.getCoordinates(),
                price,
                command.getTypeProperty(),
                PropertyStatus.CREATED,
                ownerId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                command.getImageUrls() != null ? command.getImageUrls() : List.of(),
                command.getNumberOfBedrooms(),
                command.getNumberOfBathrooms(),
                command.getAreaInSquareMeters(),
                command.getAddress(),
                rentalTerms

                );

        return propertyRepository.save(property);
    }

    private void ensureOwnerRole(Long userId) {
        if (!userRoleServicePort.isUserOwner(userId)) {
            userRoleServicePort.assignOwnerRole(userId);
        }
    }

    private String getUserEmailFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        return authentication.getName();
    }

    private Price resolvePrice(CreatePropertyCommand command) {
        if (command.getTransactionType() == TransactionType.SALE) {
            return Price.forSale(command.getPriceAmount());
        }
        return Price.forRent(command.getPriceAmount());
    }
}