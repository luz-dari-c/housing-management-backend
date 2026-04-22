package com.backend.housing.domain.ports.in.users;

public interface ForgotPasswordUseCase {
    void processForgotPassword(String email);
}