package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Enums.FileCategory;
import org.petproject.socialnetwork.Enums.RoleName;
import org.petproject.socialnetwork.Exceptions.IllegalArgument;
import org.petproject.socialnetwork.Exceptions.RoleNotFound;
import org.petproject.socialnetwork.Exceptions.UserNotFound;
import org.petproject.socialnetwork.Mapper.UserMapper;
import org.petproject.socialnetwork.Model.Role;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.RoleRepository;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final FileStorageService fileStorageService;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder encoder, UserMapper userMapper, RoleRepository roleRepository, EmailService emailService, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.fileStorageService = fileStorageService;
    }

    public UserDTO changeAccountInfo(User user, String bio, MultipartFile newProfilePicture) throws IOException {
        if (user == null) {
            throw new UserNotFound();
        }
        if (!bio.isEmpty()) {
            user.setBio(bio);
        }
        if (!newProfilePicture.isEmpty()) {
            String imageUrl = fileStorageService.saveImage(newProfilePicture, FileCategory.PROFILE_IMAGE);
            user.setProfilePicture(imageUrl);
        }
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public UserDTO findUserByUsernameOrThrow(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFound::new);
        return userMapper.toDTO(user);
    }

    public UserDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFound::new);
        return userMapper.toDTO(user);
    }

    public void changePassword(User user, String newPassword) {
        if (user == null || newPassword == null || newPassword.isBlank()) {
            logger.error("Try to change password with null input");
            throw new IllegalArgument("User or new password cannot be null/empty.");
        }
        user.setPassword(encoder.encode(newPassword));
        logger.info("User: {} change password", user.getUsername());
        userMapper.toDTO(userRepository.save(user));
    }

    public List<UserDTO> getAllUsers(String search) {
        if (search == null || search.isBlank()) {
            return userRepository.findAll().stream()
                    .map(userMapper::toDTO)
                    .collect(Collectors.toList());
        }
        return userRepository.findByUsernameContainingIgnoreCase(search).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteUserByUsername(String username) {
        User userToDelete = userRepository.findByUsername(username)
                .orElseThrow(UserNotFound::new);
        userRepository.delete(userToDelete);
    }

    public void sendEmailToUser(String userUsername, String subject, String message) {
        User user = userRepository.findByUsername(userUsername)
                .orElseThrow(UserNotFound::new);
        emailService.sendEmail(user.getEmail(), subject, message);
    }

    public User promoteToRole(String username, RoleName newRole) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFound::new);
        Role role = roleRepository.findByName(newRole)
                .orElseThrow(RoleNotFound::new);
        user.getRoles().add(role);
        userRepository.save(user);
        return user;
    }

    public User demoteFromRole(String username, RoleName roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));

        user.getRoles().remove(role);
        return userRepository.save(user);
    }

}
