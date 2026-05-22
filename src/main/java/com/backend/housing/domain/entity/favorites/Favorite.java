package com.backend.housing.domain.entity.favorites;


import com.backend.housing.domain.entity.favorites.valueobjects.FavoriteId;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;

import java.time.LocalDateTime;
import java.util.Objects;

public class Favorite {

    private final FavoriteId id;
    private final Long userId;
    private final PropertyId propertyId;
    private final LocalDateTime createdAt;

    private Favorite(FavoriteId id, Long userId, PropertyId propertyId, LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id);
        this.userId = Objects.requireNonNull(userId);
        this.propertyId = Objects.requireNonNull(propertyId);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public static Favorite create(Long userId, PropertyId propertyId) {
        return new Favorite(
                FavoriteId.generate(),
                userId,
                propertyId,
                LocalDateTime.now()
        );
    }

    public static Favorite reconstitute(FavoriteId id, Long userId, PropertyId propertyId, LocalDateTime createdAt) {
        return new Favorite(id, userId, propertyId, createdAt);
    }

    public FavoriteId getId() { return id; }
    public Long getUserId() { return userId; }
    public PropertyId getPropertyId() { return propertyId; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Favorite that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}