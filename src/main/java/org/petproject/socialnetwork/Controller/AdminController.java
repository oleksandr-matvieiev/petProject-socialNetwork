package org.petproject.socialnetwork.Controller;

import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Service.AuthenticationService;
import org.petproject.socialnetwork.Service.EmailService;
import org.petproject.socialnetwork.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    public final EmailService emailService;
    public final UserService userService;
    public final AuthenticationService authenticationService;

    public AdminController(EmailService emailService, UserService userService, AuthenticationService authenticationService) {
        this.emailService = emailService;
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

}
