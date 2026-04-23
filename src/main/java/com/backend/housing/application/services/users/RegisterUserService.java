package com.backend.housing.application.services.users;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.housing.domain.entity.users.Rol;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.users.RegisterUserUseCase;
import com.backend.housing.domain.ports.out.users.RoleRepositoryPort;
import com.backend.housing.domain.ports.out.users.UserRepositoryPort;

@Service
@Transactional
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

        userRepositoryPort.findByCedula(user.getCedula()).ifPresent(u -> {
            throw new RuntimeException("La cédula ya está registrada");
        });

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Rol userRole = roleRepositoryPort.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Error: Rol USER no encontrado en la base de datos."));

        user.setRol(Set.of(userRole));
        user.setActive(true);

        return userRepositoryPort.save(user);
    }
}