package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.Model.Comment;
import org.petproject.socialnetwork.Model.Post;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.CommentRepository;
import org.petproject.socialnetwork.Repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final Logger logger= LoggerFactory.getLogger(CommentService.class);

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public Comment addComment(Post post, String content, User user) {
        if (post == null || post.getId() == null || !postRepository.existsById(post.getId())) {
            logger.error("Post does not exist.");
            throw new IllegalArgumentException("Post does not exist.");
        }


        Comment comment = new Comment();
        comment.setPost(post);
        comment.setContent(content);
        comment.setUser(user);

        logger.info("Comment saved");
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsForPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }

}
