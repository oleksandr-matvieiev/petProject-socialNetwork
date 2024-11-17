package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.DTO.LikeDTO;
import org.petproject.socialnetwork.Mapper.LikeMapper;
import org.petproject.socialnetwork.Model.Likes;
import org.petproject.socialnetwork.Model.Post;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.LikeRepository;
import org.petproject.socialnetwork.Service.LikeService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private LikeMapper likeMapper;

    @InjectMocks
    private LikeService likeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toggleLike_AddLike() {
        Post post = new Post();
        post.setId(1L);
        User user = new User();
        user.setId(1L);
        user.setUsername("test");

        Likes like = new Likes();
        like.setPost(post);
        like.setUser(user);

        LikeDTO likeDTO = new LikeDTO();

        when(likeRepository.existsByPostIdAndUserId(post.getId(), user.getId())).thenReturn(false);
        when(likeRepository.save(any(Likes.class))).thenReturn(like);
        when(likeMapper.toDTO(like)).thenReturn(likeDTO);

        LikeDTO result = likeService.toggleLike(post, user);

        assertNotNull(result);
        verify(likeRepository, times(1)).save(any(Likes.class));
        verify(likeMapper, times(1)).toDTO(any(Likes.class));
    }
}
