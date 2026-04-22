package com.backend.housing.domain.ports.in.users;

public interface ResetPasswordUseCase {
    void resetPassword(String email, String code, String newPassword);
}