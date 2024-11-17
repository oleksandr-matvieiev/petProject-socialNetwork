package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.DTO.LikeDTO;
import org.petproject.socialnetwork.Mapper.LikeMapper;
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
    private final LikeMapper likeMapper;
    private final Logger logger = LoggerFactory.getLogger(LikeService.class);

    public LikeService(LikeRepository likeRepository, LikeMapper likeMapper) {
        this.likeRepository = likeRepository;
        this.likeMapper = likeMapper;
    }

    @Transactional
    public LikeDTO toggleLike(Post post, User user) {
        Likes like;
        if (likeRepository.existsByPostIdAndUserId(post.getId(), user.getId())) {
            like = likeRepository.findByPostIdAndUserId(post.getId(), user.getId());
            logger.info("User: {} removed like from post id: {}", user.getUsername(), post.getId());
            likeRepository.delete(like);
        } else {
            like = new Likes();
            like.setPost(post);
            like.setUser(user);
            logger.info("User: {} liked post id: {}", user.getUsername(), post.getId());
            return likeMapper.toDTO(likeRepository.save(like));
        }
        return null;
    }
}
