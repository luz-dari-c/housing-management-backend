
package com.backend.housing.infrastructure.adapters.in;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.housing.application.dto.admin.DashboardSummary;
import com.backend.housing.application.dto.admin.PaymentAdminResponse;
import com.backend.housing.application.dto.admin.PropertyAdminResponse;
import com.backend.housing.application.dto.admin.SystemMetricsResponse;
import com.backend.housing.application.dto.admin.UserAdminResponse;
import com.backend.housing.domain.entity.User;
import com.backend.housing.domain.ports.in.properties.admin.AdminUseCase;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminUseCase adminUseCase;

    public AdminController(AdminUseCase adminUseCase) {
        this.adminUseCase = adminUseCase;
    }

    
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardSummary> getDashboardSummary() {
        return ResponseEntity.ok(adminUseCase.getDashboardSummary());
    }

    @GetMapping("/metrics")
    public ResponseEntity<SystemMetricsResponse> getSystemMetrics() {
        return ResponseEntity.ok(adminUseCase.getSystemMetrics());
    }

    
    @GetMapping("/users")
    public ResponseEntity<List<UserAdminResponse>> getAllUsers() {
        return ResponseEntity.ok(adminUseCase.getAllUsers());
    }

    @GetMapping("/users/role/{roleName}")
    public ResponseEntity<List<UserAdminResponse>> getUsersByRole(@PathVariable String roleName) {
        return ResponseEntity.ok(adminUseCase.getUsersByRole(roleName));
    }

    @PatchMapping("/users/{userId}/toggle-status")
    public ResponseEntity<UserAdminResponse> toggleUserStatus(@PathVariable Long userId) {
        User updatedUser = adminUseCase.toggleUserStatus(userId);
        return ResponseEntity.ok(new UserAdminResponse(updatedUser));
    }

    
    @GetMapping("/properties")
    public ResponseEntity<List<PropertyAdminResponse>> getAllProperties() {
        return ResponseEntity.ok(adminUseCase.getAllProperties());
    }

    @GetMapping("/properties/pending")
    public ResponseEntity<List<PropertyAdminResponse>> getPendingProperties() {
        return ResponseEntity.ok(adminUseCase.getPendingProperties());
    }

    @PostMapping("/properties/{propertyId}/approve")
    public ResponseEntity<PropertyAdminResponse> approveProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok(adminUseCase.approveProperty(propertyId));
    }

    @PostMapping("/properties/{propertyId}/reject")
    public ResponseEntity<PropertyAdminResponse> rejectProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok(adminUseCase.rejectProperty(propertyId));
    }


    @GetMapping("/payments")
    public ResponseEntity<List<PaymentAdminResponse>> getAllPayments() {
        return ResponseEntity.ok(adminUseCase.getAllAdminPayments());
    }

    @GetMapping("/payments/total-income")
    public ResponseEntity<Double> getTotalIncome() {
        return ResponseEntity.ok(adminUseCase.getTotalAdminIncome());
    }
}
