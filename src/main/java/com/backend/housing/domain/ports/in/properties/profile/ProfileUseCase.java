
package com.backend.housing.domain.ports.in.properties.profile;

import java.util.Optional;

import com.backend.housing.application.dto.auth.profile.UpdateProfileRequest;
import com.backend.housing.domain.entity.User;

public interface ProfileUseCase {
    Optional<User> getProfileByEmail(String email);
    User updateProfile(String email, UpdateProfileRequest updateRequest);
}