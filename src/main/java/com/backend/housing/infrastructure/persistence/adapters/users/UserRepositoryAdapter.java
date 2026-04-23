package com.backend.housing.infrastructure.persistence.adapters.users;

import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.out.users.UserRepositoryPort;
import com.backend.housing.infrastructure.persistence.mappers.users.UserMapper;
import com.backend.housing.infrastructure.persistence.repositories.users.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        return UserMapper.toDomain(
                userJpaRepository.save(UserMapper.toEntity(user))
        );
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByCedula(String cedula) {
        return userJpaRepository.findByCedula(cedula)
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(UserMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll()
                .stream()
                .map(UserMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsById(Long id) {
        return userJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public List<User> findByActive(boolean active) {
        return userJpaRepository.findAll()
                .stream()
                .filter(e -> e.isActive() == active)
                .map(UserMapper::toDomain)
                .collect(Collectors.toList());
    }
}