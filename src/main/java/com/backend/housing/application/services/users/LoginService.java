package com.backend.housing.application.services.users;

import com.backend.housing.application.dto.response.auth.AuthResponse;
import com.backend.housing.domain.entity.users.RefreshToken;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.users.LoginUseCase;
import com.backend.housing.domain.ports.out.users.RefreshTokenRepositoryPort;
import com.backend.housing.domain.ports.out.users.UserRepositoryPort;
import com.backend.housing.infrastructure.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoginService implements LoginUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;

    @Value("${app.jwt.refresh-token-expiration:604800000}")
    private long refreshTokenExpiration;

    public LoginService(UserRepositoryPort userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService,
                        RefreshTokenRepositoryPort refreshTokenRepositoryPort) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenRepositoryPort = refreshTokenRepositoryPort;
    }

    @Override
    public AuthResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("El usuario no existe o las credenciales son incorrectas"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("El usuario no existe o las credenciales son incorrectas");
        }

        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());

        String accessToken = jwtService.generateAccessToken(user.getEmail(), roles);
        String refreshTokenString = jwtService.generateRefreshToken();

        RefreshToken refreshToken = new RefreshToken(
                user.getId(),
                refreshTokenString,
                LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000)
        );
        refreshTokenRepositoryPort.save(refreshToken);

        return new AuthResponse(accessToken, refreshTokenString);
    }
}