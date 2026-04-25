package com.backend.housing.infrastructure.persistence.repositories.chat;

import com.backend.housing.infrastructure.persistence.entities.chat.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageJpaRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findByChatIdOrderBySentAtAsc(Long chatId);

    @Query("SELECT m FROM MessageEntity m WHERE m.chat.id = :chatId AND m.sender.id != :userId AND m.status != 'SEEN'")
    List<MessageEntity> findUnreadByChatIdAndUserId(@Param("chatId") Long chatId, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE MessageEntity m SET m.status = 'SEEN', m.seenAt = CURRENT_TIMESTAMP WHERE m.chat.id = :chatId AND m.sender.id != :userId AND m.status != 'SEEN'")
    void markAsSeen(@Param("chatId") Long chatId, @Param("userId") Long userId);

    @Query("SELECT COUNT(m) FROM MessageEntity m JOIN m.chat c WHERE (c.tenant.id = :userId OR c.owner.id = :userId) AND m.sender.id != :userId AND m.status != 'SEEN'")
    Long countUnreadByUserId(@Param("userId") Long userId);

    Optional<MessageEntity> findFirstByChatIdOrderBySentAtDesc(Long chatId);
}