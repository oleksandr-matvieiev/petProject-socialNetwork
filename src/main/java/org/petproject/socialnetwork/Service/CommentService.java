package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.DTO.CommentDTO;
import org.petproject.socialnetwork.Mapper.CommentMapper;
import org.petproject.socialnetwork.Model.Comment;
import org.petproject.socialnetwork.Model.Post;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.CommentRepository;
import org.petproject.socialnetwork.Repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final Logger logger = LoggerFactory.getLogger(CommentService.class);

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.commentMapper = commentMapper;
    }

    public CommentDTO addComment(Post post, String content, User user) {
        if (post == null || post.getId() == null || !postRepository.existsById(post.getId())) {
            logger.error("Post does not exist.");
            throw new IllegalArgumentException("Post does not exist.");
        }

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setContent(content);
        comment.setUser(user);
        return commentMapper.toDTO(commentRepository.save(comment));
    }

    public List<CommentDTO> getCommentsForPost(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
