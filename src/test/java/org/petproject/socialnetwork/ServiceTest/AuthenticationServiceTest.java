package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.DTO.LoginRequest;
import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Exceptions.UserAlreadyExists;
import org.petproject.socialnetwork.Exceptions.UserWithEmailAlreadyExists;
import org.petproject.socialnetwork.Mapper.UserMapper;
import org.petproject.socialnetwork.Model.Role;
import org.petproject.socialnetwork.Model.RoleName;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.RoleRepository;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.petproject.socialnetwork.Security.JwtTokenProvider;
import org.petproject.socialnetwork.Service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @InjectMocks
    private AuthenticationService registrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() {
        String username = "testUsername";
        String email = "test@example.com";
        String password = "testPass";

        Role role = new Role();
        role.setName(RoleName.ROLE_USER);

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

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

        UserDTO result = registrationService.registerUser(username, email, password);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
        assertTrue(savedUser.getRoles().contains(role));

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

        when(jwtTokenProvider.generateToken(username)).thenReturn(expectedToken);
        String token = registrationService.login(loginRequest);

        assertEquals(expectedToken, token);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, times(1)).generateToken(username);
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

        RuntimeException exception = assertThrows(RuntimeException.class, () -> registrationService.login(loginRequest));
        assertEquals("Bad credentials", exception.getMessage());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtTokenProvider);
    }


}
