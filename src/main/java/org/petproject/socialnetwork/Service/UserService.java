package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.Exceptions.UserNotFound;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User findUserByUsernameOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found.");
                    return new UserNotFound();
                });
    }

    public User findUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found.");
                    return new UserNotFound();
                });
    }

    public void changePassword(User user, String newPassword) {
        if (user == null || newPassword == null || newPassword.isBlank()) {
            logger.error("Try to change password with null input");
            throw new IllegalArgumentException("User or new password cannot be null/empty.");
        }
        user.setPassword(newPassword);
        logger.info("User: {} change password", user.getUsername());
        userRepository.save(user);
    }


}
