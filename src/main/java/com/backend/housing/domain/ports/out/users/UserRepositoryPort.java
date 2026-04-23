package com.backend.housing.domain.ports.out.users;

import com.backend.housing.domain.entity.users.User;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findByCedula(String cedula);
    Optional<User> findById(Long id);
    List<User> findAll();
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    void deleteById(Long id);
    List<User> findByActive(boolean active);
}