    package org.petproject.socialnetwork.Controller;

    import org.petproject.socialnetwork.DTO.MessageDTO;
    import org.petproject.socialnetwork.Service.AuthenticationService;
    import org.petproject.socialnetwork.Service.MessageService;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Map;

    @RestController
    @RequestMapping("/api/messages")
    public class MessageController {
        private final MessageService messageService;
        private final AuthenticationService authenticationService;

        public MessageController(MessageService messageService, AuthenticationService authenticationService) {
            this.messageService = messageService;
            this.authenticationService = authenticationService;
        }

        @PostMapping("/send")
        public ResponseEntity<MessageDTO> sendMessage(@RequestBody Map<String, String> requestBody) {
            String sender = authenticationService.getCurrentUser().getUsername();
            String receiver = requestBody.get("receiver");
            String content = requestBody.get("content");

            MessageDTO messageDTO = messageService.sendMessage(sender, receiver, content);
            return ResponseEntity.ok(messageDTO);
        }

        @GetMapping("/conversation/{receiver}")
        public ResponseEntity<List<MessageDTO>> getConversation(@PathVariable String receiver) {
            String senderUsername = authenticationService.getCurrentUser().getUsername();
            List<MessageDTO> messageDTOs = messageService.getConversation(senderUsername, receiver);
            return ResponseEntity.ok(messageDTOs);
        }
        @GetMapping("/inbox")
        public ResponseEntity<List<MessageDTO>> getInbox() {
            String username = authenticationService.getCurrentUser().getUsername();
            List<MessageDTO> inboxMessages = messageService.getInbox(username);
            return ResponseEntity.ok(inboxMessages);
        }
        @PostMapping("/{messageId}/read")
        public ResponseEntity<Void> markMessageAsRead(@PathVariable Long messageId) {
            messageService.markAsRead(messageId);
            return ResponseEntity.ok().build();
        }

    }
