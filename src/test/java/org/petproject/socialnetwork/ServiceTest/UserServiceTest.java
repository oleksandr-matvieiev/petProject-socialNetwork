package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Exceptions.UserNotFound;
import org.petproject.socialnetwork.Mapper.UserMapper;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.petproject.socialnetwork.Service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findUserByUsernameOrThrow_UserExists() {
        String username = "testUser";
        User mockUser = new User();
        mockUser.setUsername(username);

        UserDTO mockUserDTO = new UserDTO();
        mockUserDTO.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(userMapper.toDTO(mockUser)).thenReturn(mockUserDTO);

        UserDTO result = userService.findUserByUsernameOrThrow(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
        verify(userMapper, times(1)).toDTO(mockUser);
    }

    @Test
    void findUserByUsernameOrThrow_UserNotFound() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> userService.findUserByUsernameOrThrow(username));
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void findUserByEmail_UserExists() {
        String email = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(email);

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(userMapper.toDTO(mockUser)).thenReturn(userDTO);

        UserDTO result = userService.findUserByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, times(1)).toDTO(mockUser);
    }

    @Test
    void findUserByEmail_UserNotFound() {
        String email = "nonExistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> userService.findUserByEmail(email));
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void changePassword_Success() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("oldPassword");
        String newPassword = "newPassword";
        String encodedPassword = "encodedPassword";

        when(encoder.encode(newPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        userService.changePassword(user, newPassword);

        assertEquals(encodedPassword, user.getPassword());
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDTO(user);
    }


    @Test
    void changePassword_NullUser() {
        assertThrows(IllegalArgumentException.class, () -> userService.changePassword(null, "newPassword"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_EmptyPassword() {
        User user = new User();
        user.setUsername("testUser");

        assertThrows(IllegalArgumentException.class, () -> userService.changePassword(user, ""));
        verify(userRepository, never()).save(any());
    }
}
