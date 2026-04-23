package com.backend.housing.infrastructure.web.controllers.users;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.housing.application.dto.request.profile.UpdateProfileRequest;
import com.backend.housing.application.dto.response.profile.ProfileResponse;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.profile.ProfileUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileUseCase profileUseCase;

    public ProfileController(ProfileUseCase profileUseCase) {
        this.profileUseCase = profileUseCase;
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            
            return authentication.getName();
        }
        throw new RuntimeException("Usuario no autenticado");
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile() {
        String email = getCurrentUserEmail();
        User user = profileUseCase.getProfileByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(new ProfileResponse(user));
    }

    @PutMapping("/me")
    public ResponseEntity<ProfileResponse> updateMyProfile(@Valid @RequestBody UpdateProfileRequest request) {
        String email = getCurrentUserEmail();
        User updatedUser = profileUseCase.updateProfile(email, request);
        return ResponseEntity.ok(new ProfileResponse(updatedUser));
    }
}