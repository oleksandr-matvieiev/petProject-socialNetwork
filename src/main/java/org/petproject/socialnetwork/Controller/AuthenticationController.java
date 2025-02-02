package org.petproject.socialnetwork.Controller;

import jakarta.validation.Valid;
import org.petproject.socialnetwork.DTO.LoginRequest;
import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(
            @Valid @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "bio", required = false) String bio,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {
        UserDTO registeredUser = authenticationService.registerUser(username, email, bio, password, image);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String code = requestBody.get("code");

        if (email == null || code == null) {
            return ResponseEntity.badRequest().body("Email and code are required.");
        }
        boolean isVerified = authenticationService.verifyEmail(email, code);
        if (isVerified) {
            return ResponseEntity.ok("Email verified successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authenticationService.login(loginRequest);
        User user = authenticationService.getCurrentUserDetails();
        List<String> roles = user.getRoles().stream()
                .map(role -> "ROLE_" + role.getName().name())
                .toList();
        return ResponseEntity.ok(Map.of("token", token, "roles", roles));
    }

    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser() {
        String username = authenticationService.getCurrentUser().getUsername();
        return ResponseEntity.ok("username: " + username);
    }
}
