
package com.backend.housing.domain.ports.out.external;

import com.backend.housing.application.dto.admin.PropertyAdminResponse;
import com.backend.housing.application.dto.auth.*;

import java.util.List;
import java.util.Optional;

public interface PropertyAdminPort {
    List<PropertyAdminResponse> getAllProperties();
    List<PropertyAdminResponse> getPendingProperties();
    Optional<PropertyAdminResponse> getPropertyById(Long propertyId);
    PropertyAdminResponse approveProperty(Long propertyId);
    PropertyAdminResponse rejectProperty(Long propertyId);
    long getTotalProperties();
    long getPendingPropertiesCount();
    long getApprovedPropertiesCount();
    long getRentedPropertiesCount();
    long getSoldPropertiesCount();
}