package com.backend.housing.infrastructure.persistence.mappers.favorites;

import com.backend.housing.domain.entity.favorites.Favorite;
import com.backend.housing.domain.entity.favorites.valueobjects.FavoriteId;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.infrastructure.persistence.entities.favorites.FavoriteEntity;
import org.springframework.stereotype.Component;

@Component
public class FavoriteEntityMapper {

    public FavoriteEntity toEntity(Favorite favorite) {
        return new FavoriteEntity(
                favorite.getId().getValue(),
                favorite.getUserId(),
                favorite.getPropertyId().getValue(),
                favorite.getCreatedAt()
        );
    }

    public Favorite toDomain(FavoriteEntity entity) {
        return Favorite.reconstitute(
                FavoriteId.of(entity.getId()),
                entity.getUserId(),
                PropertyId.of(entity.getPropertyId()),
                entity.getCreatedAt()
        );
    }
}