package com.example.chat_app_backend.repository;

import com.example.chat_app_backend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRecipientId(Long recipientId);
    List<Message> findBySenderId(Long senderId);
    List<Message> findBySenderIdAndRecipientId(Long senderId, Long recipientId);

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.id = :userId1 AND m.recipient.id = :userId2) OR " +
            "(m.sender.id = :userId2 AND m.recipient.id = :userId1) " +
            "ORDER BY m.timestamp ASC")
    List<Message> findConversationBetweenUsers(@Param("userId1") Long userId1,
                                               @Param("userId2") Long userId2);

}

