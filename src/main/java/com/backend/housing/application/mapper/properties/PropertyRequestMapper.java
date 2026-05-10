package com.backend.housing.application.mapper.properties;

import com.backend.housing.application.commands.properties.CreatePropertyCommand;
import com.backend.housing.application.commands.properties.UpdatePropertyCommand;
import com.backend.housing.application.dto.request.properties.AddressRequest;
import com.backend.housing.application.dto.request.properties.CoordinatesRequest;
import com.backend.housing.application.dto.request.properties.CreatePropertyRequest;
import com.backend.housing.application.dto.request.properties.UpdatePropertyRequest;
import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
@Component
public class PropertyRequestMapper {


    private Coordinates mapFromRequest(CoordinatesRequest request) {
        if (request == null) return null;
        return new Coordinates(
                request.getLongitude(),
                request.getLatitude()
        );
    }

    private Address mapFromRequest(AddressRequest request) {
        if (request == null) return null;
        return new Address(
                request.getStreet(),
                request.getCity(),
                request.getState(),
                request.getCountry(),
                request.getPostalCode()
        );
    }

    public CreatePropertyCommand toCommand(CreatePropertyRequest request,
                                           PropertyId propertyId,
                                           List<String> imageUrls) {
        if (request == null) return null;


        return new CreatePropertyCommand(
                propertyId,
                request.getTitle(),
                request.getDescription(),
                request.getCoordinates(),
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
                request.getAddress()
        );
    }

    public UpdatePropertyCommand toUpdateCommand(UpdatePropertyRequest request,
                                                 PropertyId propertyId,
                                                 Long requestingUserId) {
        if (request == null) return null;

        return new UpdatePropertyCommand(
                propertyId,
                request.getTitle(),
                request.getDescription(),
                mapFromRequest(request.getCoordinates()),
                request.getTransactionType(),
                request.getPriceAmount(),
                request.getTypeProperty(),
                null,
                null,
                request.getNumberOfBedrooms(),
                request.getNumberOfBathrooms(),
                request.getAreaInSquareMeters(),
                request.getPetsAllowed(),
                mapFromRequest(request.getAddress()),
                request.getFurnished(),
                requestingUserId
        );
    }
}