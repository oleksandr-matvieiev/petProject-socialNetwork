package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.DTO.PostDTO;
import org.petproject.socialnetwork.DTO.UserDTO;
import org.petproject.socialnetwork.Exceptions.PostNotFound;
import org.petproject.socialnetwork.Mapper.PostMapper;
import org.petproject.socialnetwork.Mapper.UserMapper;
import org.petproject.socialnetwork.Model.Post;
import org.petproject.socialnetwork.Repository.PostRepository;
import org.petproject.socialnetwork.Service.PostService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPost_ShouldReturnPostDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testUser");

        Post post = new Post();
        post.setId(1L);
        post.setContent("test post");

        PostDTO postDTO = new PostDTO();
        postDTO.setId(1L);
        postDTO.setContent("test post");
        postDTO.setUser(userDTO);

        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.toDTO(post)).thenReturn(postDTO);

        PostDTO result = postService.createPost(userMapper.toEntity(userDTO), "Test post");

        assertNotNull(result);
        assertEquals("Test post", result.getContent());
        assertEquals(userDTO.getUsername(), result.getUser().getUsername());

        verify(postRepository).save(any(Post.class));
        verify(postMapper).toDTO(post);
    }

    @Test
    void deletePost_ShouldDeletePostWhenExists() {
        Long postId = 1L;

        when(postRepository.existsById(postId)).thenReturn(true);

        postService.deletePost(postId);

        verify(postRepository).deleteById(postId);
    }

    @Test
    void deletePost_ShouldThrowExceptionWhenNotFound() {
        Long postId = 1L;

        when(postRepository.existsById(postId)).thenReturn(false);

        assertThrows(PostNotFound.class, () -> postService.deletePost(postId));

        verify(postRepository, never()).deleteById(anyLong());
    }
}
