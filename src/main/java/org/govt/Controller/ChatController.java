package org.govt.Controller;

import java.security.Principal;

import org.govt.model.ChatMessage;
import org.govt.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.send")
    public void send(ChatMessage message, Principal principal) {

        // Enforce sender identity (CRITICAL)
        message.setSenderId(principal.getName());

        chatService.handleMessage(message);
    }
   
}

