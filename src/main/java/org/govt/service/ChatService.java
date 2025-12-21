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

        message.setTimestamp(System.currentTimeMillis());
        message.setStatus("SENT");


        // Persist first (important)
        ChatMessage saved = chatRepository.save(message);

        // Send to receiver only
        messagingTemplate.convertAndSendToUser(
                message.getReceiverId(),
                "/queue/messages",
                saved
        );
    }
    public void markMessagesAsRead(String senderId, String receiverId) {

        List<ChatMessage> unreadMessages =
                chatRepository.findUnreadMessages(senderId, receiverId);

        for (ChatMessage message : unreadMessages) {
            message.setStatus("READ");
        }

        chatRepository.saveAll(unreadMessages);
    }
}

