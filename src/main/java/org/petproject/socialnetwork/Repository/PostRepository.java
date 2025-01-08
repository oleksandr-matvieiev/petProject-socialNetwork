package org.petproject.socialnetwork.Repository;

import org.petproject.socialnetwork.Model.Post;
import org.petproject.socialnetwork.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);

    List<Post> findByUserUsername(String username);
}
