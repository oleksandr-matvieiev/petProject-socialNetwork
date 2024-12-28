package org.petproject.socialnetwork.Repository;

import org.petproject.socialnetwork.Model.Message;
import org.petproject.socialnetwork.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE (m.sender = :user1 AND m.receiver = :user2) " +
            "OR (m.sender = :user2 AND m.receiver = :user1)")
    List<Message> findByParticipants(@Param("user1") User user1, @Param("user2") User user2);

    List<Message> findByReceiver(User receiver);

    List<Message> findByReceiverAndIsReadFalse(User receiver);

}
