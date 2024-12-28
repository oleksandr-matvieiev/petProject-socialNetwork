package org.petproject.socialnetwork.Controller;

import org.petproject.socialnetwork.DTO.PostDTO;
import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Mapper.UserMapper;
import org.petproject.socialnetwork.Service.PostService;
import org.petproject.socialnetwork.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    private final PostService postService;
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(PostService postService, UserService userService, UserMapper userMapper) {
        this.postService = postService;
        this.userService = userService;
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


}
