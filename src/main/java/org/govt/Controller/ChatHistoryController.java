package org.govt.Controller;

import java.security.Principal;
import java.util.List;

import org.govt.model.ChatMessage;
import org.govt.repository.ChatRepository;
import org.govt.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatHistoryController {

    @Autowired
    private ChatRepository chatRepository;
      @Autowired
    private ChatService chatService;

    @GetMapping("/{otherUserId}")
    public List<ChatMessage> getHistory(
            @PathVariable String otherUserId,
            Principal principal) {

        return chatRepository.findChatBetweenUsers(
                principal.getName(),
                otherUserId
        );
    }
     @PutMapping("/read/{senderId}")
    public ResponseEntity<String> markAsRead(
            @PathVariable String senderId,
            Principal principal) {

        String receiverId = principal.getName();

       
        chatService.markMessagesAsRead(senderId, receiverId);

        return ResponseEntity.ok("Messages marked as READ");
    }
}

