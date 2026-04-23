package com.backend.housing.application.services.users;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.users.LoginUseCase;
import com.backend.housing.domain.ports.out.users.UserRepositoryPort;
import com.backend.housing.infrastructure.security.JwtService;

@Service
public class LoginService implements LoginUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginService(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("El usuario no existe o las credenciales son incorrectas"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("El usuario no existe o las credenciales son incorrectas");
        }

        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());

        return jwtService.generateToken(user.getEmail(), roles);
    }
}