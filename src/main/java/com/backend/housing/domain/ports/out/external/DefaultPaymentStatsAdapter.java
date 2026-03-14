// RUTA: src/main/java/com/backend/housing/infrastructure/adapters/out/external/DefaultPaymentStatsAdapter.java
package com.backend.housing.domain.ports.out.external;

import org.springframework.stereotype.Component;

/**
 * Adaptador por defecto para estadísticas de pagos.
 * Esta implementación será reemplazada cuando el módulo de pagos esté listo.
 */
@Component
public class DefaultPaymentStatsAdapter implements PaymentStatsPort {
    
    @Override
    public double getTotalAdminIncome() {
        // TODO: Reemplazar con implementación real cuando el módulo de pagos esté listo
        return 12500.00; // Valor por defecto
    }
    
    @Override
    public double getMonthlyAdminIncome() {
        // TODO: Reemplazar con implementación real
        return 3450.00; // Valor por defecto
    }
    
    @Override
    public long getTotalPaymentsCount() {
        // TODO: Reemplazar con implementación real
        return 42; // Valor por defecto
    }
}