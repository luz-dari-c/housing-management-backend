package com.backend.housing.application.services.users;

import com.backend.housing.domain.entity.users.*;
import com.backend.housing.domain.ports.in.users.*;
import com.backend.housing.domain.ports.out.users.*;
import org.springframework.stereotype.Service;

@Service
public class VerifyResetCodeService implements VerifyResetCodeUseCase {

    private final PasswordResetRepositoryPort passwordResetRepositoryPort;

    public VerifyResetCodeService(PasswordResetRepositoryPort passwordResetRepositoryPort) {
        this.passwordResetRepositoryPort = passwordResetRepositoryPort;
    }

    @Override
    public boolean verifyCode(String email, String code) {
        return passwordResetRepositoryPort.findValidToken(email, code).isPresent();
    }
}
