package org.petproject.socialnetwork.Mapper;

import org.mapstruct.Mapper;
import org.petproject.socialnetwork.DTO.NotificationDTO;
import org.petproject.socialnetwork.Model.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
Notification toEntity(NotificationDTO notificationDTO);
NotificationDTO toDTO(Notification notification);
}
