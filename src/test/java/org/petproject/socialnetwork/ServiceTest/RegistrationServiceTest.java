package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Exceptions.UserAlreadyExists;
import org.petproject.socialnetwork.Exceptions.UserWithEmailAlreadyExists;
import org.petproject.socialnetwork.Mapper.UserMapper;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.petproject.socialnetwork.Service.RegistrationService;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() {
        String username = "testUsername";
        String email = "test@example.com";
        String password = "testPass";

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(username);
        savedUser.setEmail(email);
        savedUser.setPassword("encodedPassword");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setEmail(email);

        when(userMapper.toDTO(savedUser)).thenReturn(userDTO);
        when(userMapper.toEntity(userDTO)).thenReturn(savedUser);

        UserDTO result = registrationService.registerUser(username, email, password);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());

        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    void registerUser_UsernameAlreadyExists() {
        String username = "existingUser";
        String email = "test@example.com";
        String password = "password";

        when(userRepository.existsByUsername(username)).thenReturn(true);

        assertThrows(UserAlreadyExists.class, () -> registrationService.registerUser(username, email, password));
    }

    @Test
    void registerUser_EmailAlreadyExists() {
        String username = "testUser";
        String email = "existing@example.com";
        String password = "password";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(UserWithEmailAlreadyExists.class, () -> registrationService.registerUser(username, email, password));
    }


}
