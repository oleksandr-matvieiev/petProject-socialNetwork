package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.DTO.ChatDTO;
import org.petproject.socialnetwork.Model.Chat;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.ChatRepository;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.petproject.socialnetwork.Service.ChatService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ChatServiceTest {
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createChat_Success() {
        String currentUserUsername = "firstUser";
        String receiverUsername = "secondUser";

        User mockFirstUser = new User();
        mockFirstUser.setUsername(currentUserUsername);
        User mockSecondUser = new User();
        mockSecondUser.setUsername(receiverUsername);

        Chat savedChat = new Chat();
        savedChat.setUserOne(mockFirstUser);
        savedChat.setUserTwo(mockSecondUser);
        savedChat.setId(1L);

        when(userRepository.findByUsername(currentUserUsername)).thenReturn(Optional.of(mockFirstUser));
        when(userRepository.findByUsername(receiverUsername)).thenReturn(Optional.of(mockSecondUser));
        when(chatRepository.findByUserOneAndUserTwo(mockFirstUser, mockSecondUser)).thenReturn(Optional.empty());
        when(chatRepository.save(any(Chat.class))).thenReturn(savedChat);

        ChatDTO result = chatService.createChat(currentUserUsername, receiverUsername);

        assertNotNull(result);
        Assertions.assertEquals(currentUserUsername, result.getUserOne());
        Assertions.assertEquals(receiverUsername, result.getUserTwo());
        Assertions.assertEquals(1L, result.getId());

        verify(userRepository, times(2)).findByUsername(anyString());
        verify(chatRepository, times(2)).findByUserOneAndUserTwo(mockFirstUser, mockSecondUser);
        verify(chatRepository).save(any(Chat.class));
    }

    @Test
    void createChat_ExistingChat() {
        String currentUserUsername = "firstUser";
        String receiverUsername = "secondUser";

        User mockFirstUser = new User();
        mockFirstUser.setUsername(currentUserUsername);
        User mockSecondUser = new User();
        mockSecondUser.setUsername(receiverUsername);

        Chat existingChat = new Chat();
        existingChat.setUserOne(mockFirstUser);
        existingChat.setUserTwo(mockSecondUser);

        when(userRepository.findByUsername(currentUserUsername)).thenReturn(Optional.of(mockFirstUser));
        when(userRepository.findByUsername(receiverUsername)).thenReturn(Optional.of(mockSecondUser));
        when(chatRepository.findByUserOneAndUserTwo(mockFirstUser, mockSecondUser)).thenReturn(Optional.of(existingChat));

        ChatDTO result = chatService.createChat(currentUserUsername, receiverUsername);

        assertNotNull(result);
        assertEquals(currentUserUsername, result.getUserOne());
        assertEquals(receiverUsername, result.getUserTwo());

        verify(userRepository, times(2)).findByUsername(anyString());
        verify(chatRepository).findByUserOneAndUserTwo(mockFirstUser, mockSecondUser);
        verify(chatRepository, never()).save(any(Chat.class));
    }
}


