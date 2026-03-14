

package com.backend.housing.application.services.auth;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.housing.domain.entity.Role;
import com.backend.housing.domain.entity.User;
import com.backend.housing.domain.ports.in.properties.RegisterUserUseCase;
import com.backend.housing.domain.ports.out.RoleRepositoryPort;
import com.backend.housing.domain.ports.out.UserRepositoryPort;

@Service
public class RegisterUserService implements RegisterUserUseCase {
    
    private final UserRepositoryPort userRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;
    private final PasswordEncoder passwordEncoder;

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
            throw new RuntimeException("El email ya está registrado");
        });

       
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role rolTenant = roleRepositoryPort.findByName("TENANT")
            .orElseThrow(() -> new RuntimeException("Error de configuración: Rol TENANT no encontrado en la base de datos."));

        user.setRoles(Set.of(rolTenant));
       
        user.setActive(true);

        return userRepositoryPort.save(user);
    }

}