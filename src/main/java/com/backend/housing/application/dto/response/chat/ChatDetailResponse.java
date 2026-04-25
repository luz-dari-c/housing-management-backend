package com.backend.housing.application.dto.response.chat;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ChatDetailResponse {
    private Long chatId;
    private Long otherUserId;
    private String otherUserName;
    private String otherUserProfilePicture;
    private Long propertyId;
    private String propertyTitle;
    private List<MessageResponse> messages;
}

@Getter
@Setter
class MessageResponse {
    private Long id;
    private Long senderId;
    private String content;
    private String status;
    private LocalDateTime sentAt;
    private LocalDateTime seenAt;
    private boolean isMine;
}