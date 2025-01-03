package org.petproject.socialnetwork.Controller;

import org.petproject.socialnetwork.DTO.PostDTO;
import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Mapper.UserMapper;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Service.AuthenticationService;
import org.petproject.socialnetwork.Service.PostService;
import org.petproject.socialnetwork.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    private final PostService postService;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    public UserController(PostService postService, UserService userService, AuthenticationService authenticationService, UserMapper userMapper) {
        this.postService = postService;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable String username) {
        UserDTO userDTO = userService.findUserByUsernameOrThrow(username);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<List<PostDTO>> getUserPosts(@PathVariable String username) {
        UserDTO userDTO = userService.findUserByUsernameOrThrow(username);
        List<PostDTO> list = postService.getPostsByUser(userMapper.toEntity(userDTO));
        return ResponseEntity.ok(list);
    }

    @PostMapping("/edit/profile-info")
    public ResponseEntity<UserDTO> editProfileInfo(
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) MultipartFile newPicture
    ) throws IOException {
        User user = authenticationService.getCurrentUser();
        UserDTO userDTO = userService.changeAccountInfo(user, bio, newPicture);
        return ResponseEntity.ok(userDTO);
    }


}
