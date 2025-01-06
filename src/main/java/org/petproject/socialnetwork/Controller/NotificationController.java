package org.petproject.socialnetwork.Controller;

import org.petproject.socialnetwork.DTO.NotificationDTO;
import org.petproject.socialnetwork.Service.NotificationService;
import org.petproject.socialnetwork.Service.AuthenticationService;
import org.petproject.socialnetwork.Model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final AuthenticationService authenticationService;

    public NotificationController(NotificationService notificationService, AuthenticationService authenticationService) {
        this.notificationService = notificationService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications() {
        User currentUser = authenticationService.getCurrentUser();
        List<NotificationDTO> notifications = notificationService.getNotifications(currentUser);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.noContent().build();
    }

}
