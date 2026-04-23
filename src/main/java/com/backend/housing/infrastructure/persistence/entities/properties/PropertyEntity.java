package com.backend.housing.infrastructure.persistence.entities.properties;

import com.backend.housing.domain.entity.properties.enums.PaymentFrequency;
import com.backend.housing.domain.entity.properties.enums.TypeProperty;
import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "properties")
public class PropertyEntity {

    @Id
    private UUID propertyId;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Embedded
    private CoordinatesEmbeddable coordinates;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "rent_price")
    private BigDecimal rentPrice;

    @Column(name = "type_property", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeProperty typeProperty;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PropertyStatus propertyStatus;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @ElementCollection
    @CollectionTable(name = "property_images", joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "image_url")
    private List<String> imageUrls;

    @Column(name = "bedrooms")
    private Integer numberOfBedrooms;

    @Column(name = "bathrooms")
    private Integer numberOfBathrooms;

    @Column(name = "area_square_meters")
    private Integer areaInSquareMeters;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_frequency")
    private PaymentFrequency paymentFrequency;

    @Column(name = "pets_allowed")
    private Boolean petsAllowed;

    @Column(name = "furnished")
    private Boolean furnished;

    @Embedded
    private AddressEmbeddable address;
}