package com.backend.housing.infrastructure.web.controllers.chat;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.housing.application.dto.request.chat.StartChatRequest;
import com.backend.housing.application.dto.response.chat.ChatDetailResponse;
import com.backend.housing.application.dto.response.chat.ChatListResponse;
import com.backend.housing.domain.entity.chat.Chat;
import com.backend.housing.domain.ports.in.chat.ChatUseCase;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatUseCase chatUseCase;

    public ChatController(ChatUseCase chatUseCase) {
        this.chatUseCase = chatUseCase;
    }

    private Long getCurrentUserId(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }

    @PostMapping("/start")
    public ResponseEntity<ChatDetailResponse> startChat(@RequestBody StartChatRequest request,
                                                         Authentication authentication) {
        Long tenantId = getCurrentUserId(authentication);
        Chat chat = chatUseCase.startChat(tenantId, request.getOwnerId(), request.getPropertyId());
        return ResponseEntity.ok(mapToDetailResponse(chat, tenantId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<ChatListResponse>> getChatList(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        List<Chat> chats = chatUseCase.getUserChats(userId);
        List<ChatListResponse> response = chats.stream()
                .map(chat -> mapToListResponse(chat, userId))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDetailResponse> getChatDetail(@PathVariable Long chatId,
                                                             Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        Chat chat = chatUseCase.getChatDetail(chatId, userId);
        return ResponseEntity.ok(mapToDetailResponse(chat, userId));
    }

    private ChatListResponse mapToListResponse(Chat chat, Long userId) {
        ChatListResponse response = new ChatListResponse();
        response.setChatId(chat.getId());
        response.setPropertyId(chat.getPropertyId());
        com.backend.housing.domain.entity.chat.Message lastMessage = chat.getLastMessage();
        if (lastMessage != null) {
            response.setLastMessage(lastMessage.getContent());
            response.setLastMessageTime(lastMessage.getSentAt());
        }
        return response;
    }

    private ChatDetailResponse mapToDetailResponse(Chat chat, Long userId) {
        ChatDetailResponse response = new ChatDetailResponse();
        response.setChatId(chat.getId());
        response.setPropertyId(chat.getPropertyId());
        return response;
    }
}