package com.backend.housing.infrastructure.persistence.repositories.chat;

import com.backend.housing.infrastructure.persistence.entities.chat.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatJpaRepository extends JpaRepository<ChatEntity, Long> {

    @Query("SELECT c FROM ChatEntity c WHERE c.tenant.id = :tenantId AND c.owner.id = :ownerId AND c.propertyId = :propertyId")
    Optional<ChatEntity> findByTenantAndOwnerAndProperty(@Param("tenantId") Long tenantId,
                                                          @Param("ownerId") Long ownerId,
                                                          @Param("propertyId") Long propertyId);

    @Query("SELECT c FROM ChatEntity c WHERE c.tenant.id = :userId OR c.owner.id = :userId")
    List<ChatEntity> findByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(c) > 0 FROM ChatEntity c WHERE c.id = :chatId AND (c.tenant.id = :userId OR c.owner.id = :userId)")
    boolean existsByIdAndUserId(@Param("chatId") Long chatId, @Param("userId") Long userId);
}