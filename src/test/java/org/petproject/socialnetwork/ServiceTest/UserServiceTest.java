package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Enums.FileCategory;
import org.petproject.socialnetwork.Exceptions.IllegalArgument;
import org.petproject.socialnetwork.Exceptions.UserNotFound;
import org.petproject.socialnetwork.Mapper.UserMapper;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.RoleRepository;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.petproject.socialnetwork.Service.EmailService;
import org.petproject.socialnetwork.Service.FileStorageService;
import org.petproject.socialnetwork.Service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private FileStorageService fileStorageService;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void changeAccountInfo_Success() throws IOException {
        User user = new User();
        user.setUsername("test");
        user.setBio("Old bio");
        String newBio = "New test bio";
        MultipartFile mockFile = mock(MultipartFile.class);

        when(fileStorageService.saveImage(mockFile, FileCategory.PROFILE_IMAGE)).thenReturn("imageUrl");
        when(userRepository.save(user)).thenReturn(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setBio(newBio);
        userDTO.setProfilePicture("imageUrl");

        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.changeAccountInfo(user, newBio, mockFile);

        assertNotNull(result);
        assertEquals(newBio, result.getBio());
        assertEquals("imageUrl", result.getProfilePicture());
    }

    @Test
    void changeAccountInfo_NullUser() {
        MultipartFile mockFile = mock(MultipartFile.class);
        assertThrows(UserNotFound.class, () -> userService.changeAccountInfo(null, "bio", mockFile));
        verifyNoInteractions(userRepository);
    }

    @Test
    void changeAccountInfo_EmptyBioAndPicture() throws IOException {
        User user = new User();
        user.setUsername("test");
        MultipartFile emptyFile = mock(MultipartFile.class);

        when(emptyFile.isEmpty()).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");

        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.changeAccountInfo(user, "", emptyFile);

        assertNotNull(result);
        verifyNoInteractions(fileStorageService);
        verify(userRepository, times(1)).save(user);
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
        assertThrows(IllegalArgument.class, () -> userService.changePassword(null, "newPassword"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_EmptyPassword() {
        User user = new User();
        user.setUsername("testUser");

        assertThrows(IllegalArgument.class, () -> userService.changePassword(user, ""));
        verify(userRepository, never()).save(any());
    }
    @Test
    void getAllUsers_NoSearchTerm() {
        User mockUser = new User();
        mockUser.setUsername("user1");
        when(userRepository.findAll()).thenReturn(Collections.singletonList(mockUser));

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("user1");
        when(userMapper.toDTO(mockUser)).thenReturn(userDTO);

        var result = userService.getAllUsers(null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user1", result.getFirst().getUsername());
        verify(userRepository, times(1)).findAll();
    }

}
