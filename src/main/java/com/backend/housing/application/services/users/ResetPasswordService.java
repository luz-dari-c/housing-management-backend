package com.backend.housing.application.services.users;

import org.springframework.security.crypto.password.PasswordEncoder;  
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.housing.domain.entity.users.PasswordResetToken;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.users.ResetPasswordUseCase;
import com.backend.housing.domain.ports.out.users.PasswordResetRepositoryPort;
import com.backend.housing.domain.ports.out.users.UserRepositoryPort;

@Service
@Transactional
public class ResetPasswordService implements ResetPasswordUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordResetRepositoryPort passwordResetRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public ResetPasswordService(UserRepositoryPort userRepositoryPort,
                                PasswordResetRepositoryPort passwordResetRepositoryPort,
                                PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordResetRepositoryPort = passwordResetRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void resetPassword(String email, String code, String newPassword) {
        
        PasswordResetToken token = passwordResetRepositoryPort.findValidToken(email, code)
                .orElseThrow(() -> new RuntimeException("Código inválido o expirado"));

        
        User user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

       
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepositoryPort.save(user);

       
        passwordResetRepositoryPort.markAsUsed(email, code);
    }
}