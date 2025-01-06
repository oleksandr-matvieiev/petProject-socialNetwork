package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.DTO.NotificationDTO;
import org.petproject.socialnetwork.Enums.NotificationType;
import org.petproject.socialnetwork.Exceptions.NotificationNotFound;
import org.petproject.socialnetwork.Mapper.NotificationMapper;
import org.petproject.socialnetwork.Model.Notification;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    public NotificationDTO createNotification(User recipient, User triggerUser, NotificationType type, String message) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setTriggerUser(triggerUser);
        notification.setType(type);
        notification.setMessage(message);

        notification = notificationRepository.save(notification);
        return notificationMapper.toDTO(notification);
    }

    public List<NotificationDTO> getNotifications(User recipient) {
        return notificationRepository.findByRecipient(recipient).stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFound::new);
        notification.setRead(true);
        notificationRepository.save(notification);
    }

}