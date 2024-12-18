package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.DTO.LoginRequest;
import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Exceptions.*;
import org.petproject.socialnetwork.Mapper.UserMapper;
import org.petproject.socialnetwork.Model.Role;
import org.petproject.socialnetwork.Model.RoleName;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.RoleRepository;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.petproject.socialnetwork.Security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserDTO registerUser(String username, String email, String password) {
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
        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(RoleNotFound::new);
        user.getRoles().add(userRole);
        return userMapper.toDTO(userRepository.save(user));
    }

    public String login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Додано: Витяг ролей користувача для токена
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(UserNotFound::new);
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

    public UserDTO findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFound::new);
        return userMapper.toDTO(user);
    }
}
