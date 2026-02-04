package org.govt.service;

import java.util.List;

import org.govt.model.ChatMessage;
import org.govt.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void handleMessage(ChatMessage message) {

        try {
            // Validate message
            if (message.getSenderId() == null || message.getSenderId().isEmpty()) {
                System.err.println("ERROR: Message senderId is null or empty");
                return;
            }

            if (message.getReceiverId() == null || message.getReceiverId().isEmpty()) {
                System.err.println("ERROR: Message receiverId is null or empty");
                return;
            }

            if (message.getContent() == null || message.getContent().isEmpty()) {
                System.err.println("ERROR: Message content is null or empty");
                return;
            }

            message.setTimestamp(System.currentTimeMillis());
            message.setStatus("SENT");

            // Persist first (important)
            ChatMessage saved = chatRepository.save(message);

            System.out.println("✅ Message saved to database: " + saved.getId() +
                    " from " + saved.getSenderId() +
                    " to " + saved.getReceiverId());

            // Send to receiver only
            messagingTemplate.convertAndSendToUser(
                    message.getReceiverId(),
                    "/queue/messages",
                    saved);

            System.out.println("✅ Message sent via WebSocket to user: " + message.getReceiverId());

        } catch (Exception e) {
            System.err.println("❌ ERROR handling message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void markMessagesAsRead(String senderId, String receiverId) {

        try {
            List<ChatMessage> unreadMessages = chatRepository.findUnreadMessages(senderId, receiverId);

            System.out.println("Marking " + unreadMessages.size() + " messages as READ");

            for (ChatMessage message : unreadMessages) {
                message.setStatus("READ");
            }

            chatRepository.saveAll(unreadMessages);

            System.out.println("✅ Messages marked as READ successfully");

        } catch (Exception e) {
            System.err.println("❌ ERROR marking messages as read: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
