package com.backend.housing.application.services.properties;

import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.exceptions.UserNotFoundException;
import com.backend.housing.domain.ports.in.properties.GetMyPropertiesUseCase;
import com.backend.housing.domain.ports.out.properties.PropertyRepository;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import com.backend.housing.domain.valueobjects.Pagination;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetMyPropertiesService  implements GetMyPropertiesUseCase {

    private final PropertyRepository propertyRepository;
    private final UserValidationPort userValidationPort;

    public GetMyPropertiesService(PropertyRepository propertyRepository, UserValidationPort userValidationPort) {
        this.propertyRepository = propertyRepository;
        this.userValidationPort = userValidationPort;
    }

    @Override
    public List<Property> getMyProperties(Pagination pagination, PropertyStatus status) {

        String email = getUserEmailFromToken();

        User user = userValidationPort.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        Long ownerId = user.getId();

        if (status != null) {
            return propertyRepository.findByOwnerIdAndPropertyStatus(ownerId, status, pagination);
        }

        return propertyRepository.findByOwnerId(ownerId, pagination);
    }

    private String getUserEmailFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        return authentication.getName();
    }
}
