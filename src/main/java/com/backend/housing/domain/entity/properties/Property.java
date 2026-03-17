package com.backend.housing.domain.entity.properties;

import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Property {
    private final PropertyId id;
    private String title;
    private String description;
    private Coordinates coordinates;
    private BigDecimal salePrice;
    private BigDecimal rentPrice;
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
    private boolean petsAllowed;
    private boolean furnished;
    private Address address;
    private RentType rentType;

    public Property(PropertyId id, String title, String description,
                    Coordinates coordinates, BigDecimal salePrice,
                    BigDecimal rentPrice, TypeProperty typeProperty, PropertyStatus status,
                    Long ownerId, LocalDateTime createdAt, LocalDateTime updatedAt,
                    LocalDateTime publishedAt, List<String> imageUrls,
                    Integer numberOfBedrooms, Integer numberOfBathrooms,
                    Integer areaInSquareMeters, boolean petsAllowed,
                    Address address, boolean furnished, RentType rentType) {

        this.id = Objects.requireNonNull(id, "Property ID cannot be null");
        this.title = validateTitle(title);
        this.description = description;
        this.coordinates = coordinates;
        this.typeProperty = Objects.requireNonNull(typeProperty, "Type property cannot be null");
        this.ownerId = Objects.requireNonNull(ownerId, "Owner ID cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.address = Objects.requireNonNull(address, "Address cannot be null");

        validatePricesByModality(salePrice, rentPrice, typeProperty);
        this.salePrice = salePrice;
        this.rentPrice = rentPrice;

        this.publishedAt = publishedAt;
        this.imageUrls = imageUrls != null ? new ArrayList<>(imageUrls) : new ArrayList<>();
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.areaInSquareMeters = areaInSquareMeters;
        this.petsAllowed = petsAllowed;
        this.furnished = furnished;
        this.rentType = rentType;

        validateArea();
        validateBedroomsAndBathrooms();
    }

    private String validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (title.length() < 3 || title.length() > 200) {
            throw new IllegalArgumentException("Title must be between 3 and 200 characters");
        }
        return title.trim();
    }

    private void validatePricesByModality(BigDecimal salePrice, BigDecimal rentPrice, TypeProperty typeProperty) {
        if (salePrice == null && rentPrice == null) {
            throw new IllegalArgumentException("At least one price (sale or rent) must be provided for HOUSE or APARTMENT");
        }
        if (salePrice != null && salePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Sale price must be greater than zero");
        }
        if (rentPrice != null && rentPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Rent price must be greater than zero");
        }
    }

    private void validateArea() {
        if (areaInSquareMeters != null && areaInSquareMeters <= 0) {
            throw new IllegalArgumentException("Area must be greater than zero if provided");
        }
    }

    private void validateBedroomsAndBathrooms() {
        if (numberOfBedrooms != null && numberOfBedrooms < 0) {
            throw new IllegalArgumentException("Number of bedrooms cannot be negative");
        }
        if (numberOfBathrooms != null && numberOfBathrooms < 0) {
            throw new IllegalArgumentException("Number of bathrooms cannot be negative");
        }
    }

    public void publish() {
        if (this.status != PropertyStatus.DRAFT && this.status != PropertyStatus.CREATED) {
            throw new IllegalStateException("Only properties in DRAFT or CREATED status can be published");
        }
        this.status = PropertyStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String title, String description, BigDecimal salePrice,
                       BigDecimal rentPrice, Address address, Coordinates coordinates,
                       Integer numberOfBedrooms, Integer numberOfBathrooms,
                       Integer areaInSquareMeters, Boolean petsAllowed, Boolean furnished) {

        if (this.status != PropertyStatus.DRAFT && this.status != PropertyStatus.CREATED) {
            throw new IllegalStateException("Only properties in DRAFT or CREATED status can be updated");
        }

        if (title != null) {
            this.title = validateTitle(title);
        }
        if (description != null) {
            this.description = description;
        }
        if (address != null) {
            this.address = address;
        }
        if (coordinates != null) {
            this.coordinates = coordinates;
        }
        if (numberOfBedrooms != null) {
            if (numberOfBedrooms < 0) {
                throw new IllegalArgumentException("Number of bedrooms cannot be negative");
            }
            this.numberOfBedrooms = numberOfBedrooms;
        }
        if (numberOfBathrooms != null) {
            if (numberOfBathrooms < 0) {
                throw new IllegalArgumentException("Number of bathrooms cannot be negative");
            }
            this.numberOfBathrooms = numberOfBathrooms;
        }
        if (areaInSquareMeters != null) {
            if (areaInSquareMeters <= 0) {
                throw new IllegalArgumentException("Area must be greater than zero");
            }
            this.areaInSquareMeters = areaInSquareMeters;
        }
        if (petsAllowed != null) {
            this.petsAllowed = petsAllowed;
        }
        if (furnished != null) {
            this.furnished = furnished;
        }

        validatePricesByModality(
                salePrice != null ? salePrice : this.salePrice,
                rentPrice != null ? rentPrice : this.rentPrice,
                this.typeProperty
        );

        if (salePrice != null) {
            this.salePrice = salePrice;
        }
        if (rentPrice != null) {
            this.rentPrice = rentPrice;
        }

        this.updatedAt = LocalDateTime.now();
    }

    public void markAsRented() {
        if (this.status != PropertyStatus.PUBLISHED) {
            throw new IllegalStateException("Only published properties can be marked as rented");
        }
        this.status = PropertyStatus.RENTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsSold() {
        if (this.status != PropertyStatus.PUBLISHED) {
            throw new IllegalStateException("Only published properties can be marked as sold");
        }
        if (this.salePrice == null) {
            throw new IllegalStateException("Property without sale price cannot be marked as sold");
        }
        this.status = PropertyStatus.SOLD;
        this.updatedAt = LocalDateTime.now();
    }

    public void addImage(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Image URL cannot be null or empty");
        }
        this.imageUrls.add(imageUrl);
        this.updatedAt = LocalDateTime.now();
    }

    public void removeImage(String imageUrl) {
        this.imageUrls.remove(imageUrl);
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isOwnedBy(Long userId) {
        return this.ownerId.equals(userId);
    }

    public boolean isPublished() {
        return this.status == PropertyStatus.PUBLISHED;
    }

    public boolean isAvailableForRent() {
        return isPublished() && rentPrice != null;
    }

    public boolean isAvailableForSale() {
        return isPublished() && salePrice != null;
    }

    public PropertyId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public BigDecimal getRentPrice() {
        return rentPrice;
    }

    public TypeProperty getTypeProperty() {
        return typeProperty;
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
        return Collections.unmodifiableList(imageUrls);
    }

    public Integer getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public Integer getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public Integer getAreaInSquareMeters() {
        return areaInSquareMeters;
    }

    public boolean isPetsAllowed() {
        return petsAllowed;
    }

    public boolean isFurnished() {
        return furnished;
    }

    public Address getAddress() {
        return address;
    }

    public RentType getRentType() {
        return rentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property = (Property) o;
        return Objects.equals(id, property.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Property{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", typeProperty=" + typeProperty +
                ", status=" + status +
                ", ownerId=" + ownerId +
                ", rentType=" + rentType +
                '}';
    }
}