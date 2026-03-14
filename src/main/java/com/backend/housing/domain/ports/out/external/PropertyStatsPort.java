
package com.backend.housing.domain.ports.out.external;

public interface PropertyStatsPort {
    long getTotalProperties();
    long getRentedProperties();
    long getSoldProperties();
    long getPendingProperties();
    long getApprovedProperties();
}