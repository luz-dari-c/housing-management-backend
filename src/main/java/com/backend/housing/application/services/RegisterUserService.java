package com.backend.housing.application.services;

import java.util.Set;

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

    public RegisterUserService(UserRepositoryPort userRepositoryPort, RoleRepositoryPort roleRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.roleRepositoryPort = roleRepositoryPort;
    }

    @Override
    public User register(User user) {
        userRepositoryPort.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email already exist");
        });

        Role rolTenant = roleRepositoryPort.findByName("Tenant").orElseThrow(() -> new RuntimeException("Role Tenant NOT FOUND"));

        user.setRoles(Set.of(rolTenant));

        return userRepositoryPort.save(user);
    }
}