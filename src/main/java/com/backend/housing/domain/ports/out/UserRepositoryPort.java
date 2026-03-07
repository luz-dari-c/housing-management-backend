package com.backend.housing.domain.ports.out;

import java.util.Optional;

import com.backend.housing.domain.entity.User;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findByEmail(String email);
}