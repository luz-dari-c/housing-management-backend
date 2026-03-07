package com.backend.housing.infrastructure.adapters.out.persistence;

import org.springframework.stereotype.Component;
import java.util.Optional;

import com.backend.housing.domain.entity.User;
import com.backend.housing.domain.ports.out.UserRepositoryPort;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final UserJpaRepository userJpaRepository;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save (User user){
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email){
        return userJpaRepository.findByEmail(email);
    }

}