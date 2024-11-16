package org.petproject.socialnetwork.Repository;

import org.petproject.socialnetwork.Model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {
    List<Likes> findByPostId(Long postId);

    boolean existsByPostIdAndUserId(Long postId, Long userId);

    Likes findByPostIdAndUserId(Long postId, Long userId);
}
