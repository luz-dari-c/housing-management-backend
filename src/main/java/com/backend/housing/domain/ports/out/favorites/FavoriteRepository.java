package com.backend.housing.domain.ports.out.favorites;

import com.backend.housing.domain.entity.favorites.Favorite;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.favorites.valueobjects.FavoriteId;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository {

    Favorite save(Favorite favorite);

    void delete(FavoriteId id);

    Optional<Favorite> findByUserIdAndPropertyId(Long userId, PropertyId propertyId);

    List<Favorite> findByUserId(Long userId);

    boolean existsByUserIdAndPropertyId(Long userId, PropertyId propertyId);

    void deleteByUserIdAndPropertyId(Long userId, PropertyId propertyId);
}