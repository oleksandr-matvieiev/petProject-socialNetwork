package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    public EmailService(JavaMailSender mailSender, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    public void sendVerificationEmail(String to, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Email verification");
            message.setText("Your verification code is:" + code);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendEmailToAllUsers(String subject, String messageText) {
        List<User> users = userRepository.findAll();
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            for (User user : users) {
                mailMessage.setTo(user.getEmail());
                mailMessage.setSubject(subject);
                mailMessage.setText(messageText);
                mailSender.send(mailMessage);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
