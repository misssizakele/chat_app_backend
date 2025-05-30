package com.example.chat_app_backend.controller;

import com.example.chat_app_backend.dto.MessageRequest;
import com.example.chat_app_backend.dto.MessageResponse;
import com.example.chat_app_backend.entity.Message;
import com.example.chat_app_backend.entity.User;
import com.example.chat_app_backend.repository.MessageRepository;
import com.example.chat_app_backend.repository.UserRepository;
import com.example.chat_app_backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

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
//    @GetMapping("/conversation/{userId1}/{userId2}")
//    public ResponseEntity<List<Message>> getConversation(@PathVariable Long userId1,
//                                                         @PathVariable Long userId2) {
//        List<Message> messages = messageRepository.findConversationBetweenUsers(userId1, userId2);
//        return ResponseEntity.ok(messages);
//    }

    //This API gets the whole conversation between two users
    @GetMapping("/conversation/{userId1}/{userId2}")
    public ResponseEntity<?> getConversation(@PathVariable Long userId1, @PathVariable Long userId2) {

        // Get current logged-in username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Get current user from database
        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Check if current user is part of the conversation
        if (!currentUser.getId().equals(userId1) && !currentUser.getId().equals(userId2)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this conversation.");
        }

        List<Message> messages = messageRepository.findConversationBetweenUsers(userId1, userId2);

        List<MessageResponse> response = messages.stream()
                .map(message -> new MessageResponse(
                        message.getSender().getUsername(),
                        message.getRecipient().getUsername(),
                        message.getContent()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

}

