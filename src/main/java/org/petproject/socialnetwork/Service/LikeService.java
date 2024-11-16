package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.Model.Likes;
import org.petproject.socialnetwork.Model.Post;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.LikeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final Logger logger = LoggerFactory.getLogger(LikeService.class);

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Transactional
    public void toggleLike(Post post, User user) {
        if (likeRepository.existsByPostIdAndUserId(post.getId(), user.getId())) {
            Likes like = likeRepository.findByPostIdAndUserId(post.getId(), user.getId());
            logger.info("User: {} delete like from post id: {}", user.getUsername(), post.getId());
            likeRepository.delete(like);
        } else {
            Likes like = new Likes();
            like.setPost(post);
            like.setUser(user);
            logger.info("User: {} liked post id: {}", user.getUsername(), post.getId());
            likeRepository.save(like);
        }
    }

    public boolean isPostLikedByUser(Long postId, Long userId) {
        return likeRepository.existsByPostIdAndUserId(postId, userId);
    }
}
