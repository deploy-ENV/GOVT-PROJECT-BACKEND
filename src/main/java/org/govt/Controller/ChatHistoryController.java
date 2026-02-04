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
    public ResponseEntity<?> getHistory(
            @PathVariable String otherUserId,
            Principal principal) {

        try {
            if (principal == null) {
                System.err.println("ERROR: No authenticated user for chat history request");
                return ResponseEntity.status(401).body("Unauthorized");
            }

            String currentUserId = principal.getName();
            System.out.println("Fetching chat history between " + currentUserId + " and " + otherUserId);

            List<ChatMessage> messages = chatRepository.findChatBetweenUsers(
                    currentUserId,
                    otherUserId);

            System.out.println("✅ Found " + messages.size() + " messages");
            return ResponseEntity.ok(messages);

        } catch (Exception e) {
            System.err.println("❌ ERROR fetching chat history: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching chat history");
        }
    }

    @PutMapping("/read/{senderId}")
    public ResponseEntity<String> markAsRead(
            @PathVariable String senderId,
            Principal principal) {

        try {
            if (principal == null) {
                System.err.println("ERROR: No authenticated user for mark as read request");
                return ResponseEntity.status(401).body("Unauthorized");
            }

            String receiverId = principal.getName();
            System.out.println("Marking messages from " + senderId + " to " + receiverId + " as READ");

            chatService.markMessagesAsRead(senderId, receiverId);

            return ResponseEntity.ok("Messages marked as READ");

        } catch (Exception e) {
            System.err.println("❌ ERROR marking messages as read: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error marking messages as read");
        }
    }
}
