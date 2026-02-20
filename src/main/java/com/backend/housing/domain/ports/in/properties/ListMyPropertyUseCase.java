package com.backend.housing.domain.ports.in.properties;


import com.backend.housing.domain.entity.properties.Property;

import java.util.List;
import java.util.UUID;

public interface ListMyPropertyUseCase {

    List<Property> listMyPublishedProperties(UUID myUserId);
    List<Property> listMySoldProperties (UUID myUserId);
    List<Property> listMyRentProperties(UUID myUserId);
    List<Property> listAllMyProperties(UUID myUserId);





}


