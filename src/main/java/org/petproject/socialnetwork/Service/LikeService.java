package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.DTO.LikeDTO;
import org.petproject.socialnetwork.Enums.NotificationType;
import org.petproject.socialnetwork.Exceptions.PostNotFound;
import org.petproject.socialnetwork.Exceptions.UserNotFound;
import org.petproject.socialnetwork.Mapper.LikeMapper;
import org.petproject.socialnetwork.Model.Likes;
import org.petproject.socialnetwork.Model.Post;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.LikeRepository;
import org.petproject.socialnetwork.Repository.PostRepository;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;

    private final NotificationService notificationService;

    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public LikeService(LikeRepository likeRepository, LikeMapper likeMapper, NotificationService notificationService, PostRepository postRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.likeMapper = likeMapper;
        this.notificationService = notificationService;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public LikeDTO toggleLike(Long postId, Long userId) {
        Optional<Likes> existingLike = likeRepository.findByPostIdAndUserId(postId, userId);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return null;
        }
        Post post=postRepository.findById(postId).orElseThrow(PostNotFound::new);
        User user=userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Likes like = new Likes();
        like.setPost(post);
        like.setUser(user);
        notificationService.createNotification(post.getUser(), user, NotificationType.LIKE, "You have a new like on your post!");

        return likeMapper.toDTO(likeRepository.save(like));
    }

}
