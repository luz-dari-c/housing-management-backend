package com.backend.housing.infrastructure.persistence.adapters.users;

import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import com.backend.housing.domain.ports.out.users.UserRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserValidationAdapter implements UserValidationPort {

    private final UserRepositoryPort userRepositoryPort;

    public UserValidationAdapter(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public boolean userExists(Long userId) {
        return userRepositoryPort.existsById(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepositoryPort.findByEmail(email);
    }

    @Override
    public Optional<String> getUserName(Long userId) {
        return userRepositoryPort.findById(userId)
                .map(this::buildFullName);
    }

    private String buildFullName(User user) {
        StringBuilder fullName = new StringBuilder();

        if (user.getPrimerNombre() != null) {
            fullName.append(user.getPrimerNombre()).append(" ");
        }

        if (user.getSegundoNombre() != null) {
            fullName.append(user.getSegundoNombre()).append(" ");
        }

        if (user.getPrimerApellido() != null) {
            fullName.append(user.getPrimerApellido()).append(" ");
        }

        if (user.getSegundoApellido() != null) {
            fullName.append(user.getSegundoApellido());
        }

        return fullName.toString().trim();
    }
}