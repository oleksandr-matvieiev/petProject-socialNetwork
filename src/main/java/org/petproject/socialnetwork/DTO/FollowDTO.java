package org.petproject.socialnetwork.DTO;

import lombok.Data;

@Data
public class FollowDTO {
    private Long id;
    private UserDTO follower;
    private UserDTO followee;
}
