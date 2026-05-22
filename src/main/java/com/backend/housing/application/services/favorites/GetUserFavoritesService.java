package com.backend.housing.application.services.favorites;

import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.ports.in.favorites.GetUserFavoritesUseCase;
import com.backend.housing.domain.ports.out.favorites.FavoriteRepository;
import com.backend.housing.domain.ports.out.properties.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetUserFavoritesService implements GetUserFavoritesUseCase {

    private final FavoriteRepository favoriteRepository;
    private final PropertyRepository propertyRepository;

    public GetUserFavoritesService(FavoriteRepository favoriteRepository,
                                   PropertyRepository propertyRepository) {
        this.favoriteRepository = favoriteRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Property> execute(Long userId) {

        return favoriteRepository.findByUserId(userId)
                .stream()
                .map(favorite -> propertyRepository.findById(favorite.getPropertyId())
                        .orElseThrow(() -> new IllegalStateException("Propiedad no encontrada: " + favorite.getPropertyId())))
                .collect(Collectors.toList());
    }
}