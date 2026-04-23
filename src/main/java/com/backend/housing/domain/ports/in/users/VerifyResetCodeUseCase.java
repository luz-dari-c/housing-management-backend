package com.backend.housing.domain.ports.in.users;

public interface VerifyResetCodeUseCase {
    boolean verifyCode(String email, String code);
}