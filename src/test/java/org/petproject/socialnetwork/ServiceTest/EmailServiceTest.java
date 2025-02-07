package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.petproject.socialnetwork.Service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void sendVerificationEmail_Success() {
        String to = "user@example.com";
        String code = "123456";

        emailService.sendVerificationEmail(to, code);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertNotNull(sentMessage);
        assertArrayEquals(new String[]{to}, sentMessage.getTo());
        assertEquals("Email verification", sentMessage.getSubject());
        assertEquals("Your verification code is: " + code, sentMessage.getText());
        assertNull(sentMessage.getFrom());
        assertNull(sentMessage.getReplyTo());
    }

    @Test
    void sendEmailToAllUsers_Success() {
        String subject = "Subject for All Users";
        String messageText = "This is a message for all users.";

        User user1 = new User();
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setEmail("user2@example.com");

        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        emailService.sendEmailToAllUsers(subject, messageText);

        verify(userRepository, times(1)).findAll();
        verify(mailSender, times(users.size())).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_Success() {
        String to = "user@example.com";
        String subject = "Custom Subject";
        String messageText = "Custom message text.";

        emailService.sendEmail(to, subject, messageText);

        verify(mailSender, times(1)).send(argThat((SimpleMailMessage message) ->
                message.getTo()[0].equals(to) &&
                        message.getSubject().equals(subject) &&
                        message.getText().equals(messageText)
        ));
    }
}


