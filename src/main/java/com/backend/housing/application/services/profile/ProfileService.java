
package com.backend.housing.application.services.profile;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.housing.application.dto.auth.profile.UpdateProfileRequest;
import com.backend.housing.domain.entity.User;
import com.backend.housing.domain.ports.in.properties.profile.ProfileUseCase;
import com.backend.housing.domain.ports.out.UserRepositoryPort;

@Service
@Transactional
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

        
        if (request.getPrimerNombre() != null && !request.getPrimerNombre().isBlank()) {
            user.setPrimerNombre(request.getPrimerNombre());
        }
        if (request.getSegundoNombre() != null && !request.getSegundoNombre().isBlank()) {
            user.setSegundoNombre(request.getSegundoNombre());
        }
        if (request.getPrimerApellido() != null && !request.getPrimerApellido().isBlank()) {
            user.setPrimerApellido(request.getPrimerApellido());
        }
        if (request.getSegundoApellido() != null && !request.getSegundoApellido().isBlank()) {
            user.setSegundoApellido(request.getSegundoApellido());
        }
       
        if (request.getEmail() != null && !request.getEmail().equals(email)) {
            userRepositoryPort.findByEmail(request.getEmail()).ifPresent(u -> {
                throw new RuntimeException("El email " + request.getEmail() + " ya está en uso.");
            });
            user.setEmail(request.getEmail());
        }
        if (request.getCedula() != null && !request.getCedula().isBlank()) {
            user.setCedula(request.getCedula());
        }
        if (request.getEdad() > 0) {
            user.setEdad(request.getEdad());
        }

        return userRepositoryPort.save(user);
    }
}
