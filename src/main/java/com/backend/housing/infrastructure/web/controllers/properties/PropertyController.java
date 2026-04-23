package com.backend.housing.infrastructure.web.controllers.properties;

import com.backend.housing.application.commands.properties.CreatePropertyCommand;
import com.backend.housing.application.commands.properties.UpdatePropertyCommand;
import com.backend.housing.application.dto.request.properties.CreatePropertyRequest;
import com.backend.housing.application.dto.request.properties.SearchPropertyRequest;
import com.backend.housing.application.dto.response.properties.CreatePropertyResponse;
import com.backend.housing.application.dto.response.properties.PropertyDetailResponse;
import com.backend.housing.application.dto.response.properties.PropertySummaryResponse;
import com.backend.housing.application.mapper.properties.PropertyRequestMapper;
import com.backend.housing.application.mapper.properties.PropertyResponseMapper;
import com.backend.housing.application.mapper.properties.PropertySummaryResponseMapper;
import com.backend.housing.application.mapper.properties.SearchPropertyRequestMapper;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.properties.valueObjects.SearchPropertyQuery;
import com.backend.housing.domain.ports.in.properties.*;
import com.backend.housing.domain.ports.out.external.supabase.ImageStoragePort;
import com.backend.housing.domain.valueobjects.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Properties", description = "Gestión de propiedades inmobiliarias")
@RestController
@RequestMapping("api/properties")
public class PropertyController {

    private final GetMyPropertiesUseCase getMyPropertiesUseCase;
    private final SearchPropertyUseCase searchPropertyUseCase;
    private final ListPropertiesUseCase listPropertiesUseCase;
    private final CreatePropertyUseCase createPropertyUseCase;
    private final GetPropertyUseCase getPropertyUseCase;
    private final PropertyRequestMapper requestMapper;
    private final PropertyResponseMapper propertyResponseMapper;
    private final ImageStoragePort imageStoragePort;
    private final UpdatePropertyUseCase updatePropertyUseCase;
    private final PublishPropertyUseCase publishPropertyUseCase;

    public PropertyController(CreatePropertyUseCase createPropertyUseCase,
                              GetPropertyUseCase getPropertyUseCase,
                              PropertyRequestMapper requestMapper,
                              PropertyResponseMapper propertyResponseMapper,
                              ListPropertiesUseCase listPropertiesUseCase,
                              SearchPropertyUseCase searchPropertyUseCase,
                              ImageStoragePort imageStoragePort,  UpdatePropertyUseCase updatePropertyUseCase,GetMyPropertiesUseCase getMyPropertiesUseCase,
                              PublishPropertyUseCase publishPropertyUseCase) {

        this.createPropertyUseCase = createPropertyUseCase;
        this.getPropertyUseCase = getPropertyUseCase;
        this.requestMapper = requestMapper;
        this.propertyResponseMapper = propertyResponseMapper;
        this.listPropertiesUseCase = listPropertiesUseCase;
        this.searchPropertyUseCase = searchPropertyUseCase;
        this.imageStoragePort = imageStoragePort;
        this.updatePropertyUseCase = updatePropertyUseCase;
        this.getMyPropertiesUseCase = getMyPropertiesUseCase;
        this.publishPropertyUseCase = publishPropertyUseCase;
    }


    @Operation(summary = "Crear una propiedad con imágenes")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreatePropertyResponse> createProperty(
            @RequestPart("data") @Valid CreatePropertyRequest request,
            @RequestPart("files") List<MultipartFile> files) {

        PropertyId propertyId = PropertyId.generate();

        List<String> imageUrls = files.stream()
                .map(file -> imageStoragePort.uploadImage(propertyId.getValue().toString(), file))
                .toList();

        CreatePropertyCommand command = requestMapper.toCommand(request, propertyId, imageUrls);

        Property property = createPropertyUseCase.createProperty(command);

        return ResponseEntity.ok(propertyResponseMapper.toCreateResponse(property));
    }

    @Operation(summary = "Obtener propiedad con todos los detalles")
    @GetMapping("/{id}")
    public PropertyDetailResponse getProperty(@PathVariable UUID id) {
        Property property = getPropertyUseCase.getProperty(PropertyId.of(id));

        return propertyResponseMapper.toDetailResponse(property);
    }

    @Operation(summary = "Obtener la lista de todas las propiedades")
    @GetMapping
    public List<PropertySummaryResponse> listProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pagination pagination = Pagination.of(page, size);

        List<Property> properties = listPropertiesUseCase.listProperties(pagination);

        return propertyResponseMapper.toSummaryResponseList(properties);
    }

    @Operation(summary = "Buscar propiedades con filtros")
    @GetMapping("/search")
    public List<PropertySummaryResponse> searchProperties(@Valid SearchPropertyRequest request) {

        SearchPropertyQuery query = SearchPropertyRequestMapper.toQuery(request);

        List<Property> properties = searchPropertyUseCase.searchProperties(query);

        return PropertySummaryResponseMapper.toResponseList(properties);
    }


    @Operation(summary = "Subir imágenes de una propiedad existente")
    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> uploadImages(
            @PathVariable UUID id,
            @RequestParam("files") List<MultipartFile> files) {

        List<String> newUrls = files.stream()
                .map(file -> imageStoragePort.uploadImage(id.toString(), file))
                .toList();

        UpdatePropertyCommand command = new UpdatePropertyCommand(
                PropertyId.of(id),
                null, null, null, null, null, null, null,
                newUrls,
                null, null, null, null, null, null
        );

        updatePropertyUseCase.update(command);

        return ResponseEntity.ok(newUrls);
    }

    @Operation(summary = "Listar imágenes de una propiedad")
    @GetMapping("/{id}/images")
    public ResponseEntity<List<String>> listImages(@PathVariable UUID id) {

        List<String> urls = imageStoragePort.listImages(id.toString());

        return ResponseEntity.ok(urls);
    }


    @Operation(summary = "Eliminar una imagen de una propiedad")
    @DeleteMapping("/{id}/images")
    public ResponseEntity<Void> deleteImage(
            @PathVariable UUID id,
            @RequestParam("url") String imageUrl) {

        imageStoragePort.deleteImage(imageUrl);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener las propiedades del usuario autenticado")
    @GetMapping("/my-properties")
    public List<PropertySummaryResponse> getMyProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) PropertyStatus status) {

        Pagination pagination = Pagination.of(page, size);

        List<Property> properties =
                getMyPropertiesUseCase.getMyProperties(pagination, status);

        return propertyResponseMapper.toSummaryResponseList(properties);
    }

    @Operation(summary = "Publicar una propiedad")
    @PatchMapping("/{id}/publish")
    public ResponseEntity<Void> publishProperty(@PathVariable UUID id) {

        publishPropertyUseCase.publish(PropertyId.of(id));

        return ResponseEntity.noContent().build();
    }
}