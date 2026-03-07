package com.backend.housing.domain.ports.out;

import com.backend.housing.domain.entity.User;
import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findByEmail(String email);

}