package com.backend.housing.infrastructure.web.controllers.properties;

import com.backend.housing.application.commands.properties.CreatePropertyCommand;
import com.backend.housing.application.commands.properties.UpdatePropertyCommand;
import com.backend.housing.application.dto.request.properties.CreatePropertyRequest;
import com.backend.housing.application.dto.request.properties.UpdatePropertyRequest;
import com.backend.housing.application.dto.request.properties.SearchPropertyRequest;
import com.backend.housing.application.dto.response.properties.CreatePropertyResponse;
import com.backend.housing.application.dto.response.properties.PropertyDetailResponse;
import com.backend.housing.application.dto.response.properties.PropertySummaryResponse;
import com.backend.housing.application.dto.response.properties.UpdatePropertyResponse;
import com.backend.housing.application.mapper.properties.PropertyRequestMapper;
import com.backend.housing.application.mapper.properties.PropertyResponseMapper;
import com.backend.housing.application.mapper.properties.PropertySummaryResponseMapper;
import com.backend.housing.application.mapper.properties.SearchPropertyRequestMapper;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.properties.valueObjects.SearchPropertyQuery;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.properties.CreatePropertyUseCase;
import com.backend.housing.domain.ports.in.properties.DeletePropertyUseCase;
import com.backend.housing.domain.ports.in.properties.DisablePropertyUseCase;
import com.backend.housing.domain.ports.in.properties.GetMyPropertiesUseCase;
import com.backend.housing.domain.ports.in.properties.GetPropertyUseCase;
import com.backend.housing.domain.ports.in.properties.ListPropertiesUseCase;
import com.backend.housing.domain.ports.in.properties.PublishPropertyUseCase;
import com.backend.housing.domain.ports.in.properties.SearchPropertyUseCase;
import com.backend.housing.domain.ports.in.properties.UpdatePropertyUseCase;
import com.backend.housing.domain.ports.out.external.supabase.ImageStoragePort;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import com.backend.housing.domain.valueobjects.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Properties", description = "Gestión de propiedades inmobiliarias")
@RestController
@RequestMapping("api/properties")
public class PropertyController {

    private final CreatePropertyUseCase createPropertyUseCase;
    private final GetPropertyUseCase getPropertyUseCase;
    private final ListPropertiesUseCase listPropertiesUseCase;
    private final GetMyPropertiesUseCase getMyPropertiesUseCase;
    private final SearchPropertyUseCase searchPropertyUseCase;
    private final UpdatePropertyUseCase updatePropertyUseCase;
    private final PublishPropertyUseCase publishPropertyUseCase;
    private final DeletePropertyUseCase deletePropertyUseCase;
    private final DisablePropertyUseCase disablePropertyUseCase;
    private final ImageStoragePort imageStoragePort;
    private final UserValidationPort userValidationPort;
    private final PropertyRequestMapper requestMapper;
    private final PropertyResponseMapper propertyResponseMapper;

