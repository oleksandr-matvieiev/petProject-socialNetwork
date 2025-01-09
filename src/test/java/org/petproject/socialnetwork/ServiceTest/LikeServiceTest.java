package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.DTO.LikeDTO;
import org.petproject.socialnetwork.Enums.NotificationType;
import org.petproject.socialnetwork.Exceptions.PostNotFound;
import org.petproject.socialnetwork.Exceptions.UserNotFound;
import org.petproject.socialnetwork.Mapper.LikeMapper;
import org.petproject.socialnetwork.Model.Likes;
import org.petproject.socialnetwork.Model.Post;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.LikeRepository;
import org.petproject.socialnetwork.Repository.PostRepository;
import org.petproject.socialnetwork.Repository.UserRepository;
import org.petproject.socialnetwork.Service.LikeService;
import org.petproject.socialnetwork.Service.NotificationService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private LikeMapper likeMapper;
    @Mock
    private NotificationService notificationService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private LikeService likeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toggleLike_AddLike() {
        Long postId = 1L;
        Long userId = 2L;

        User user = new User();
        user.setId(userId);
        user.setUsername("test");

        Post post = new Post();
        post.setId(postId);
        post.setUser(user);

        Likes like = new Likes();

        LikeDTO likeDTO = new LikeDTO();

        when(likeRepository.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.empty());
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(likeRepository.save(any(Likes.class))).thenReturn(like);
        when(likeMapper.toDTO(like)).thenReturn(likeDTO);


        LikeDTO result = likeService.toggleLike(postId, userId);

        assertNotNull(result);
        verify(likeRepository, times(1)).save(any(Likes.class));
        verify(likeMapper, times(1)).toDTO(any(Likes.class));
        verify(notificationService, times(1)).createNotification(eq(post.getUser()), eq(user), eq(NotificationType.LIKE), anyString());
    }

    @Test
    void toggleLike_removeLike() {
        Long postId = 1L;
        Long userId = 2L;

        Likes existingLike = new Likes();

        when(likeRepository.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.of(existingLike));

        LikeDTO result = likeService.toggleLike(postId, userId);

        assertNull(result);
        verify(likeRepository, times(1)).delete(existingLike);
        verifyNoInteractions(notificationService);

    }

    @Test
    void toggleLike_shouldThrowPostNotFound_whenPostDoesNotExist() {
        Long postId = 1L;
        Long userId = 2L;

        when(likeRepository.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.empty());
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(PostNotFound.class, () -> likeService.toggleLike(postId, userId));

        verify(postRepository).findById(postId);
        verifyNoInteractions(userRepository, notificationService);
    }

    @Test
    void toggleLike_shouldThrowUserNotFound_whenUserDoesNotExist() {
        Long postId = 1L;
        Long userId = 2L;

        Post post = new Post();
        post.setId(postId);

        when(likeRepository.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.empty());
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> likeService.toggleLike(postId, userId));

        verify(userRepository).findById(userId);
        verifyNoInteractions(notificationService);
    }
}