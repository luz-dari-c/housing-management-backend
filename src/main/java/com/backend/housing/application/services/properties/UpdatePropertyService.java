package com.backend.housing.application.services.properties;

import com.backend.housing.application.commands.properties.UpdatePropertyCommand;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.enums.TransactionType;
import com.backend.housing.domain.entity.properties.valueObjects.Price;
import com.backend.housing.domain.entity.properties.valueObjects.RentalTerms;
import com.backend.housing.domain.exceptions.InvalidNotFoundException;
import com.backend.housing.domain.ports.in.properties.UpdatePropertyUseCase;
import com.backend.housing.domain.ports.out.properties.PropertyRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdatePropertyService implements UpdatePropertyUseCase {

    private final PropertyRepository propertyRepository;

    public UpdatePropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public Property update(UpdatePropertyCommand command) {

        Property property = propertyRepository.findById(command.getId())
                .orElseThrow(() ->
                        new InvalidNotFoundException("Property not found with id: " + command.getId())
                );

        Price price = resolvePrice(command);
        RentalTerms rentalTerms = resolveRentalTerms(command, property);

        property.update(
                command.getTitle(),
                command.getDescription(),
                command.getCoordinates(),
                price,
                command.getAddress(),
                command.getNumberOfBedrooms(),
                command.getNumberOfBathrooms(),
                command.getAreaInSquareMeters(),
                rentalTerms
        );

        if (command.getImageUrls() != null) {
            property.addImages(command.getImageUrls());
        }

        return propertyRepository.save(property);
    }

    @Override
    public Property updateFromDomain(Property property) {
        return propertyRepository.save(property);
    }

    private Price resolvePrice(UpdatePropertyCommand command) {
        if (command.getPriceAmount() == null || command.getTransactionType() == null) {
            return null;
        }

        if (command.getTransactionType() == TransactionType.SALE) {
            return Price.forSale(command.getPriceAmount());
        }

        return Price.forRent(command.getPriceAmount());
    }



    private RentalTerms resolveRentalTerms(UpdatePropertyCommand command, Property property) {

        if (command.getPetsAllowed() == null && command.getFurnished() == null) {
            return null;
        }

        boolean petsAllowed = command.getPetsAllowed() != null
                ? command.getPetsAllowed()
                : property.getRentalTerms().isPetsAllowed();

        boolean furnished = command.getFurnished() != null
                ? command.getFurnished()
                : property.getRentalTerms().isFurnished();

        return new RentalTerms(
                property.getRentalTerms().getPaymentFrequency(),
                petsAllowed,
                furnished
        );
    }
}