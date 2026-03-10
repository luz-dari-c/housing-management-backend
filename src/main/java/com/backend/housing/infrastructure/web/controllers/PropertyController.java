package com.backend.housing.infrastructure.web.controllers;


import com.backend.housing.application.Commands.properties.CreatePropertyCommand;
import com.backend.housing.application.dto.request.properties.CreatePropertyRequest;
import com.backend.housing.application.dto.response.properties.CreatePropertyResponse;
import com.backend.housing.application.mappers.properties.PropertyRequestMapper;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.ports.in.properties.CreatePropertyUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/properties")

public class PropertyController {

   private final CreatePropertyUseCase propertyUseCase;
    private final PropertyRequestMapper requestMapper;

    public PropertyController(CreatePropertyUseCase propertyUseCase, PropertyRequestMapper requestMapper){

        this.propertyUseCase = propertyUseCase;
        this.requestMapper = requestMapper;

    }

    @PostMapping
    public ResponseEntity<CreatePropertyResponse> createProperty(@Valid @RequestBody CreatePropertyRequest request){

        CreatePropertyCommand command = requestMapper.toCommand(request);

        Property property = propertyUseCase.createProperty(command);

        CreatePropertyResponse response = new CreatePropertyResponse(
                property.getId().getValue(),
                property.getTitle(),
                property.getDescription(),
                property.getCoordinates(),
                property.getSalePrice(),
                property.getRentPrice(),
                property.getModality(),
                property.getStatus(),
                property.getOwnerId(),
                property.getCreatedAt(),
                property.getUpdatedAt(),
                property.getPublishedAt(),
                property.getImageUrls(),
                property.getNumberOfBedrooms(),
                property.getNumberOfBathrooms(),
                property.getAreaInSquareMeters(),
                property.isPetsAllowed(),
                property.isFurnished(),
                property.getAddress()
        );

        return ResponseEntity.ok(response);

    }



}
