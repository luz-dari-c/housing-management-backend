package com.backend.housing.infrastructure.web.controllers.favorites;

import com.backend.housing.application.dto.response.properties.PropertySummaryResponse;
import com.backend.housing.application.mapper.properties.PropertyResponseMapper;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.favorites.AddFavoriteUseCase;
import com.backend.housing.domain.ports.in.favorites.GetUserFavoritesUseCase;
import com.backend.housing.domain.ports.in.favorites.RemoveFavoriteUseCase;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Favorites", description = "Gestión de propiedades favoritas")
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final AddFavoriteUseCase addFavoriteUseCase;
    private final RemoveFavoriteUseCase removeFavoriteUseCase;
    private final GetUserFavoritesUseCase getUserFavoritesUseCase;
    private final PropertyResponseMapper propertyResponseMapper;
    private final UserValidationPort userValidationPort;

    public FavoriteController(AddFavoriteUseCase addFavoriteUseCase,
                              RemoveFavoriteUseCase removeFavoriteUseCase,
                              GetUserFavoritesUseCase getUserFavoritesUseCase,
                              PropertyResponseMapper propertyResponseMapper,
                              UserValidationPort userValidationPort) {
        this.addFavoriteUseCase = addFavoriteUseCase;
        this.removeFavoriteUseCase = removeFavoriteUseCase;
        this.getUserFavoritesUseCase = getUserFavoritesUseCase;
        this.propertyResponseMapper = propertyResponseMapper;
        this.userValidationPort = userValidationPort;
    }

    @Operation(summary = "Agregar una propiedad a favoritos")
    @PostMapping("/{propertyId}")
    public ResponseEntity<Void> addFavorite(@PathVariable UUID propertyId) {
        User currentUser = getAuthenticatedUser();
        addFavoriteUseCase.execute(currentUser.getId(), PropertyId.of(propertyId));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Quitar una propiedad de favoritos")
    @DeleteMapping("/{propertyId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable UUID propertyId) {
        User currentUser = getAuthenticatedUser();
        removeFavoriteUseCase.execute(currentUser.getId(), PropertyId.of(propertyId));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar mis propiedades favoritas")
    @GetMapping
    public ResponseEntity<List<PropertySummaryResponse>> getMyFavorites() {
        User currentUser = getAuthenticatedUser();
        List<Property> favorites = getUserFavoritesUseCase.execute(currentUser.getId());
        return ResponseEntity.ok(propertyResponseMapper.toSummaryResponseList(favorites));
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }
        String email = authentication.getName();
        return userValidationPort.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}