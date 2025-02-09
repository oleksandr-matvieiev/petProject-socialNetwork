package org.petproject.socialnetwork.Repository;

import org.petproject.socialnetwork.Model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {
    List<Likes> findByPostId(Long postId);

    Optional<Likes> findByPostIdAndUserId(Long postId, Long userId);
    void deleteByPostId(Long post_id);
    List<Likes> findByUserId(Long userId);
}
