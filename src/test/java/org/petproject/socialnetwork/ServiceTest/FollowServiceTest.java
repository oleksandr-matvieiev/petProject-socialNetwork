package org.petproject.socialnetwork.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.petproject.socialnetwork.Model.Follow;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.FollowRepository;
import org.petproject.socialnetwork.Service.FollowService;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @InjectMocks
    private FollowService followService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void followUser_Success() {
        User follower = new User();
        follower.setUsername("follower");
        follower.setId(1L);
        User followee = new User();
        followee.setUsername("followee");
        followee.setId(2L);

        when(followRepository.findByFollowerAndFollowee(follower, followee)).thenReturn(Optional.empty());

        followService.followUser(follower, followee);

        verify(followRepository, times(1)).save(any(Follow.class));
    }

}
