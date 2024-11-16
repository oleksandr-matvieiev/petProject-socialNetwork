package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.Exceptions.ErrorMessages;
import org.petproject.socialnetwork.Exceptions.PostNotFound;
import org.petproject.socialnetwork.Model.Post;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final Logger logger = LoggerFactory.getLogger(PostService.class);

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(User user, String content) {
        if (user == null) {
            logger.error("Attempt to create post with null user.");
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (content == null || content.isBlank()) {
            logger.error("Attempt to create post with blank content.");
            throw new IllegalArgumentException("Content cannot be blank.");
        }

        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        Post savedPost = postRepository.save(post);
        logger.info("Post created for user: {}", user.getUsername());
        return savedPost;
    }

    @Transactional
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            logger.error("Attempt to delete non-existent post with ID: {}", postId);
            throw new PostNotFound(ErrorMessages.POST_NOT_FOUND.getMessage());
        }
        postRepository.deleteById(postId);
        logger.info("Post with ID: {} successfully deleted.", postId);
    }
}

