package com.backend.housing.infrastructure.adapters.out;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.housing.domain.entity.User;

import java.util.Optional;


public interface SpringDataUserRepository extends
 JpaRepository<User,Long>  {

    Optional <User> findByEmail(String email);
    
}

