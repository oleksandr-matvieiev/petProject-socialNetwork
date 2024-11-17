package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.DTO.FollowDTO;
import org.petproject.socialnetwork.Mapper.FollowMapper;
import org.petproject.socialnetwork.Model.Follow;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.FollowRepository;
import org.petproject.socialnetwork.Service.FollowService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private FollowMapper followMapper;

    @InjectMocks
    private FollowService followService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void followUser_Success() {
        User follower = new User();
        follower.setId(1L);
        User followee = new User();
        followee.setId(2L);

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowee(followee);

        FollowDTO followDTO = new FollowDTO();

        when(followRepository.findByFollowerAndFollowee(follower, followee)).thenReturn(Optional.empty());
        when(followRepository.save(any(Follow.class))).thenReturn(follow);
        when(followMapper.toDTO(follow)).thenReturn(followDTO);

        FollowDTO result = followService.followUser(follower, followee);

        assertNotNull(result);
        verify(followRepository, times(1)).save(any(Follow.class));
        verify(followMapper, times(1)).toDTO(any(Follow.class));
    }

    @Test
    void followUser_AlreadyFollowing() {
        User follower = new User();
        User followee = new User();

        Follow follow = new Follow();

        when(followRepository.findByFollowerAndFollowee(follower, followee)).thenReturn(Optional.of(follow));

        assertThrows(IllegalArgumentException.class, () -> followService.followUser(follower, followee));
        verify(followRepository, never()).save(any(Follow.class));
    }
}
