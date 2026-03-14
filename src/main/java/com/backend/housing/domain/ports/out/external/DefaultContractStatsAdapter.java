// RUTA: src/main/java/com/backend/housing/infrastructure/adapters/out/external/DefaultContractStatsAdapter.java
package com.backend.housing.domain.ports.out.external;

import org.springframework.stereotype.Component;

/**
 * Adaptador por defecto para estadísticas de contratos.
 * Esta implementación será reemplazada cuando el módulo de contratos esté listo.
 */
@Component
public class DefaultContractStatsAdapter implements ContractStatsPort {
    
    @Override
    public long getActiveContracts() {
        // TODO: Reemplazar con implementación real cuando el módulo de contratos esté listo
        return 15; // Valor por defecto
    }
}