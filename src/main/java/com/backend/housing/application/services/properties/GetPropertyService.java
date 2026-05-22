package com.backend.housing.application.services.properties;
import com.backend.housing.application.dto.response.properties.PropertyDetailResponse;
import com.backend.housing.application.mapper.properties.PropertyResponseMapper;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.exceptions.InvalidIdException;
import com.backend.housing.domain.exceptions.InvalidNotFoundException;
import com.backend.housing.domain.ports.in.properties.GetPropertyUseCase;
import com.backend.housing.domain.ports.out.properties.PropertyRepository;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetPropertyService implements GetPropertyUseCase {

    private final PropertyRepository repository;
    private final UserValidationPort userValidationPort;
    private final PropertyResponseMapper propertyResponseMapper;

    public GetPropertyService(
            PropertyRepository repository,
            UserValidationPort userValidationPort,
            PropertyResponseMapper propertyResponseMapper
    ) {
        this.repository = repository;
        this.userValidationPort = userValidationPort;
        this.propertyResponseMapper = propertyResponseMapper;
    }

    @Override
    public Property getProperty(PropertyId id) {
        if (id == null) {
            throw new InvalidIdException("Property ID cannot be null");
        }
        return repository.findById(id)
                .orElseThrow(() -> new InvalidNotFoundException("Property not found with id: " + id));
    }

    @Override
    public PropertyDetailResponse getPropertyDetail(PropertyId id) {
        Property property = getProperty(id);

        User owner = userValidationPort.findByUserId(property.getOwnerId())
                .orElseThrow(() -> new InvalidNotFoundException("Owner not found with id: " + property.getOwnerId()));

        return propertyResponseMapper.toDetailResponse(property, owner);
    }
}