package com.backend.housing.application.services.properties;


import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.exceptions.InvalidPaginationException;
import com.backend.housing.domain.ports.in.properties.ListPropertiesUseCase;
import com.backend.housing.domain.ports.out.PropertyRepository;
import com.backend.housing.domain.valueObjects.Pagination;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ListPropertiesService  implements ListPropertiesUseCase {

    private final PropertyRepository repository;

    public ListPropertiesService(PropertyRepository repository){
        this.repository = repository;
    }

    @Override
    public List<Property> listProperties(Pagination pagination) {
        if (pagination == null){
            throw new InvalidPaginationException("pagination cant be null");
        }
        return repository.findAll(pagination);
    }
}
