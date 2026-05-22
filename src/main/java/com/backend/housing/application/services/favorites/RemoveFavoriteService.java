package com.backend.housing.application.services.favorites;

import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.ports.in.favorites.RemoveFavoriteUseCase;
import com.backend.housing.domain.ports.out.favorites.FavoriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RemoveFavoriteService implements RemoveFavoriteUseCase {

    private final FavoriteRepository favoriteRepository;

    public RemoveFavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    @Transactional
    public void execute(Long userId, PropertyId propertyId) {

        if (!favoriteRepository.existsByUserIdAndPropertyId(userId, propertyId)) {
            throw new IllegalArgumentException("La propiedad no está en tus favoritos");
        }

        favoriteRepository.deleteByUserIdAndPropertyId(userId, propertyId);
    }
}