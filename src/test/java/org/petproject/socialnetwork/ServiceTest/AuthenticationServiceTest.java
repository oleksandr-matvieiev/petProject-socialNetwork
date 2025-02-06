package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.DTO.LoginRequest;
import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Enums.FileCategory;
import org.petproject.socialnetwork.Enums.RoleName;
import org.petproject.socialnetwork.Exceptions.UserAlreadyExists;
import org.petproject.socialnetwork.Exceptions.UserWithEmailAlreadyExists;
import org.petproject.socialnetwork.Mapper.UserMapper;
import org.petproject.socialnetwork.Model.Role;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.RoleRepository;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.petproject.socialnetwork.Security.JwtTokenProvider;
import org.petproject.socialnetwork.Service.AuthenticationService;
import org.petproject.socialnetwork.Service.EmailService;
import org.petproject.socialnetwork.Service.FileStorageService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private FileStorageService fileStorageService;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() throws IOException {
        String username = "testUser";
        String email = "test@example.com";
        String password = "password123";
        String bio = "Test bio";
        MultipartFile mockFile = mock(MultipartFile.class);

        Role role = new Role();
        role.setName(RoleName.USER);

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(roleRepository.findByName(RoleName.USER)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(fileStorageService.saveImage(mockFile, FileCategory.PROFILE_IMAGE)).thenReturn("imageUrl");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(username);
        savedUser.setEmail(email);
        savedUser.setPassword("encodedPassword");
        savedUser.setRoles(Set.of(role));

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setEmail(email);

        when(userMapper.toDTO(savedUser)).thenReturn(userDTO);

        UserDTO result = authenticationService.registerUser(username, email, bio, password, mockFile);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
        assertTrue(savedUser.getRoles().contains(role));

        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendVerificationEmail(eq(email), anyString());
    }


    @Test
    void registerUser_UsernameAlreadyExists() {
        String username = "testUser";
        String email = "test@example.com";
        String password = "password123";
        String bio = "Test bio";
        MultipartFile mockFile = mock(MultipartFile.class);

        when(userRepository.existsByUsername(username)).thenReturn(true);

        assertThrows(UserAlreadyExists.class, () -> authenticationService.registerUser(username, email, bio, password, mockFile));
    }

    @Test
    void registerUser_EmailAlreadyExists() {
        String username = "testUser";
        String email = "test@example.com";
        String password = "password123";
        String bio = "Test bio";
        MultipartFile mockFile = mock(MultipartFile.class);

        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(UserWithEmailAlreadyExists.class, () -> authenticationService.registerUser(username, email, bio, password, mockFile));
    }

    @Test
    void login_Success() {
        String username = "testUser";
        String password = "testPassword";
        String expectedToken = "testJwtToken";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        Authentication authenticationMock = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticationMock);

        User user = new User();
        user.setUsername(username);
        user.setEmailVerified(true);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(eq(username), any())).thenReturn(expectedToken);
        String token = authenticationService.login(loginRequest);

        assertEquals(expectedToken, token);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, times(1)).generateToken(eq(username),any());
    }

    @Test
    void login_EmailNotVerified() {
        String username = "testUser";
        String password = "testPassword";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        User user = new User();
        user.setUsername(username);
        user.setEmailVerified(false);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                authenticationService.login(loginRequest)
        );
        assertEquals("Email not verified", exception.getMessage());
        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    void login_InvalidCredentials() {
        String username = "invalidUser";
        String password = "wrongPassword";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authenticationService.login(loginRequest));
        assertEquals("Bad credentials", exception.getMessage());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtTokenProvider);
    }


}
