package com.backend.housing.infrastructure.persistence.repositories.favorites;

import com.backend.housing.infrastructure.persistence.entities.favorites.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaFavoriteRepository extends JpaRepository<FavoriteEntity, UUID> {

    Optional<FavoriteEntity> findByUserIdAndPropertyId(Long userId, UUID propertyId);

    List<FavoriteEntity> findByUserId(Long userId);

    boolean existsByUserIdAndPropertyId(Long userId, UUID propertyId);

    @Modifying
    @Query("DELETE FROM FavoriteEntity f WHERE f.userId = :userId AND f.propertyId = :propertyId")
    void deleteByUserIdAndPropertyId(@Param("userId") Long userId, @Param("propertyId") UUID propertyId);
}