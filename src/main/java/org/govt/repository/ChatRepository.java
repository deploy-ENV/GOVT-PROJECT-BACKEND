package org.govt.repository;

import java.util.List;

import org.govt.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface ChatRepository extends MongoRepository<ChatMessage, String> {
    @Query(
  value = "{ $or: [ { senderId: ?0, receiverId: ?1 }, { senderId: ?1, receiverId: ?0 } ] }",
  sort = "{ timestamp: 1 }"
)
List<ChatMessage> findChatBetweenUsers(String user1, String user2);


    @Query("{ senderId: ?0, receiverId: ?1, status: 'SENT' }")
    List<ChatMessage> findUnreadMessages(String senderId, String receiverId);
}



