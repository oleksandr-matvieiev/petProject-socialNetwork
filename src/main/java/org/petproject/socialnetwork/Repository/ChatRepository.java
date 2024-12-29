package org.petproject.socialnetwork.Repository;

import org.petproject.socialnetwork.Model.Chat;
import org.petproject.socialnetwork.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByUserOneAndUserTwo(User userOne, User userTwo);
    List<Chat> findByUserOneOrUserTwo(User userOne, User userTwo);
}
