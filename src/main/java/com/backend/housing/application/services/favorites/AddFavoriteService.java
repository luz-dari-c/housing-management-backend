package com.backend.housing.application.services.favorites;

import com.backend.housing.domain.entity.favorites.Favorite;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.ports.in.favorites.AddFavoriteUseCase;
import com.backend.housing.domain.ports.out.favorites.FavoriteRepository;
import com.backend.housing.domain.ports.out.properties.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddFavoriteService implements AddFavoriteUseCase {

    private final FavoriteRepository favoriteRepository;
    private final PropertyRepository propertyRepository;

    public AddFavoriteService(FavoriteRepository favoriteRepository,
                              PropertyRepository propertyRepository) {
        this.favoriteRepository = favoriteRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    @Transactional
    public void execute(Long userId, PropertyId propertyId) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("La propiedad no existe"));

        if (property.getOwnerId().equals(userId)) {
            throw new IllegalArgumentException("No puedes añadir tu propia propiedad a favoritos");
        }

        if (favoriteRepository.existsByUserIdAndPropertyId(userId, propertyId)) {
            throw new IllegalStateException("La propiedad ya está en tus favoritos");
        }

        Favorite favorite = Favorite.create(userId, propertyId);
        favoriteRepository.save(favorite);
    }
}