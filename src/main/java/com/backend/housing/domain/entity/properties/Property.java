package com.backend.housing.domain.entity.properties;

import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import com.backend.housing.domain.entity.properties.enums.TransactionType;
import com.backend.housing.domain.entity.properties.enums.TypeProperty;
import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;
import com.backend.housing.domain.entity.properties.valueObjects.Price;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.properties.valueObjects.RentalTerms;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Property {

    private final PropertyId id;
    private String title;
    private String description;
    private Coordinates coordinates;
    private Price price;
    private TypeProperty typeProperty;
    private PropertyStatus status;
    private final Long ownerId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
    private List<String> imageUrls;
    private Integer numberOfBedrooms;
    private Integer numberOfBathrooms;
    private Integer areaInSquareMeters;
    private Address address;
    private RentalTerms rentalTerms;

    public Property(PropertyId id, String title, String description,
                    Coordinates coordinates, Price price, TypeProperty typeProperty,
                    PropertyStatus status, Long ownerId, LocalDateTime createdAt,
                    LocalDateTime updatedAt, LocalDateTime publishedAt, List<String> imageUrls,
                    Integer numberOfBedrooms, Integer numberOfBathrooms,
                    Integer areaInSquareMeters, Address address,
                    RentalTerms rentalTerms) {

        this.id = Objects.requireNonNull(id);
        this.title = validateTitle(title);
        this.description = description;
        this.coordinates = coordinates;
        this.price = Objects.requireNonNull(price);
        this.typeProperty = Objects.requireNonNull(typeProperty);
        this.ownerId = Objects.requireNonNull(ownerId);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
        this.status = Objects.requireNonNull(status);
        this.address = Objects.requireNonNull(address);
        this.publishedAt = publishedAt;
        this.imageUrls = imageUrls != null ? new ArrayList<>(imageUrls) : new ArrayList<>();
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.areaInSquareMeters = areaInSquareMeters;
        this.rentalTerms = rentalTerms;

        validateArea();
        validateBedroomsAndBathrooms();
        validateRentalTerms();
    }

    public static Property create(PropertyId id, String title, String description,
                                  Coordinates coordinates, Price price, TypeProperty typeProperty,
                                  Long ownerId, Address address, Integer numberOfBedrooms,
                                  Integer numberOfBathrooms, Integer areaInSquareMeters,
                                  RentalTerms rentalTerms) {

        return new Property(
                id, title, description, coordinates, price, typeProperty,
                PropertyStatus.DRAFT, ownerId, LocalDateTime.now(), LocalDateTime.now(),
                null, new ArrayList<>(), numberOfBedrooms, numberOfBathrooms,
                areaInSquareMeters, address, rentalTerms
        );
    }

    public static Property reconstitute(PropertyId id, String title, String description,
                                        Coordinates coordinates, Price price, TypeProperty typeProperty,
                                        PropertyStatus status, Long ownerId, LocalDateTime createdAt,
                                        LocalDateTime updatedAt, LocalDateTime publishedAt,
                                        List<String> imageUrls, Integer numberOfBedrooms,
                                        Integer numberOfBathrooms, Integer areaInSquareMeters,
                                        Address address, RentalTerms rentalTerms) {

        return new Property(
                id, title, description, coordinates, price, typeProperty, status, ownerId,
                createdAt, updatedAt, publishedAt, imageUrls, numberOfBedrooms,
                numberOfBathrooms, areaInSquareMeters, address, rentalTerms
        );
    }

    public void update(
            String title,
            String description,
            Coordinates coordinates,
            Price price,
            Address address,
            Integer numberOfBedrooms,
            Integer numberOfBathrooms,
            Integer areaInSquareMeters,
            RentalTerms rentalTerms
    ) {
        if (title != null) this.title = title;
        if (description != null) this.description = description;
        if (coordinates != null) this.coordinates = coordinates;
        if (price != null) this.price = price;
        if (address != null) this.address = address;
        if (numberOfBedrooms != null) this.numberOfBedrooms = numberOfBedrooms;
        if (numberOfBathrooms != null) this.numberOfBathrooms = numberOfBathrooms;
        if (areaInSquareMeters != null) this.areaInSquareMeters = areaInSquareMeters;
        if (rentalTerms != null) this.rentalTerms = rentalTerms;

        this.updatedAt = LocalDateTime.now();
    }
    private void validateRentalTerms() {
        if (price.isForRent() && rentalTerms == null) {
            throw new IllegalStateException();
        }
        if (price.isForSale() && rentalTerms != null) {
            throw new IllegalStateException();
        }
    }

    public void addImages(List<String> newImages) {
        if (newImages == null || newImages.isEmpty()) return;

        this.imageUrls.addAll(newImages);
        this.updatedAt = LocalDateTime.now();
    }



    public void publish() {
        if (this.status != PropertyStatus.DRAFT && this.status != PropertyStatus.CREATED) {
            throw new IllegalStateException();
        }
        validateRentalTerms();
        this.status = PropertyStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsRented() {
        if (this.status != PropertyStatus.PUBLISHED) {
            throw new IllegalStateException();
        }
        if (!price.isForRent()) {
            throw new IllegalStateException();
        }
        this.status = PropertyStatus.RENTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsAvailable() {
        if (this.status != PropertyStatus.RENTED) {
            throw new IllegalStateException("Only rented properties can be made available again");
        }
        this.status = PropertyStatus.PUBLISHED;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsSold() {
        if (this.status != PropertyStatus.PUBLISHED) {
            throw new IllegalStateException();
        }
        if (!price.isForSale()) {
            throw new IllegalStateException();
        }
        this.status = PropertyStatus.SOLD;
        this.updatedAt = LocalDateTime.now();
    }

    private String validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (title.length() < 3 || title.length() > 200) {
            throw new IllegalArgumentException();
        }
        return title.trim();
    }

    private void validateArea() {
        if (areaInSquareMeters != null && areaInSquareMeters <= 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateBedroomsAndBathrooms() {
        if (numberOfBedrooms != null && numberOfBedrooms < 0) {
            throw new IllegalArgumentException();
        }
        if (numberOfBathrooms != null && numberOfBathrooms < 0) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isAvailableForRent() {
        return status == PropertyStatus.PUBLISHED && price.isForRent();
    }

    public boolean isAvailableForSale() {
        return status == PropertyStatus.PUBLISHED && price.isForSale();
    }

    public PropertyId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = validateTitle(title);
        this.updatedAt = LocalDateTime.now();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        this.updatedAt = LocalDateTime.now();
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
        validateRentalTerms();
        this.updatedAt = LocalDateTime.now();
    }

    public TypeProperty getTypeProperty() {
        return typeProperty;
    }

    public void setTypeProperty(TypeProperty typeProperty) {
        this.typeProperty = typeProperty;
        this.updatedAt = LocalDateTime.now();
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public List<String> getImageUrls() {
        return List.copyOf(imageUrls);
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = new ArrayList<>(imageUrls);
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(Integer numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
        validateBedroomsAndBathrooms();
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public void setNumberOfBathrooms(Integer numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
        validateBedroomsAndBathrooms();
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getAreaInSquareMeters() {
        return areaInSquareMeters;
    }

    public void setAreaInSquareMeters(Integer areaInSquareMeters) {
        this.areaInSquareMeters = areaInSquareMeters;
        validateArea();
        this.updatedAt = LocalDateTime.now();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        this.updatedAt = LocalDateTime.now();
    }

    public RentalTerms getRentalTerms() {
        return rentalTerms;
    }

    public void setRentalTerms(RentalTerms rentalTerms) {
        this.rentalTerms = rentalTerms;
        validateRentalTerms();
        this.updatedAt = LocalDateTime.now();
    }

    public TransactionType getTransactionType() {
        return price.getTransactionType();
    }

    public BigDecimal getPriceAmount() {
        return price.getAmount();
    }
}