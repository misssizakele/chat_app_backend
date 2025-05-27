package com.example.chat_app_backend.controller;

import com.example.chat_app_backend.dto.MessageRequest;
import com.example.chat_app_backend.entity.Message;
import com.example.chat_app_backend.repository.MessageRepository;
import com.example.chat_app_backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    //This API sends a message from a sender to a recipient
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest request) {
        Message message = messageService.sendMessage(request.getSenderId(), request.getRecipientId(), request.getContent());
        return ResponseEntity.ok(message);
    }

//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Message>> getMessagesForUser(@PathVariable Long userId) {
//        return ResponseEntity.ok(messageService.getMessagesForUser(userId));
//    }

//    @GetMapping("/conversation")
//    public ResponseEntity<List<Message>> getConversation(@RequestParam Long user1Id, @RequestParam Long user2Id) {
//        return ResponseEntity.ok(messageService.getConversation(user1Id, user2Id));
//    }

    //This API gets the whole conversation between two users
    @GetMapping("/conversation/{userId1}/{userId2}")
    public ResponseEntity<List<Message>> getConversation(@PathVariable Long userId1,
                                                         @PathVariable Long userId2) {
        List<Message> messages = messageRepository.findConversationBetweenUsers(userId1, userId2);
        return ResponseEntity.ok(messages);
    }

}

