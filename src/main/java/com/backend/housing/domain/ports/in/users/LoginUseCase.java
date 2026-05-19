package com.backend.housing.domain.ports.in.users;

import com.backend.housing.application.dto.response.auth.AuthResponse;

public interface LoginUseCase {
    AuthResponse login(String email, String password);
}