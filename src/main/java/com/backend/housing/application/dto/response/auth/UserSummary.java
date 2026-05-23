package com.backend.housing.application.dto.response.auth;



public record UserSummary(
        Long id,
        String fullName,
        String email
) {}