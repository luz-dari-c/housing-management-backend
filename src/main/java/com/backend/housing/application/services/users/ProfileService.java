// application/services/users/ProfileService.java
package com.backend.housing.application.services.users;

import com.backend.housing.application.dto.request.profile.UpdateProfileRequest;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.profile.ProfileUseCase;
import com.backend.housing.domain.ports.out.users.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService implements ProfileUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public ProfileService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Optional<User> getProfileByEmail(String email) {
        return userRepositoryPort.findByEmail(email);
    }

    @Override
    public User updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (request.getPrimerNombre() != null) user.setPrimerNombre(request.getPrimerNombre());
        if (request.getSegundoNombre() != null) user.setSegundoNombre(request.getSegundoNombre());
        if (request.getPrimerApellido() != null) user.setPrimerApellido(request.getPrimerApellido());
        if (request.getSegundoApellido() != null) user.setSegundoApellido(request.getSegundoApellido());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getCedula() != null) user.setCedula(request.getCedula());
        if (request.getEdad() > 0) user.setEdad(request.getEdad());

        return userRepositoryPort.save(user);
    }
}