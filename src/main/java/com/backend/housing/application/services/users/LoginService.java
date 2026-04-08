package com.backend.housing.application.services.users;


import com.backend.housing.domain.ports.in.users.LoginUseCase;
import com.backend.housing.domain.ports.out.users.UserRepositoryPort;
import com.backend.housing.infrastructure.security.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.backend.housing.domain.entity.users.User;
import com.backend.housing.infrastructure.security.JwtAuthenticationFilter;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginService implements LoginUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService  jwtService;

    public LoginService(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder,
                        JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Extrae los roles del usuario (maneja si no hay roles)
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());

        // Genera token incluyendo roles (puede ser lista vacía)
        return jwtService.generateToken(user.getEmail(), roles);
    }

}