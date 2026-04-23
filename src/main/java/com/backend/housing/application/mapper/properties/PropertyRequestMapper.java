package com.backend.housing.application.mapper.properties;

import com.backend.housing.application.commands.properties.CreatePropertyCommand;
import com.backend.housing.application.dto.request.properties.CreatePropertyRequest;
import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PropertyRequestMapper {

    private Coordinates mapCoordinates(Coordinates coordinates) {
        if (coordinates == null) return null;

        return new Coordinates(
                coordinates.getLongitud(),
                coordinates.getLatitud()
        );
    }

    private Address mapAddress(Address address) {
        if (address == null) return null;

        return new Address(
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getPostalCode()
        );
    }

    public CreatePropertyCommand toCommand(CreatePropertyRequest request,
                                           PropertyId propertyId,
                                           List<String> imageUrls) {
        if (request == null) return null;

        Coordinates coordinates = mapCoordinates(request.getCoordinates());
        Address address = mapAddress(request.getAddress());

        return new CreatePropertyCommand(
                propertyId,
                request.getTitle(),
                request.getDescription(),
                coordinates,
                request.getTransactionType(),
                request.getPriceAmount(),
                request.getTypeProperty(),
                null,
                imageUrls,
                request.getNumberOfBedrooms(),
                request.getNumberOfBathrooms(),
                request.getAreaInSquareMeters(),
                request.getPetsAllowed(),
                request.getFurnished(),
                request.getPaymentFrequency(),
                address
        );
    }
}