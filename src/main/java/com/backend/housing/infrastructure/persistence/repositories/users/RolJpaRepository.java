package com.backend.housing.infrastructure.persistence.repositories.users;

import com.backend.housing.infrastructure.persistence.entities.users.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolJpaRepository extends JpaRepository<RolEntity, Long> {

    Optional<RolEntity> findByName(String name);
}