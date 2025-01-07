package org.petproject.socialnetwork.Controller;

import org.petproject.socialnetwork.Service.AuthenticationService;
import org.petproject.socialnetwork.Service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    public final EmailService emailService;
    public final AuthenticationService authenticationService;

    public AdminController(EmailService emailService, AuthenticationService authenticationService) {
        this.emailService = emailService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/send-email-to-all")
    public ResponseEntity<Void> sendEmailToAllUsers(@RequestParam String subject, @RequestParam String message) {
        emailService.sendEmailToAllUsers(subject, message);
        return ResponseEntity.noContent().build();
    }
}
