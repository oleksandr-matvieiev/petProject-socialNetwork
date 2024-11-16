package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.Exceptions.PostNotFound;
import org.petproject.socialnetwork.Model.Post;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.PostRepository;
import org.petproject.socialnetwork.Service.PostService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPost_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Post post = new Post();
        post.setContent("Test post");
        post.setUser(user);

        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post result = postService.createPost(user, "Test post");

        assertNotNull(result);
        assertEquals("Test post", result.getContent());
        assertEquals(user, result.getUser());

        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void deletePost_Success() {
        Long postId = 1L;

        when(postRepository.existsById(postId)).thenReturn(true);

        postService.deletePost(postId);

        verify(postRepository, times(1)).deleteById(postId);
    }

    @Test
    void deletePost_PostNotFound() {
        Long postId = 1L;

        when(postRepository.existsById(postId)).thenReturn(false);

        assertThrows(PostNotFound.class, () -> postService.deletePost(postId));

        verify(postRepository, times(0)).deleteById(postId);
    }

}
