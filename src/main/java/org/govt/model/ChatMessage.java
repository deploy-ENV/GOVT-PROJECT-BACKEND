package org.govt.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    @Id
    private String id;

    private String senderId;
    private String receiverId;

    private String content;

    private long timestamp;
    private String status; // SENT, DELIVERED, READ
}
