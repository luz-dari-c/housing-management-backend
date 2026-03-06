package com.backend.housing.infrastructure.persistence.mappers;

import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.infrastructure.persistence.entities.AddressEmbeddable;
import com.backend.housing.infrastructure.persistence.entities.CoordinatesEmbeddable;
import com.backend.housing.infrastructure.persistence.entities.PropertyEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PropertyEntityMapper {

    public PropertyEntity toEntity(Property domain) {
        if (domain == null) return null;

        PropertyEntity entity = new PropertyEntity();
        entity.setPropertyId(mapToLong(domain.getId()));
        entity.setTitle(domain.getTitle());
        entity.setDescription(domain.getDescription());
        entity.setCoordinates(mapToEmbeddable(domain.getCoordinates()));
        entity.setSalePrice(domain.getSalePrice());
        entity.setRentPrice(domain.getRentPrice());
        entity.setModality(domain.getModality());
        entity.setPropertyStatus(domain.getStatus());
        entity.setOwnerId(domain.getOwnerId());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setPublishedAt(domain.getPublishedAt());
        entity.setImageUrls(domain.getImageUrls());
        entity.setNumberOfBedrooms(domain.getNumberOfBedrooms());
        entity.setNumberOfBathrooms(domain.getNumberOfBathrooms());
        entity.setAreaInSquareMeters(domain.getAreaInSquareMeters());
        entity.setPetsAllowed(domain.isPetsAllowed());
        entity.setFurnished(domain.isFurnished());
        entity.setAddress(mapToEmbeddable(domain.getAddress()));

        return entity;
    }

    public Property toDomain(PropertyEntity entity) {
        if (entity == null) return null;

        return new Property(
                mapToPropertyId(entity.getPropertyId()),
                entity.getTitle(),
                entity.getDescription(),
                mapToCoordinates(entity.getCoordinates()),
                entity.getSalePrice(),
                entity.getRentPrice(),
                entity.getModality(),
                entity.getPropertyStatus(),
                entity.getOwnerId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getPublishedAt(),
                entity.getImageUrls() != null ? entity.getImageUrls() : Collections.emptyList(),
                entity.getNumberOfBedrooms(),
                entity.getNumberOfBathrooms(),
                entity.getAreaInSquareMeters(),
                entity.isPetsAllowed(),
                mapToAddress(entity.getAddress()),
                entity.isFurnished()
        );
    }

    public List<Property> toDomainList(List<PropertyEntity> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(this::toDomain).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Long mapToLong(PropertyId propertyId) {
        return propertyId != null ? propertyId.getValue() : null;
    }

    private PropertyId mapToPropertyId(Long id) {
        return id != null ? new PropertyId(id) : null;
    }

    private CoordinatesEmbeddable mapToEmbeddable(Coordinates coordinates) {
        if (coordinates == null) return null;
        CoordinatesEmbeddable emb = new CoordinatesEmbeddable();
        emb.setLatitud(coordinates.getLatitud());
        emb.setLongitud(coordinates.getLongitud());
        return emb;
    }

    private Coordinates mapToCoordinates(CoordinatesEmbeddable emb) {
        if (emb == null) return null;
        return new Coordinates(emb.getLatitud(), emb.getLongitud());
    }

    private AddressEmbeddable mapToEmbeddable(Address address) {
        if (address == null) return null;
        AddressEmbeddable emb = new AddressEmbeddable();
        emb.setStreet(address.getStreet());
        emb.setCity(address.getCity());
        emb.setState(address.getState());
        emb.setCountry(address.getCountry());
        emb.setPostalCode(address.getPostalCode());
        return emb;
    }

    private Address mapToAddress(AddressEmbeddable emb) {
        if (emb == null) return null;
        return new Address(
                emb.getStreet(),
                emb.getCity(),
                emb.getState(),
                emb.getCountry(),
                emb.getPostalCode()
        );
    }
}