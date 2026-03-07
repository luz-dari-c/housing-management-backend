package com.backend.housing.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.backend.housing.domain.entity.User;

public interface UserJpaRepository extends JpaRepository<User,Long>{
    Optional<User> findByEmail (String email);
}