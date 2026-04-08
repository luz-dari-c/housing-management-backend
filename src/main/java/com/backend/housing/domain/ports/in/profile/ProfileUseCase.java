package com.backend.housing.domain.ports.in.profile;


import java.util.Optional;

import com.backend.housing.application.dto.request.profile.UpdateProfileRequest;
import com.backend.housing.domain.entity.users.User;

public interface ProfileUseCase {
    Optional<User> getProfileByEmail(String email);
    User updateProfile(String email, UpdateProfileRequest updateRequest);
}