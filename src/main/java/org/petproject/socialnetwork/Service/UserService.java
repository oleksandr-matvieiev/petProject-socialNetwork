package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Exceptions.UserNotFound;
import org.petproject.socialnetwork.Mapper.UserMapper;
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
    private final UserMapper userMapper;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder encoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
    }

    public UserDTO findUserByUsernameOrThrow(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFound("User not found."));
        return userMapper.toDTO(user);
    }

    public UserDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFound("User not found."));
        return userMapper.toDTO(user);
    }

    public void changePassword(User user, String newPassword) {
        if (user == null || newPassword == null || newPassword.isBlank()) {
            logger.error("Try to change password with null input");
            throw new IllegalArgumentException("User or new password cannot be null/empty.");
        }
        user.setPassword(encoder.encode(newPassword));
        logger.info("User: {} change password", user.getUsername());
        userMapper.toDTO(userRepository.save(user));
    }


}
