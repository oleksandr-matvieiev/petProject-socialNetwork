package org.petproject.socialnetwork.Controller;

import org.petproject.socialnetwork.DTO.PostDTO;
import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Enums.RoleName;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Service.AuthenticationService;
import org.petproject.socialnetwork.Service.EmailService;
import org.petproject.socialnetwork.Service.PostService;
import org.petproject.socialnetwork.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    public final EmailService emailService;
    private final PostService postService;
    public final UserService userService;
    public final AuthenticationService authenticationService;

    public AdminController(EmailService emailService, PostService postService, UserService userService, AuthenticationService authenticationService) {
        this.emailService = emailService;
        this.postService = postService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/send-email-to-all")
    public ResponseEntity<Void> sendEmailToAllUsers(@RequestParam String subject, @RequestParam String message) {
        emailService.sendEmailToAllUsers(subject, message);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestParam(value = "search", required = false) String search) {
        List<UserDTO> userDTOList = userService.getAllUsers(search);
        return ResponseEntity.ok(userDTOList);
    }

    @GetMapping("/actions/view-account-info/{username}")
    public ResponseEntity<UserDTO> getUserDetails(@PathVariable String username) {
        UserDTO user = userService.findUserByUsernameOrThrow(username);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/actions/delete-account/{username}")
    public ResponseEntity<String> deleteUserAccount(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok("User account deleted successfully.");
    }

    @PostMapping("/actions/send-email/{username}")
    public ResponseEntity<String> sendEmailToUser(@PathVariable String username, @RequestBody Map<String, String> requestBody) {
        String subject = requestBody.get("subject");
        String content = requestBody.get("content");
        userService.sendEmailToUser(username, subject, content);
        return ResponseEntity.ok("Email sent successfully.");
    }

    @PostMapping("/actions/promote/{username}")
    public ResponseEntity<User> promoteUser(@PathVariable String username, @RequestParam String roleName) {
        RoleName role = RoleName.valueOf(roleName);
        User updatedUser = userService.promoteToRole(username, role);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/actions/demote/{username}")
    public ResponseEntity<User> demoteUser(@PathVariable String username, @RequestParam String roleName) {
        RoleName role = RoleName.valueOf(roleName);
        User updatedUser = userService.demoteFromRole(username, role);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/actions/posts/{username}")
    public ResponseEntity<List<PostDTO>> getUserPosts(@PathVariable String username) {
        List<PostDTO> posts = postService.getPostsByUsername(username);
        return ResponseEntity.ok(posts);
    }

    @DeleteMapping("/actions/posts/{username}/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable String username, @PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

}
