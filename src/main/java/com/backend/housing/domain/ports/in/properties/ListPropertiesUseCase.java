package com.backend.housing.domain.ports.in.properties;


import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.valueObjects.Pagination;

import java.util.List;

public interface ListPropertiesUseCase  {

    List<Property> listProperties(Pagination pagination);

}
