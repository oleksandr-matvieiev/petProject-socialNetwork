package org.petproject.socialnetwork.DTO;

import lombok.Data;

@Data
public class LikeDTO {
    private Long id;
    private Long postId;
    private UserDTO user;
}
