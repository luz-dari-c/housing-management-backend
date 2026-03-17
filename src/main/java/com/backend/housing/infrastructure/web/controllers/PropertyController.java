package com.backend.housing.infrastructure.web.controllers;
import com.backend.housing.application.mapper.properties.PropertySummaryResponseMapper;
import com.backend.housing.domain.ports.in.properties.SearchPropertyUseCase;
import com.backend.housing.application.dto.request.properties.SearchPropertyRequest;
import com.backend.housing.application.mapper.properties.SearchPropertyRequestMapper;
import com.backend.housing.domain.entity.properties.valueObjects.SearchPropertyQuery;

import com.backend.housing.application.Commands.properties.CreatePropertyCommand;
import com.backend.housing.application.dto.request.properties.CreatePropertyRequest;
import com.backend.housing.application.dto.response.properties.CreatePropertyResponse;
import com.backend.housing.application.dto.response.properties.PropertyDetailResponse;
import com.backend.housing.application.dto.response.properties.PropertySummaryResponse;
import com.backend.housing.application.mapper.properties.PropertyRequestMapper;
import com.backend.housing.application.mapper.properties.PropertyResponseMapper;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.ports.in.properties.CreatePropertyUseCase;
import com.backend.housing.domain.ports.in.properties.GetPropertyUseCase;
import com.backend.housing.domain.ports.in.properties.ListPropertiesUseCase;
import com.backend.housing.domain.valueObjects.Pagination;
import com.backend.housing.infrastructure.config.FakeUserConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Properties", description = "Gestión de propiedades inmobiliarias")
@RestController
@RequestMapping("api/properties")

public class PropertyController {

    private final SearchPropertyUseCase searchPropertyUseCase;
    private final ListPropertiesUseCase listPropertiesUseCase;
    private final CreatePropertyUseCase propertyUseCase;
    private final PropertyRequestMapper requestMapper;
    private final PropertyResponseMapper propertyResponseMapper;
    private final GetPropertyUseCase getPropertyUseCase;

    public PropertyController(CreatePropertyUseCase propertyUseCase, PropertyRequestMapper requestMapper,
                              GetPropertyUseCase getPropertyUseCase, PropertyResponseMapper propertyResponseMapper, ListPropertiesUseCase listPropertiesUseCase
    ,SearchPropertyUseCase searchPropertyUseCase) {

        this.propertyUseCase = propertyUseCase;
        this.requestMapper = requestMapper;
        this.getPropertyUseCase = getPropertyUseCase;
        this.propertyResponseMapper = propertyResponseMapper;
        this.listPropertiesUseCase = listPropertiesUseCase;
        this.searchPropertyUseCase = searchPropertyUseCase;

    }

    @Operation (summary = "Crear una propiedad")
    @PostMapping
    public ResponseEntity<CreatePropertyResponse> createProperty(@Valid @RequestBody CreatePropertyRequest request) {

        CreatePropertyCommand command = requestMapper.toCommand(request);

        Property property = propertyUseCase.createProperty(command);

        CreatePropertyResponse response = new CreatePropertyResponse(
                property.getId().getValue(),
                property.getTitle(),
                property.getDescription(),
                property.getCoordinates(),
                property.getSalePrice(),
                property.getRentPrice(),
                property.getTypeProperty(),
                property.getStatus(),
                property.getOwnerId(),
                property.getUpdatedAt(),
                property.getPublishedAt(),
                property.getImageUrls(),
                property.getAddress()
        );

        return ResponseEntity.ok(response);

    }


    @Operation (summary = "Obtener propiedad con todos los detalles")
    @GetMapping("/{id}")
    public PropertyDetailResponse getProperty(@PathVariable Long id,
                                              @RequestHeader(value = "X_USER_ID", required = false)
                                        UUID userId) {
        if (userId == null) {
            userId = FakeUserConfig.TEST_USER_ID;
        }

        Property property = getPropertyUseCase.getProperty(new PropertyId(id), userId);


        return propertyResponseMapper.toResponse(property);

    }

    @Operation (summary = "Obtener la lista de todas las propiedades")
    @GetMapping
    public List<PropertySummaryResponse> listProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pagination pagination = Pagination.of(page, size);

        List<Property> properties =
                listPropertiesUseCase.listProperties(pagination);

        return PropertyResponseMapper.toResponseList(properties);
    }

    @Operation(summary = "Buscar propiedades con filtros")
    @GetMapping("/search")
    public List<PropertySummaryResponse> searchProperties(
            SearchPropertyRequest request
    ) {

        SearchPropertyQuery query =
                SearchPropertyRequestMapper.toQuery(request);

        List<Property> properties =
                searchPropertyUseCase.searchProperties(query);

        return PropertySummaryResponseMapper.toResponseList(properties);
    }


}
