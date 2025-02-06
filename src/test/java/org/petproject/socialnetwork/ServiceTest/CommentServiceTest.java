package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.DTO.CommentDTO;
import org.petproject.socialnetwork.Exceptions.PostNotFound;
import org.petproject.socialnetwork.Mapper.CommentMapper;
import org.petproject.socialnetwork.Model.Comment;
import org.petproject.socialnetwork.Model.Post;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.CommentRepository;
import org.petproject.socialnetwork.Repository.PostRepository;
import org.petproject.socialnetwork.Service.CommentService;
import org.petproject.socialnetwork.Service.NotificationService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private NotificationService notificationService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addComment_Success() {
        Post post = new Post();
        post.setId(1L);

        User user = new User();
        user.setId(2L);

        String content = "This is a test comment";

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setUser(user);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent(content);

        when(postRepository.existsById(post.getId())).thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toDTO(comment)).thenReturn(commentDTO);

        CommentDTO result = commentService.addComment(post, content, user);

        assertNotNull(result);
        assertEquals(content, result.getContent());
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(commentMapper, times(1)).toDTO(any(Comment.class));
    }

    @Test
    void addComment_PostDoesNotExist() {
        Post post = new Post();
        post.setId(1L);

        User user = new User();
        user.setId(2L);

        String content = "This is a test comment";

        when(postRepository.existsById(post.getId())).thenReturn(false);

        assertThrows(PostNotFound.class, () -> commentService.addComment(post, content, user));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void getCommentsForPost() {
        Post post = new Post();
        post.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test comment");

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setContent("Test comment");

        when(commentRepository.findByPostId(post.getId())).thenReturn(List.of(comment));
        when(commentMapper.toDTO(comment)).thenReturn(commentDTO);

        List<CommentDTO> result = commentService.getCommentsForPost(post.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test comment", result.get(0).getContent());
        verify(commentRepository, times(1)).findByPostId(post.getId());
        verify(commentMapper, times(1)).toDTO(any(Comment.class));
    }
}
