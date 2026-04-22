
package com.backend.housing.infrastructure.persistence.repositories.users;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.housing.infrastructure.persistence.entities.users.PasswordResetTokenEntity;

public interface PasswordResetJpaRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

    Optional<PasswordResetTokenEntity> findByEmailAndCode(String email, String code);

    @Query("SELECT t FROM PasswordResetTokenEntity t WHERE t.email = :email AND t.code = :code AND t.used = false AND t.expiresAt > CURRENT_TIMESTAMP")
    Optional<PasswordResetTokenEntity> findValidToken(@Param("email") String email, @Param("code") String code);

    @Modifying
    @Query("UPDATE PasswordResetTokenEntity t SET t.used = true WHERE t.email = :email")
    void invalidatePreviousTokens(@Param("email") String email);

    @Modifying
    @Query("UPDATE PasswordResetTokenEntity t SET t.used = true WHERE t.email = :email AND t.code = :code")
    void markAsUsed(@Param("email") String email, @Param("code") String code);
    //si  lees esto, estás actualizado 22/04/2026
}