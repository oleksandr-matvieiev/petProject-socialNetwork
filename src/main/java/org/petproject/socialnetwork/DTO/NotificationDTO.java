package org.petproject.socialnetwork.DTO;

import lombok.Data;
import org.petproject.socialnetwork.Enums.NotificationType;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private String message;
    private LocalDateTime createdAt;
    private boolean read;
    private NotificationType type;
    private UserDTO triggerUser;

}
