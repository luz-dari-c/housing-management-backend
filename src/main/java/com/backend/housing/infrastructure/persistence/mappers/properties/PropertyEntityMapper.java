package com.backend.housing.infrastructure.persistence.mappers.properties;

import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.enums.TransactionType;
import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;
import com.backend.housing.domain.entity.properties.valueObjects.Price;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.properties.valueObjects.RentalTerms;
import com.backend.housing.infrastructure.persistence.entities.properties.AddressEmbeddable;
import com.backend.housing.infrastructure.persistence.entities.properties.CoordinatesEmbeddable;
import com.backend.housing.infrastructure.persistence.entities.properties.PropertyEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PropertyEntityMapper {

    public PropertyEntity toEntity(Property domain) {
        if (domain == null) return null;

        PropertyEntity entity = new PropertyEntity();
        entity.setPropertyId(mapToUUID(domain.getId()));
        entity.setTitle(domain.getTitle());
        entity.setDescription(domain.getDescription());
        entity.setCoordinates(mapToEmbeddable(domain.getCoordinates()));

        if (domain.getTransactionType() == TransactionType.SALE) {
            entity.setSalePrice(domain.getPriceAmount());
            entity.setRentPrice(null);
            entity.setPaymentFrequency(null);
            entity.setPetsAllowed(false);
            entity.setFurnished(false);
        } else {
            entity.setSalePrice(null);
            entity.setRentPrice(domain.getPriceAmount());

            RentalTerms terms = domain.getRentalTerms();
            if (terms != null) {
                entity.setPaymentFrequency(terms.getPaymentFrequency());
                entity.setPetsAllowed(terms.isPetsAllowed());
                entity.setFurnished(terms.isFurnished());
            }
        }

        entity.setTypeProperty(domain.getTypeProperty());
        entity.setPropertyStatus(domain.getStatus());
        entity.setOwnerId(domain.getOwnerId());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setPublishedAt(domain.getPublishedAt());
        entity.setImageUrls(domain.getImageUrls());
        entity.setNumberOfBedrooms(domain.getNumberOfBedrooms());
        entity.setNumberOfBathrooms(domain.getNumberOfBathrooms());
        entity.setAreaInSquareMeters(domain.getAreaInSquareMeters());
        entity.setAddress(mapToEmbeddable(domain.getAddress()));

        return entity;
    }

    public Property toDomain(PropertyEntity entity) {
        if (entity == null) return null;

        Price price;
        RentalTerms rentalTerms = null;

        if (entity.getSalePrice() != null) {
            price = Price.forSale(entity.getSalePrice());
        } else if (entity.getRentPrice() != null) {
            price = Price.forRent(entity.getRentPrice());

            rentalTerms = new RentalTerms(
                    entity.getPaymentFrequency(),
                    entity.getPetsAllowed(),
                    entity.getFurnished()
            );
        } else {
            throw new IllegalStateException();
        }

        return new Property(
                mapToPropertyId(entity.getPropertyId()),
                entity.getTitle(),
                entity.getDescription(),
                mapToCoordinates(entity.getCoordinates()),
                price,
                entity.getTypeProperty(),
                entity.getPropertyStatus(),
                entity.getOwnerId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getPublishedAt(),
                entity.getImageUrls() != null ? entity.getImageUrls() : Collections.emptyList(),
                entity.getNumberOfBedrooms(),
                entity.getNumberOfBathrooms(),
                entity.getAreaInSquareMeters(),
                entity.getAddress() != null ? mapToAddress(entity.getAddress()) : null,
                rentalTerms
        );
    }

    public List<Property> toDomainList(List<PropertyEntity> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDomain)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private UUID mapToUUID(PropertyId propertyId) {
        return propertyId != null ? propertyId.getValue() : null;
    }

    private PropertyId mapToPropertyId(UUID uuid) {
        return uuid != null ? PropertyId.of(uuid) : null;
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