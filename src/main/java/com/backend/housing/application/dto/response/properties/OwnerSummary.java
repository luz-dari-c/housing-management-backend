package com.backend.housing.application.dto.response.properties;

public record OwnerSummary(
        Long id,
        String fullName,
        String email,
        String phoneNumber
) {}