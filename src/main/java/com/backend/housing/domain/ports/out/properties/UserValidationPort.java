package com.backend.housing.domain.ports.out.properties;

import com.backend.housing.domain.entity.users.User;

import java.util.Optional;

public interface UserValidationPort {

    boolean userExists(Long userId);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(Long userId);

    Optional<String> getUserName(Long userId);
}