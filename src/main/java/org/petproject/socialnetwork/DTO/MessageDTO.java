package org.petproject.socialnetwork.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Long id;
    private String content;
    private String senderUsername;
    private String receiverUsername;
    private Boolean isRead;
    private LocalDateTime timestamp;
}
