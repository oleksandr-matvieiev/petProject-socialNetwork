package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.DTO.MessageDTO;
import org.petproject.socialnetwork.Enums.NotificationType;
import org.petproject.socialnetwork.Exceptions.MessageNotFound;
import org.petproject.socialnetwork.Mapper.MessageMapper;
import org.petproject.socialnetwork.Model.Chat;
import org.petproject.socialnetwork.Model.Message;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.ChatRepository;
import org.petproject.socialnetwork.Repository.MessageRepository;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.petproject.socialnetwork.Service.MessageService;
import org.petproject.socialnetwork.Service.NotificationService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private ChatRepository chatRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMessage_Success() {
        String senderUsername = "senderUser";
        String receiverUsername = "receiverUser";
        String content = "Hello!";

        User sender = new User();
        sender.setUsername(senderUsername);

        User receiver = new User();
        receiver.setUsername(receiverUsername);

        Chat chat = new Chat();
        chat.setId(1L);

        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setChat(chat);
        message.setCreatedAt(LocalDateTime.now());

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent(content);

        when(userRepository.findByUsername(senderUsername)).thenReturn(Optional.of(sender));
        when(userRepository.findByUsername(receiverUsername)).thenReturn(Optional.of(receiver));
        when(chatRepository.findByUserOneAndUserTwo(sender, receiver)).thenReturn(Optional.empty());
        when(chatRepository.save(any(Chat.class))).thenReturn(chat);
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(messageMapper.toDTO(message)).thenReturn(messageDTO);

        MessageDTO result = messageService.sendMessage(senderUsername, receiverUsername, content);

        assertNotNull(result);
        assertEquals(content, result.getContent());
        verify(notificationService, times(1)).createNotification(receiver, sender, NotificationType.MESSAGE, "You have a new message from: senderUser!");
    }

    @Test
    void getConversation_Success() {
        String senderUsername = "senderUser";
        String receiverUsername = "receiverUser";

        User sender = new User();
        sender.setUsername(senderUsername);

        User receiver = new User();
        receiver.setUsername(receiverUsername);

        Message message1 = new Message();
        message1.setContent("Hi");
        message1.setCreatedAt(LocalDateTime.now().minusMinutes(2));

        MessageDTO messageDTO1=new MessageDTO();
        messageDTO1.setContent("Hi");

        Message message2 = new Message();
        message2.setContent("Hello");
        message2.setCreatedAt(LocalDateTime.now());

        MessageDTO messageDTO2=new MessageDTO();
        messageDTO1.setContent("Hello");

        List<Message> messages = Arrays.asList(message1, message2);

        when(userRepository.findByUsername(senderUsername)).thenReturn(Optional.of(sender));
        when(userRepository.findByUsername(receiverUsername)).thenReturn(Optional.of(receiver));
        when(messageRepository.findByParticipants(sender, receiver)).thenReturn(messages);

        when(messageMapper.toDTO(message1)).thenReturn(messageDTO1);
        when(messageMapper.toDTO(message2)).thenReturn(messageDTO2);

        List<MessageDTO> result = messageService.getConversation(receiverUsername,senderUsername);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Hello", result.getFirst().getContent());
    }

    @Test
    void getInbox_Success() {
        String username = "receiverUser";

        User receiver = new User();
        receiver.setUsername(username);

        Message message = new Message();
        message.setContent("Unread Message");

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("Unread Message");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(receiver));
        when(messageRepository.findByReceiverAndIsReadFalse(receiver)).thenReturn(List.of(message));
        when(messageMapper.toDTO(message)).thenReturn(messageDTO);

        List<MessageDTO> result = messageService.getInbox(username);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Unread Message", result.getFirst().getContent());
    }

    @Test
    void markAsRead_Success() {
        Long messageId = 1L;
        Message message = new Message();
        message.setId(messageId);
        message.setRead(false);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        messageService.markAsRead(messageId);

        assertTrue(message.isRead());
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    void markAsRead_MessageNotFound_ThrowsException() {
        Long messageId = 1L;

        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        assertThrows(MessageNotFound.class, () -> messageService.markAsRead(messageId));
        verify(messageRepository, never()).save(any());
    }
}
