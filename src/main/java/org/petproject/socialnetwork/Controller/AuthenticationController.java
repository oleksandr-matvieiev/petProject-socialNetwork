package org.petproject.socialnetwork.Controller;

import jakarta.validation.Valid;
import org.petproject.socialnetwork.DTO.LoginRequest;
import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;


    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
            UserDTO registeredUser = authenticationService.registerUser(userDTO.getUsername(),
                    userDTO.getEmail(),
                    userDTO.getPassword());
                return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);


    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        String token= authenticationService.login(loginRequest);
        return ResponseEntity.ok(Map.of("token",token));
    }
//    @GetMapping("/me")
//    public ResponseEntity<String> getCurrentUser() {
//        String username = authenticationService.getCurrentUserUsername();
//        return ResponseEntity.ok("Current user: " + username);
//    }
}
