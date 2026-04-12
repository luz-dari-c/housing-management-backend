package com.backend.housing.application.services.users;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.backend.housing.application.dto.request.profile.UpdateProfileRequest;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.profile.ProfileUseCase;
import com.backend.housing.domain.ports.out.users.UserRepositoryPort;

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

        // Actualizar solo los campos permitidos (NO cédula)
        if (request.getPrimerNombre() != null) user.setPrimerNombre(request.getPrimerNombre());
        if (request.getSegundoNombre() != null) user.setSegundoNombre(request.getSegundoNombre());
        if (request.getPrimerApellido() != null) user.setPrimerApellido(request.getPrimerApellido());
        if (request.getSegundoApellido() != null) user.setSegundoApellido(request.getSegundoApellido());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getEdad() != null && request.getEdad() > 0) user.setEdad(request.getEdad());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber()); // NUEVO
        if (request.getProfilePictureUrl() != null) user.setProfilePictureUrl(request.getProfilePictureUrl()); // NUEVO

        return userRepositoryPort.save(user);
    }
}