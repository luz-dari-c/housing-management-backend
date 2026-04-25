package com.backend.housing.application.dto.response.chat;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ChatListResponse {
    private Long chatId;
    private Long otherUserId;
    private String otherUserName;
    private String otherUserProfilePicture;
    private Long propertyId;
    private String propertyTitle;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private boolean isLastMessageMine;
    private boolean isLastMessageSeen;
    private Long unreadCount;
}