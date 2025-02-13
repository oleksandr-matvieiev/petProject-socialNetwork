package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.DTO.LoginRequest;
import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Enums.FileCategory;
import org.petproject.socialnetwork.Enums.RoleName;
import org.petproject.socialnetwork.Exceptions.*;
import org.petproject.socialnetwork.Mapper.UserMapper;
import org.petproject.socialnetwork.Model.Role;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.RoleRepository;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.petproject.socialnetwork.Security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final FileStorageService fileStorageService;
    private final EmailService emailService;
    private final UserMapper userMapper;
    @Value("${app.file.default-avatar-photo}")
    private String DEFAULT_AVATAR_PHOTO;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, FileStorageService fileStorageService, EmailService emailService, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.fileStorageService = fileStorageService;
        this.emailService = emailService;
        this.userMapper = userMapper;
    }
    public User getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
    @Transactional
    public UserDTO registerUser(String username, String email, String bio, String password, MultipartFile image) throws IOException {
        if (username == null || username.isBlank()) {
            throw new IllegalArgument("Username cannot be blank.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgument("Invalid email.");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgument("Password must be at least 6 characters.");
        }

        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExists("Username is already taken.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserWithEmailAlreadyExists("Email is already registered.");
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setBio(bio);

        if (image != null && !image.isEmpty()) {
            String imageUrl = fileStorageService.saveImage(image, FileCategory.PROFILE_IMAGE);
            user.setProfilePicture(imageUrl);
        } else {
            user.setProfilePicture(DEFAULT_AVATAR_PHOTO);
        }

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(RoleNotFound::new);
        user.getRoles().add(userRole);

        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        user.setVerificationCode(verificationCode);

        emailService.sendVerificationEmail(email, verificationCode);

        return userMapper.toDTO(userRepository.save(user));
    }

    public boolean verifyEmail(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFound::new);
        if (user.getVerificationCode().equals(code)) {
            user.setEmailVerified(true);
            user.setVerificationCode(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }


    public String login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(UserNotFound::new);

        if (!user.isEmailVerified()) {
            throw new IllegalArgumentException("Email not verified");
        }

        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        return tokenProvider.generateToken(user.getUsername(), roles);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgument("No authenticated user found.");
        }
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new IllegalArgument("Unexpected authentication principal.");
        }
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFound::new);
    }

    public  User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFound::new);
    }

}
