package org.petproject.socialnetwork.Repository;

import org.petproject.socialnetwork.Model.Notification;
import org.petproject.socialnetwork.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipient(User recipient);

    List<Notification> findByRecipientAndIsRead(User recipient, boolean read);
}
