package com.backend.housing.infrastructure.persistence.entities.favorites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_favorites")
public class FavoriteEntity {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "property_id", nullable = false)
    private UUID propertyId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public FavoriteEntity() {}

    public FavoriteEntity(UUID id, Long userId, UUID propertyId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.propertyId = propertyId;
        this.createdAt = createdAt;
    }


}