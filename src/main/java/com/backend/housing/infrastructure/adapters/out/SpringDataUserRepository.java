package com.backend.housing.infrastructure.adapters.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.housing.domain.entity.User;

import java.util.Optional;

@Repository
public interface SpringDataUserRepository extends
 JpaRepository<User,Long>  {

    Optional <User> findByEmail(String email);
    
}

