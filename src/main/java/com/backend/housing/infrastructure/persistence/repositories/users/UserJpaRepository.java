package com.backend.housing.infrastructure.persistence.repositories.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.housing.infrastructure.persistence.entities.users.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
    
    Optional<UserEntity> findByCedula(String cedula);
    
    boolean existsByEmail(String email);
}