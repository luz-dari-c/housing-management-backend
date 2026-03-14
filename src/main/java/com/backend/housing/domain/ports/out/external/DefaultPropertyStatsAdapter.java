// RUTA: src/main/java/com/backend/housing/infrastructure/adapters/out/external/DefaultPropertyStatsAdapter.java
package com.backend.housing.domain.ports.out.external;

import org.springframework.stereotype.Component;

/**
 * Adaptador por defecto para estadísticas de propiedades.
 * Esta implementación será reemplazada cuando el módulo de propiedades esté listo.
 */
@Component
public class DefaultPropertyStatsAdapter implements PropertyStatsPort {
    
    @Override
    public long getTotalProperties() {
        // TODO: Reemplazar con implementación real cuando el módulo de propiedades esté listo
        return 25; // Valor por defecto
    }
    
    @Override
    public long getRentedProperties() {
        // TODO: Reemplazar con implementación real
        return 8; // Valor por defecto
    }
    
    @Override
    public long getSoldProperties() {
        // TODO: Reemplazar con implementación real
        return 5; // Valor por defecto
    }
    
    @Override
    public long getPendingProperties() {
        // TODO: Reemplazar con implementación real
        return 3; // Valor por defecto
    }
    
    @Override
    public long getApprovedProperties() {
        // TODO: Reemplazar con implementación real
        return 17; // Valor por defecto
    }
}