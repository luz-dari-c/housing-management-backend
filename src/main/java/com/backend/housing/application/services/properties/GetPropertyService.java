package com.backend.housing.application.services.properties;

import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.exceptions.InvalidIdException;
import com.backend.housing.domain.exceptions.InvalidNotFoundException;
import com.backend.housing.domain.ports.in.properties.GetPropertyUseCase;
import com.backend.housing.domain.ports.out.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class GetPropertyService implements GetPropertyUseCase {

    private final PropertyRepository repository;


    public GetPropertyService(PropertyRepository repository){
        this.repository = repository;

    }

    @Override
    public Property getProperty(PropertyId id, UUID requestingUserId) {
        if (id ==  null){
            throw new InvalidIdException("Property ID cannot be null");
        }
        if (requestingUserId == null){
            throw new IllegalArgumentException("User Id cannot be null");
        }

        Property property = repository.findById(id).orElseThrow(()-> new InvalidNotFoundException("Property not found with id: " + id));

        return property;
    }
}
