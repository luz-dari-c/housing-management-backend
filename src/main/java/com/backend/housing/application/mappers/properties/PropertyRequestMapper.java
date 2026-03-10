package com.backend.housing.application.mappers.properties;

import com.backend.housing.application.Commands.properties.CreatePropertyCommand;
import com.backend.housing.application.dto.request.properties.AddressRequest;
import com.backend.housing.application.dto.request.properties.CoordinatesRequest;
import com.backend.housing.application.dto.request.properties.CreatePropertyRequest;
import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;
import org.springframework.stereotype.Component;

@Component
public class PropertyRequestMapper {

    private Coordinates mapCoordinates(CoordinatesRequest request) {
        if (request == null) return null;

        return new Coordinates(
                request.getLongitude(),
                request.getLatitude()
        );
    }

    private Address mapAddress(AddressRequest request){
        if (request == null) return null;

        return new Address(
                request.getStreet(),
                request.getState(),
                request.getCity(),
                request.getCountry(),
                request.getPostalCode()
        );
    }

    public CreatePropertyCommand toCommand(CreatePropertyRequest request) {

        Coordinates coordinates = mapCoordinates(request.getCoordinates());
        Address address = mapAddress(request.getAddress());

        return new CreatePropertyCommand(
                request.getOwnerId(),
                request.getTitle(),
                request.getDescription(),
                coordinates,
                request.getSalePrice(),
                request.getRentPrice(),
                request.getModality(),
                null,
                request.getImageUrls(),
                request.getNumberOfBedrooms(),
                request.getNumberOfBathrooms(),
                request.getAreaInSquareMeters(),
                request.isPetsAllowed(),
                address,
                request.isFurnished()
        );
    }
}