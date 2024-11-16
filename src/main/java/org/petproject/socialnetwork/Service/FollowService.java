package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.Exceptions.ErrorMessages;
import org.petproject.socialnetwork.Model.Follow;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.FollowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final Logger logger = LoggerFactory.getLogger(FollowService.class);

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public void followUser(User follower, User followee) {
        if (follower == null || followee == null) {
            logger.error("Follower or followee is null.");
            throw new IllegalArgumentException("Follower and followee cannot be null.");
        }
        if (follower.equals(followee)) {
            logger.warn("User {} tried to follow themselves.", follower.getUsername());
            throw new IllegalArgumentException(ErrorMessages.FOLLOW_SELF.getMessage());
        }
        if (followRepository.findByFollowerAndFollowee(follower, followee).isPresent()) {
            logger.info("User {} is already following user {}.", follower.getUsername(), followee.getUsername());
            throw new IllegalArgumentException(ErrorMessages.FOLLOW_ALREADY_EXISTS.getMessage());
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowee(followee);
        followRepository.save(follow);

        logger.info("User {} successfully followed user {}.", follower.getUsername(), followee.getUsername());
    }
}
