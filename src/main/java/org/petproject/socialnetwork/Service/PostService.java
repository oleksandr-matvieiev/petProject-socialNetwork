package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.DTO.PostDTO;
import org.petproject.socialnetwork.Exceptions.IllegalArgument;
import org.petproject.socialnetwork.Exceptions.PostNotFound;
import org.petproject.socialnetwork.Mapper.PostMapper;
import org.petproject.socialnetwork.Model.Post;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final Logger logger = LoggerFactory.getLogger(PostService.class);

    public PostService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    public PostDTO createPost(User user, PostDTO postDTO) {
        if (user == null) {
            logger.error("Attempt to create post with null user.");
            throw new IllegalArgument("User cannot be null.");
        }
        if (postDTO.getContent() == null || postDTO.getContent().isBlank()) {
            logger.error("Attempt to create post with blank content.");
            throw new IllegalArgument("Content cannot be blank.");
        }
        Post post = new Post();
//        if (!postDTO.getImageUrl().isEmpty()){
//            post.setImageUrl(postDTO.getImageUrl());
//        }
        post.setUser(user);
        post.setContent(postDTO.getContent());
        return postMapper.toDTO(postRepository.save(post));
    }

    @Transactional
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            logger.error("Attempt to delete non-existent post with ID: {}", postId);
            throw new PostNotFound();
        }
        postRepository.deleteById(postId);
        logger.info("Post with ID: {} successfully deleted.", postId);
    }

    public List<PostDTO> getPostsByUser(User user) {
        return postRepository.findByUser(user).stream()
                .map(postMapper::toDTO)
                .collect(Collectors.toList());
    }
}