    public PropertyController(CreatePropertyUseCase createPropertyUseCase,
                              GetPropertyUseCase getPropertyUseCase,
                              ListPropertiesUseCase listPropertiesUseCase,
                              GetMyPropertiesUseCase getMyPropertiesUseCase,
                              SearchPropertyUseCase searchPropertyUseCase,
                              UpdatePropertyUseCase updatePropertyUseCase,
                              PublishPropertyUseCase publishPropertyUseCase,
                              DeletePropertyUseCase deletePropertyUseCase,
                              DisablePropertyUseCase disablePropertyUseCase,
                              ImageStoragePort imageStoragePort,
                              UserValidationPort userValidationPort,
                              PropertyRequestMapper requestMapper,
                              PropertyResponseMapper propertyResponseMapper) {

        this.createPropertyUseCase = createPropertyUseCase;
        this.getPropertyUseCase = getPropertyUseCase;
        this.listPropertiesUseCase = listPropertiesUseCase;
        this.getMyPropertiesUseCase = getMyPropertiesUseCase;
        this.searchPropertyUseCase = searchPropertyUseCase;
        this.updatePropertyUseCase = updatePropertyUseCase;
        this.publishPropertyUseCase = publishPropertyUseCase;
        this.deletePropertyUseCase = deletePropertyUseCase;
        this.disablePropertyUseCase = disablePropertyUseCase;
        this.imageStoragePort = imageStoragePort;
        this.userValidationPort = userValidationPort;
        this.requestMapper = requestMapper;
        this.propertyResponseMapper = propertyResponseMapper;
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
    public ResponseEntity<PropertyDetailResponse> getProperty(@PathVariable UUID id) {
        PropertyDetailResponse response = getPropertyUseCase.getPropertyDetail(PropertyId.of(id));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener la lista de todas las propiedades")
    @GetMapping
    public ResponseEntity<List<PropertySummaryResponse>> listProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Property> properties = listPropertiesUseCase.listProperties(Pagination.of(page, size));
        return ResponseEntity.ok(propertyResponseMapper.toSummaryResponseList(properties));
    }

    @Operation(summary = "Buscar propiedades con filtros")
    @GetMapping("/search")
    public ResponseEntity<List<PropertySummaryResponse>> searchProperties(
            @Valid @ModelAttribute SearchPropertyRequest request) {

        SearchPropertyQuery query = SearchPropertyRequestMapper.toQuery(request);
        List<Property> properties = searchPropertyUseCase.searchProperties(query);
        return ResponseEntity.ok(PropertySummaryResponseMapper.toResponseList(properties));
    }

    @Operation(summary = "Obtener las propiedades del usuario autenticado")
    @GetMapping("/my-properties")
    public ResponseEntity<List<PropertySummaryResponse>> getMyProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) PropertyStatus status) {

        if (status == PropertyStatus.DELETED) {
            return ResponseEntity.badRequest().build();
        }

        List<Property> properties = getMyPropertiesUseCase.getMyProperties(
                Pagination.of(page, size), status
        );

        return ResponseEntity.ok(propertyResponseMapper.toSummaryResponseList(properties));
    }

    @Operation(summary = "Subir imágenes de una propiedad existente")
    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> uploadImages(
            @PathVariable UUID id,
            @RequestParam("files") List<MultipartFile> files) {

        User user = getAuthenticatedUser();

        List<String> newUrls = files.stream()
                .map(file -> imageStoragePort.uploadImage(id.toString(), file))
                .toList();

        UpdatePropertyCommand command = new UpdatePropertyCommand(
                PropertyId.of(id),
                null, null, null, null, null, null, null,
                newUrls,
                null, null, null, null, null, null,
                user.getId()
        );

        updatePropertyUseCase.update(command);

        return ResponseEntity.ok(newUrls);
    }

    @Operation(summary = "Listar imágenes de una propiedad")
    @GetMapping("/{id}/images")
    public ResponseEntity<List<String>> listImages(@PathVariable UUID id) {
        return ResponseEntity.ok(imageStoragePort.listImages(id.toString()));
    }

    @Operation(summary = "Eliminar una imagen de una propiedad")
    @DeleteMapping("/{id}/images")
    public ResponseEntity<Void> deleteImage(
            @PathVariable UUID id,
            @RequestParam("url") String imageUrl) {

        imageStoragePort.deleteImage(imageUrl);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Publicar una propiedad")
    @PatchMapping("/{id}/publish")
    public ResponseEntity<Void> publishProperty(@PathVariable UUID id) {
        publishPropertyUseCase.publish(PropertyId.of(id));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Editar información de una propiedad")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UpdatePropertyResponse> updateProperty(
            @PathVariable UUID id,
            @RequestPart("data") @Valid UpdatePropertyRequest request) {

        User user = getAuthenticatedUser();

        UpdatePropertyCommand command = requestMapper.toUpdateCommand(request, PropertyId.of(id), user.getId());

        Property property = updatePropertyUseCase.update(command);

        return ResponseEntity.ok(propertyResponseMapper.toUpdateResponse(property));
    }

    @Operation(summary = "Deshabilitar una propiedad publicada (vuelve a CREATED)")
    @PatchMapping("/{id}/disable")
    public ResponseEntity<Void> disableProperty(@PathVariable UUID id) {
        User user = getAuthenticatedUser();
        disablePropertyUseCase.execute(PropertyId.of(id), user.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar una propiedad (borrado lógico)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable UUID id) {
        User user = getAuthenticatedUser();
        deletePropertyUseCase.execute(PropertyId.of(id), user.getId());
        return ResponseEntity.noContent().build();
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }
        return userValidationPort.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}