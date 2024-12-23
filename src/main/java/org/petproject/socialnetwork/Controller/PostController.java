package org.petproject.socialnetwork.Controller;

import org.petproject.socialnetwork.DTO.LikeDTO;
import org.petproject.socialnetwork.DTO.PostDTO;
import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Mapper.UserMapper;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Service.AuthenticationService;
import org.petproject.socialnetwork.Service.LikeService;
import org.petproject.socialnetwork.Service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final LikeService likeService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    public PostController(PostService postService, LikeService likeService, AuthenticationService authenticationService, UserMapper userMapper) {
        this.postService = postService;
        this.likeService = likeService;
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
    }

    @PostMapping("/createPost")
    public ResponseEntity<PostDTO> createPost(
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {
        User currentUser = authenticationService.getCurrentUser();
        PostDTO createdPost = postService.createPost(currentUser, content, image);

        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/allPosts")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> allPosts = postService.getAllPosts();
        return ResponseEntity.ok(allPosts);
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

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long postId) {
        User currentUser = authenticationService.getCurrentUser();
        LikeDTO likeDTO = likeService.toggleLike(postId, currentUser.getId());
        return ResponseEntity.ok(likeDTO != null ? likeDTO : "Like removed");
    }


}
