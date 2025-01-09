package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.DTO.NotificationDTO;
import org.petproject.socialnetwork.Enums.NotificationType;
import org.petproject.socialnetwork.Exceptions.NotificationNotFound;
import org.petproject.socialnetwork.Mapper.NotificationMapper;
import org.petproject.socialnetwork.Model.Notification;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.NotificationRepository;
import org.petproject.socialnetwork.Service.NotificationService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNotification_Success() {
        User recipient = new User();
        recipient.setId(1L);

        User triggerUser = new User();
        triggerUser.setId(2L);

        NotificationType type = NotificationType.MESSAGE;
        String message = "You have a new message!";

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setRecipient(recipient);
        notification.setTriggerUser(triggerUser);
        notification.setType(type);
        notification.setMessage(message);

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        notificationService.createNotification(recipient, triggerUser, type, message);

        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(notificationMapper, times(1)).toDTO(notification);
    }

    @Test
    void getNotifications_Success() {
        User recipient = new User();
        recipient.setId(1L);

        Notification notification1 = new Notification();
        notification1.setMessage("Notification 1");

        Notification notification2 = new Notification();
        notification2.setMessage("Notification 2");

        NotificationDTO notificationDTO1 = new NotificationDTO();
        notificationDTO1.setMessage("Notification 1");

        NotificationDTO notificationDTO2 = new NotificationDTO();
        notificationDTO2.setMessage("Notification 2");

        when(notificationRepository.findByRecipient(recipient)).thenReturn(Arrays.asList(notification1, notification2));
        when(notificationMapper.toDTO(notification1)).thenReturn(notificationDTO1);
        when(notificationMapper.toDTO(notification2)).thenReturn(notificationDTO2);

        List<NotificationDTO> result = notificationService.getNotifications(recipient);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Notification 2", result.get(0).getMessage());
        assertEquals("Notification 2", result.get(1).getMessage());
    }

    @Test
    void markAsRead_Success() {
        Long notificationId = 1L;

        Notification notification = new Notification();
        notification.setId(notificationId);
        notification.setRead(false);

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        notificationService.markAsRead(notificationId);

        assertTrue(notification.isRead());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void markAsRead_NotificationNotFound_ThrowsException() {
        Long notificationId = 1L;

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        assertThrows(NotificationNotFound.class, () -> notificationService.markAsRead(notificationId));
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void markAllAsRead_Success() {
        User recipient = new User();
        recipient.setId(1L);

        Notification notification1 = new Notification();
        notification1.setRead(false);

        Notification notification2 = new Notification();
        notification2.setRead(false);

        List<Notification> notifications = Arrays.asList(notification1, notification2);

        when(notificationRepository.findByRecipient(recipient)).thenReturn(notifications);

        notificationService.markAllAsRead(recipient);

        assertTrue(notification1.isRead());
        assertTrue(notification2.isRead());
        verify(notificationRepository, times(1)).saveAll(notifications);
    }
}
