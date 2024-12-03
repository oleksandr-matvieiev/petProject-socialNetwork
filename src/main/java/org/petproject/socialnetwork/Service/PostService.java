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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final Logger logger = LoggerFactory.getLogger(PostService.class);

    private static final String UPLOAD_DIR = "src/main/resources/uploads/";

    public PostService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }


    public PostDTO createPost(User user, String content, MultipartFile image) {
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
            try {
                String fileName = System.currentTimeMillis() + image.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);

                if (Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(image.getInputStream(), filePath);
                post.setImageUrl("/" + UPLOAD_DIR + fileName);
            } catch (IOException ex) {
                logger.error("Can not save file");
                throw new IllegalArgument("Failed to save file: " + ex.getMessage());
            }


        }
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

    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(postMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PostDTO> getPostsByUser(User user) {
        return postRepository.findByUser(user).stream()
                .map(postMapper::toDTO)
                .collect(Collectors.toList());
    }
}

