
package com.backend.housing.domain.ports.in.properties.admin;

import java.util.List;

import com.backend.housing.application.dto.admin.DashboardSummary;
import com.backend.housing.application.dto.admin.PaymentAdminResponse;
import com.backend.housing.application.dto.admin.PropertyAdminResponse;
import com.backend.housing.application.dto.admin.SystemMetricsResponse;
import com.backend.housing.application.dto.admin.UserAdminResponse;
import com.backend.housing.domain.entity.User;

public interface AdminUseCase {
    
   
    SystemMetricsResponse getSystemMetrics();
    
    List<UserAdminResponse> getAllUsers();
    User toggleUserStatus(Long userId);
    List<UserAdminResponse> getUsersByRole(String roleName);
    
    List<PropertyAdminResponse> getPendingProperties();
    PropertyAdminResponse approveProperty(Long propertyId);
    PropertyAdminResponse rejectProperty(Long propertyId);
    List<PropertyAdminResponse> getAllProperties();
    
    
    List<PaymentAdminResponse> getAllAdminPayments();
    Double getTotalAdminIncome();

    DashboardSummary getDashboardSummary();
}