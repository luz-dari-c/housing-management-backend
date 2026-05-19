package com.backend.housing.infrastructure.web.controllers.users;

import com.backend.housing.application.dto.request.users.LoginRequest;
import com.backend.housing.application.dto.request.users.RefreshTokenRequest;
import com.backend.housing.application.dto.request.users.RegisterRequest;
import com.backend.housing.application.dto.response.auth.AuthResponse;
import com.backend.housing.domain.entity.users.RefreshToken;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.users.LoginUseCase;
import com.backend.housing.domain.ports.in.users.RegisterUserUseCase;
import com.backend.housing.domain.ports.out.users.RefreshTokenRepositoryPort;
import com.backend.housing.infrastructure.security.JwtService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;
    private final JwtService jwtService;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;

    @Value("${app.default-profile-picture:https://ui-avatars.com/api/?name=John+Doe&background=0D8F81&color=fff&size=200}")
    private String defaultProfilePicture;

    public AuthController(LoginUseCase loginUseCase,
                          RegisterUserUseCase registerUserUseCase,
                          JwtService jwtService,
                          RefreshTokenRepositoryPort refreshTokenRepositoryPort) {
        this.loginUseCase = loginUseCase;
        this.registerUserUseCase = registerUserUseCase;
        this.jwtService = jwtService;
        this.refreshTokenRepositoryPort = refreshTokenRepositoryPort;
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterRequest request) {
        User user = new User();
        user.setPrimerNombre(request.getPrimerNombre());
        user.setSegundoNombre(request.getSegundoNombre());
        user.setPrimerApellido(request.getPrimerApellido());
        user.setSegundoApellido(request.getSegundoApellido());
        user.setEmail(request.getEmail());
        user.setCedula(request.getCedula());
        user.setEdad(request.getEdad());
        user.setPassword(request.getPassword());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setProfilePictureUrl(defaultProfilePicture);
        registerUserUseCase.register(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request) {
        return loginUseCase.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        RefreshToken storedToken = refreshTokenRepositoryPort.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token inválido o expirado"));

        if (!storedToken.isValid()) {
            throw new RuntimeException("Refresh token inválido o expirado");
        }

        refreshTokenRepositoryPort.revokeByToken(storedToken.getToken());

        String newAccessToken = jwtService.generateAccessToken(
                storedToken.getUserId().toString(),
                List.of("USER")
        );

        String newRefreshTokenString = jwtService.generateRefreshToken();
        RefreshToken newRefreshToken = new RefreshToken(
                storedToken.getUserId(),
                newRefreshTokenString,
                storedToken.getExpiresAt()
        );
        refreshTokenRepositoryPort.save(newRefreshToken);

        return ResponseEntity.ok(new AuthResponse(newAccessToken, newRefreshTokenString));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenRequest request) {
        refreshTokenRepositoryPort.findByToken(request.getRefreshToken())
                .ifPresent(token -> refreshTokenRepositoryPort.revokeByToken(token.getToken()));
        return ResponseEntity.ok().build();
    }
}