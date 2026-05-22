package com.backend.housing.infrastructure.persistence.adapters.favorites;

import com.backend.housing.domain.entity.favorites.Favorite;
import com.backend.housing.domain.entity.favorites.valueobjects.FavoriteId;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.ports.out.favorites.FavoriteRepository;
import com.backend.housing.infrastructure.persistence.entities.favorites.FavoriteEntity;
import com.backend.housing.infrastructure.persistence.mappers.favorites.FavoriteEntityMapper;
import com.backend.housing.infrastructure.persistence.repositories.favorites.JpaFavoriteRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FavoriteRepositoryAdapter implements FavoriteRepository {

    private final JpaFavoriteRepository jpaFavoriteRepository;
    private final FavoriteEntityMapper mapper;

    public FavoriteRepositoryAdapter(JpaFavoriteRepository jpaFavoriteRepository,
                                     FavoriteEntityMapper mapper) {
        this.jpaFavoriteRepository = jpaFavoriteRepository;
        this.mapper = mapper;
    }

    @Override
    public Favorite save(Favorite favorite) {
        FavoriteEntity entity = mapper.toEntity(favorite);
        return mapper.toDomain(jpaFavoriteRepository.save(entity));
    }

    @Override
    public void delete(FavoriteId id) {
        jpaFavoriteRepository.deleteById(id.getValue());
    }

    @Override
    public Optional<Favorite> findByUserIdAndPropertyId(Long userId, PropertyId propertyId) {
        return jpaFavoriteRepository.findByUserIdAndPropertyId(userId, propertyId.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<Favorite> findByUserId(Long userId) {
        return jpaFavoriteRepository.findByUserId(userId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUserIdAndPropertyId(Long userId, PropertyId propertyId) {
        return jpaFavoriteRepository.existsByUserIdAndPropertyId(userId, propertyId.getValue());
    }

    @Override
    public void deleteByUserIdAndPropertyId(Long userId, PropertyId propertyId) {
        jpaFavoriteRepository.deleteByUserIdAndPropertyId(userId, propertyId.getValue());
    }
}