package org.petproject.socialnetwork.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String profilePicture;
    private String bio;
    private String password;
}
