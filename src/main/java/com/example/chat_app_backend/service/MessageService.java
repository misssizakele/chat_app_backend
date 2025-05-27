package com.example.chat_app_backend.service;

import com.example.chat_app_backend.entity.Message;
import com.example.chat_app_backend.entity.User;
import com.example.chat_app_backend.repository.MessageRepository;
import com.example.chat_app_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    public Message sendMessage(Long senderId, Long recipientId, String content) {
        User sender = userRepository.findById(senderId).orElseThrow();
        User recipient = userRepository.findById(recipientId).orElseThrow();

        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public List<Message> getMessagesForUser(Long userId) {
        return messageRepository.findByRecipientId(userId);
    }

    public List<Message> getConversation(Long user1Id, Long user2Id) {
        return messageRepository.findBySenderIdAndRecipientId(user1Id, user2Id);
    }
}

