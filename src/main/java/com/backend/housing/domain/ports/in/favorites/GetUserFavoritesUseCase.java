package com.backend.housing.domain.ports.in.favorites;

import com.backend.housing.domain.entity.properties.Property;

import java.util.List;


public interface GetUserFavoritesUseCase
{
    List<Property> execute(Long userId);

}
