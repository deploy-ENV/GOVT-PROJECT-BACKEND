package org.govt.Controller;

import java.security.Principal;

import org.govt.model.ChatMessage;
import org.govt.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.send")
    public void send(@Payload ChatMessage message, Principal principal) {

        // CRITICAL FIX: Set senderId from authenticated principal, not from client
        if (principal != null) {
            message.setSenderId(principal.getName());
            chatService.handleMessage(message);
        } else {
            System.err.println("ERROR: Unauthenticated WebSocket message received");
        }
    }

}
