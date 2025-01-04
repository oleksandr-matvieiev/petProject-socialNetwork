package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.DTO.PostDTO;
import org.petproject.socialnetwork.Enums.FileCategory;
import org.petproject.socialnetwork.Exceptions.IllegalArgument;
import org.petproject.socialnetwork.Exceptions.PostNotFound;
import org.petproject.socialnetwork.Mapper.PostMapper;
import org.petproject.socialnetwork.Model.Post;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.CommentRepository;
import org.petproject.socialnetwork.Repository.LikeRepository;
import org.petproject.socialnetwork.Repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final FileStorageService fileStorageService;
    private final Logger logger = LoggerFactory.getLogger(PostService.class);

    public PostService(PostRepository postRepository, PostMapper postMapper, LikeRepository likeRepository, CommentRepository commentRepository, FileStorageService fileStorageService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.fileStorageService = fileStorageService;
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(PostNotFound::new);

    }

    public PostDTO createPost(User user, String content, MultipartFile image) throws IOException {
        if (user == null) {
            logger.error("Attempt to create post with null user.");
            throw new IllegalArgument("User cannot be null.");
        }
        if (content == null || content.isBlank()) {
            logger.error("Attempt to create post with blank content.");
            throw new IllegalArgument("Content cannot be blank.");
        }
        Post post = new Post();
        post.setUser(user);
        post.setContent(content);


        if (image != null && !image.isEmpty()) {
            String imageUrl = fileStorageService.saveImage(image, FileCategory.POST_IMAGE);
            post.setImageUrl(imageUrl);
        }
        return postMapper.toDTO(postRepository.save(post));
    }

    @Transactional
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            logger.error("Attempt to delete non-existent post with ID: {}", postId);
            throw new PostNotFound();
        }
        likeRepository.deleteByPostId(postId);
        commentRepository.deleteByPostId(postId);
        postRepository.deleteById(postId);
        logger.info("Post with ID: {} successfully deleted.", postId);
    }

    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> {
                    PostDTO postDTO = postMapper.toDTO(post);
                    postDTO.setLikeCount(likeRepository.findByPostId(post.getId()).size());
                    return postDTO;
                })
                .collect(Collectors.toList());
    }

    public List<PostDTO> getPostsByUser(User user) {
        return postRepository.findByUser(user).stream()
                .map(postMapper::toDTO)
                .collect(Collectors.toList());
    }
}
    
