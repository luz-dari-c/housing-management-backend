package com.backend.housing.infrastructure.persistence.repositories;


import com.backend.housing.domain.entity.properties.Modality;
import com.backend.housing.domain.entity.properties.PropertyStatus;
import com.backend.housing.infrastructure.persistence.entities.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPropertyRepository  extends JpaRepository<PropertyEntity, Long> {


    List<PropertyEntity> findByPropertyStatus(PropertyStatus status);

    List<PropertyEntity> findByModality(Modality modality);

    List<PropertyEntity> findByOwnerId(Long id);

    List<PropertyEntity> findBySalePrice(BigDecimal salePrice);

    List<PropertyEntity> findByRentPrice(BigDecimal rentPrice);

    Long countByOwnerIdAndPropertyStatus(Long ownerId, PropertyStatus status);

    List<PropertyEntity> findBySalePriceBetween(BigDecimal min, BigDecimal max);

    List<PropertyEntity> findByRentPriceBetween(BigDecimal min, BigDecimal max);

    List<PropertyEntity> findByAddress_City(String city);


}
