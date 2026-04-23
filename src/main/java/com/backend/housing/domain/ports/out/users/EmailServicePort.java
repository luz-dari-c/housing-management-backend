package com.backend.housing.domain.ports.out.users;

public interface EmailServicePort {
    void sendPasswordResetCode(String toEmail, String code);
}