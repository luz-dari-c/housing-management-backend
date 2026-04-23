package com.backend.housing.infrastructure.web.controllers.users;

import com.backend.housing.application.dto.request.users.ForgotPasswordRequest;
import com.backend.housing.application.dto.request.users.ResetPasswordRequest;
import com.backend.housing.application.dto.request.users.VerifyCodeRequest;
import com.backend.housing.domain.ports.in.users.ForgotPasswordUseCase;
import com.backend.housing.domain.ports.in.users.ResetPasswordUseCase;
import com.backend.housing.domain.ports.in.users.VerifyResetCodeUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final VerifyResetCodeUseCase verifyResetCodeUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    public PasswordResetController(ForgotPasswordUseCase forgotPasswordUseCase,
                                   VerifyResetCodeUseCase verifyResetCodeUseCase,
                                   ResetPasswordUseCase resetPasswordUseCase) {
        this.forgotPasswordUseCase = forgotPasswordUseCase;
        this.verifyResetCodeUseCase = verifyResetCodeUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        forgotPasswordUseCase.processForgotPassword(request.getEmail());
        return ResponseEntity.ok(Map.of(
                "message", "Si el correo existe, se enviará un código"
        ));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Map<String, Boolean>> verifyCode(@Valid @RequestBody VerifyCodeRequest request) {
        boolean valid = verifyResetCodeUseCase.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(Map.of("valid", valid));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        resetPasswordUseCase.resetPassword(request.getEmail(), request.getCode(), request.getNewPassword());
        return ResponseEntity.ok(Map.of(
                "message", "Contraseña actualizada correctamente"
        ));
    }
}