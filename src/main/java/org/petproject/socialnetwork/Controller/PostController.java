package org.petproject.socialnetwork.Controller;

import org.petproject.socialnetwork.DTO.PostDTO;
import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Mapper.UserMapper;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Service.AuthenticationService;
import org.petproject.socialnetwork.Service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    public PostController(PostService postService, AuthenticationService authenticationService, UserMapper userMapper) {
        this.postService = postService;
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
    }

    @PostMapping("/createPost")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        User currentUser = authenticationService.getCurrentUser();
        PostDTO createdPost = postService.createPost(currentUser, postDTO);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/userPosts/{username}")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable String username) {
        UserDTO userDTO = authenticationService.findUserByUsername(username);
        List<PostDTO> posts = postService.getPostsByUser(userMapper.toEntity(userDTO));
        return ResponseEntity.ok(posts);
    }

    @DeleteMapping("/deletePost/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
