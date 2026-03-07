package com.backend.housing.application.services;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.housing.domain.entity.Role;
import com.backend.housing.domain.entity.User;
import com.backend.housing.domain.ports.in.properties.*;
import com.backend.housing.domain.ports.out.UserRepositoryPort;
import com.backend.housing.domain.ports.out.RoleRepositoryPort;


@Service
public class RegisterUserService implements RegisterUserUseCase {
    
    private final UserRepositoryPort userRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;
    private final PasswordEncoder passwordEncoder; // 1. Agrega esta línea

    // 2. Agrégalo aquí al constructor
    public RegisterUserService(UserRepositoryPort userRepositoryPort, 
                               RoleRepositoryPort roleRepositoryPort,
                               PasswordEncoder passwordEncoder) { 
        this.userRepositoryPort = userRepositoryPort;
        this.roleRepositoryPort = roleRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        userRepositoryPort.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email already exist");
        });

        // 3. ¡MUY IMPORTANTE! Encripta la contraseña antes de guardar
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Ojo: En tu base de datos (Neon) el rol dice "TENANT" (mayúsculas)
        Role rolTenant = roleRepositoryPort.findByName("TENANT")
            .orElseThrow(() -> new RuntimeException("Role TENANT NOT FOUND"));

        user.setRoles(Set.of(rolTenant));

        return userRepositoryPort.save(user);
    }

}