package org.petproject.socialnetwork.Controller;

import org.petproject.socialnetwork.DTO.ChatDTO;
import org.petproject.socialnetwork.Service.AuthenticationService;
import org.petproject.socialnetwork.Service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;
    private final AuthenticationService authenticationService;

    public ChatController(ChatService chatService, AuthenticationService authenticationService) {
        this.chatService = chatService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/create")
    public ResponseEntity<ChatDTO> createChat(@RequestParam String receiverUsername) {
        String currentUser = authenticationService.getCurrentUser().getUsername();
        return ResponseEntity.ok(chatService.createChat(currentUser, receiverUsername));
    }

    @GetMapping
    public ResponseEntity<List<ChatDTO>> getUserChats() {
        String currentUser = authenticationService.getCurrentUser().getUsername();
        return ResponseEntity.ok(chatService.getUserChats(currentUser));
    }
}
