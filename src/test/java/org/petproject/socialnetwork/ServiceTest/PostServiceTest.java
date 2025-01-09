package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import org.petproject.socialnetwork.Service.FileStorageService;
import org.petproject.socialnetwork.Service.PostService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPost_ShouldReturnPostDTO() throws IOException {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Post post = new Post();
        post.setId(1L);
        post.setContent("Test post");

        PostDTO postDTO = new PostDTO();
        postDTO.setId(1L);
        postDTO.setContent("Test post");

        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.toDTO(any(Post.class))).thenReturn(postDTO);

        PostDTO result = postService.createPost(user, "Test post", null);

        assertNotNull(result);
        assertEquals("Test post", result.getContent());

        verify(postRepository).save(any(Post.class));
        verify(postMapper).toDTO(post);
    }

    @Test
    void createPost_ShouldSaveImageWhenProvided() throws IOException {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Post post = new Post();
        post.setId(1L);
        post.setContent("Test post");
        post.setImageUrl("test/image/url");

        PostDTO postDTO = new PostDTO();
        postDTO.setId(1L);
        postDTO.setContent("Test post");

        MultipartFile image = mock(MultipartFile.class);
        when(image.isEmpty()).thenReturn(false);
        when(fileStorageService.saveImage(any(MultipartFile.class), any())).thenReturn("test/image/url");
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.toDTO(any(Post.class))).thenReturn(postDTO);

        PostDTO result = postService.createPost(user, "Test post", image);

        assertNotNull(result);
        assertEquals("Test post", result.getContent());
        assertEquals("test/image/url", post.getImageUrl());

        verify(fileStorageService).saveImage(image, FileCategory.POST_IMAGE);
        verify(postRepository).save(any(Post.class));
        verify(postMapper).toDTO(post);
    }

    @Test
    void createPost_ShouldThrowExceptionWhenUserIsNull() {
        assertThrows(IllegalArgument.class, () -> postService.createPost(null, "Test post", null));
    }

    @Test
    void createPost_ShouldThrowExceptionWhenContentIsBlank() {
        User user = new User();
        user.setId(1L);

        assertThrows(IllegalArgument.class, () -> postService.createPost(user, "  ", null));
    }

    @Test
    void deletePost_ShouldDeletePostWhenExists() {
        Long postId = 1L;

        when(postRepository.existsById(postId)).thenReturn(true);

        postService.deletePost(postId);

        verify(likeRepository).deleteByPostId(postId);
        verify(commentRepository).deleteByPostId(postId);
        verify(postRepository).deleteById(postId);
    }

    @Test
    void deletePost_ShouldThrowExceptionWhenNotFound() {
        Long postId = 1L;

        when(postRepository.existsById(postId)).thenReturn(false);

        assertThrows(PostNotFound.class, () -> postService.deletePost(postId));

        verify(likeRepository, never()).deleteByPostId(anyLong());
        verify(commentRepository, never()).deleteByPostId(anyLong());
        verify(postRepository, never()).deleteById(anyLong());
    }

    @Test
    void getAllPosts_ShouldReturnPostDTOList() {
        Post post = new Post();
        post.setId(1L);
        post.setContent("Test post");

        PostDTO postDTO = new PostDTO();
        postDTO.setId(1L);
        postDTO.setContent("Test post");

        when(postRepository.findAll()).thenReturn(Collections.singletonList(post));
        when(postMapper.toDTO(any(Post.class))).thenReturn(postDTO);
        when(likeRepository.findByPostId(post.getId())).thenReturn(Collections.emptyList());

        List<PostDTO> result = postService.getAllPosts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test post", result.getFirst().getContent());

        verify(postRepository).findAll();
        verify(postMapper).toDTO(post);
        verify(likeRepository).findByPostId(post.getId());
    }

    @Test
    void getPostsByUser_ShouldReturnPostDTOList() {
        User user = new User();
        user.setId(1L);

        Post post = new Post();
        post.setId(1L);
        post.setContent("Test post");

        PostDTO postDTO = new PostDTO();
        postDTO.setId(1L);
        postDTO.setContent("Test post");

        when(postRepository.findByUser(user)).thenReturn(Collections.singletonList(post));
        when(postMapper.toDTO(any(Post.class))).thenReturn(postDTO);

        List<PostDTO> result = postService.getPostsByUser(user);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test post", result.getFirst().getContent());

        verify(postRepository).findByUser(user);
        verify(postMapper).toDTO(post);
    }
}
