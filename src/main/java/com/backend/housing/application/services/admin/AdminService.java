// RUTA: src/main/java/com/backend/housing/application/services/admin/AdminService.java
package com.backend.housing.application.services.admin;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.housing.application.dto.admin.DashboardSummary;
import com.backend.housing.application.dto.admin.PaymentAdminResponse;
import com.backend.housing.application.dto.admin.PropertyAdminResponse;
import com.backend.housing.application.dto.admin.SystemMetricsResponse;
import com.backend.housing.application.dto.admin.UserAdminResponse;
import com.backend.housing.domain.entity.Role;
import com.backend.housing.domain.entity.User;
import com.backend.housing.domain.ports.in.properties.admin.AdminUseCase;
import com.backend.housing.domain.ports.out.UserRepositoryPort;
import com.backend.housing.domain.ports.out.external.ContractStatsPort;
import com.backend.housing.domain.ports.out.external.PaymentStatsPort;
import com.backend.housing.domain.ports.out.external.PropertyStatsPort;

@Service
@Transactional
public class AdminService implements AdminUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PropertyStatsPort propertyStatsPort;
    private final PaymentStatsPort paymentStatsPort;
    private final ContractStatsPort contractStatsPort;

    public AdminService(
            UserRepositoryPort userRepositoryPort,
            PropertyStatsPort propertyStatsPort,
            PaymentStatsPort paymentStatsPort,
            ContractStatsPort contractStatsPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.propertyStatsPort = propertyStatsPort;
        this.paymentStatsPort = paymentStatsPort;
        this.contractStatsPort = contractStatsPort;
    }

    @Override
    public SystemMetricsResponse getSystemMetrics() {
        SystemMetricsResponse metrics = new SystemMetricsResponse();

        // 1. Métricas de Usuarios
        List<User> allUsers = userRepositoryPort.findAll();
        metrics.setTotalUsers(allUsers.size());

        Map<String, Long> usersByRole = allUsers.stream()
                .flatMap(user -> user.getRoles().stream())
                .collect(Collectors.groupingBy(Role::getName, Collectors.counting()));
        metrics.setUsersByRole(usersByRole);

       
        metrics.setTotalProperties(propertyStatsPort.getTotalProperties());
        metrics.setRentedProperties(propertyStatsPort.getRentedProperties());
        metrics.setSoldProperties(propertyStatsPort.getSoldProperties());
        metrics.setPendingProperties(propertyStatsPort.getPendingProperties());
        metrics.setApprovedProperties(propertyStatsPort.getApprovedProperties());

      
        metrics.setTotalAdminIncome(paymentStatsPort.getTotalAdminIncome());
        metrics.setMonthlyAdminIncome(paymentStatsPort.getMonthlyAdminIncome());
        metrics.setTotalPayments(paymentStatsPort.getTotalPaymentsCount());

       
        metrics.setActiveContracts(contractStatsPort.getActiveContracts());

        return metrics;
    }

    @Override
    public List<UserAdminResponse> getAllUsers() {
        return userRepositoryPort.findAll().stream()
                .map(UserAdminResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public User toggleUserStatus(Long userId) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));
        user.setActive(!user.isActive());
        return userRepositoryPort.save(user);
    }

    @Override
    public List<UserAdminResponse> getUsersByRole(String roleName) {
        return userRepositoryPort.findAll().stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equalsIgnoreCase(roleName)))
                .map(UserAdminResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<PropertyAdminResponse> getPendingProperties() {
        // Cuando el módulo de propiedades esté listo, aquí se implementará la lógica real
        return List.of(); // Lista vacía por ahora
    }

    @Override
    public PropertyAdminResponse approveProperty(Long propertyId) {
        throw new UnsupportedOperationException("Funcionalidad no disponible: El módulo de propiedades aún no está integrado");
    }

    @Override
    public PropertyAdminResponse rejectProperty(Long propertyId) {
        throw new UnsupportedOperationException("Funcionalidad no disponible: El módulo de propiedades aún no está integrado");
    }

    @Override
    public List<PropertyAdminResponse> getAllProperties() {
        return List.of();
    }

    @Override
    public List<PaymentAdminResponse> getAllAdminPayments() {
        return List.of();
    }

    @Override
    public Double getTotalAdminIncome() {
        return paymentStatsPort.getTotalAdminIncome();
    }

    @Override
    public DashboardSummary getDashboardSummary() {
        return new DashboardSummary(
            userRepositoryPort.findAll().size(),
            propertyStatsPort.getTotalProperties(),
            propertyStatsPort.getPendingProperties(),
            contractStatsPort.getActiveContracts(),
            paymentStatsPort.getMonthlyAdminIncome(),
            paymentStatsPort.getTotalAdminIncome()
        );
    }
}