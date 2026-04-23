package com.backend.housing.application.services.users;

import com.backend.housing.domain.entity.users.PasswordResetToken;
import com.backend.housing.domain.ports.in.users.ForgotPasswordUseCase;
import com.backend.housing.domain.ports.out.users.EmailServicePort;
import com.backend.housing.domain.ports.out.users.PasswordResetRepositoryPort;
import com.backend.housing.domain.ports.out.users.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@Transactional
public class ForgotPasswordService implements ForgotPasswordUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordResetRepositoryPort passwordResetRepositoryPort;
    private final EmailServicePort emailServicePort;

    public ForgotPasswordService(UserRepositoryPort userRepositoryPort,
                                 PasswordResetRepositoryPort passwordResetRepositoryPort,
                                 EmailServicePort emailServicePort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordResetRepositoryPort = passwordResetRepositoryPort;
        this.emailServicePort = emailServicePort;
    }

    @Override
    public void processForgotPassword(String email) {
        boolean emailExists = userRepositoryPort.existsByEmail(email);

        if (emailExists) {
            passwordResetRepositoryPort.invalidatePreviousTokens(email);

            String code = generateVerificationCode();

            PasswordResetToken token = new PasswordResetToken(
                    email,
                    code,
                    LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5)
            );

            passwordResetRepositoryPort.save(token);
            emailServicePort.sendPasswordResetCode(email, code);
        }
    }

    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}