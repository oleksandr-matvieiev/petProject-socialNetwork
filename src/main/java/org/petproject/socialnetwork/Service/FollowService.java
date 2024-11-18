package org.petproject.socialnetwork.Service;

import org.petproject.socialnetwork.DTO.FollowDTO;
import org.petproject.socialnetwork.Exceptions.ErrorMessages;
import org.petproject.socialnetwork.Exceptions.IllegalArgument;
import org.petproject.socialnetwork.Mapper.FollowMapper;
import org.petproject.socialnetwork.Model.Follow;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Repository.FollowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final FollowMapper followMapper;
    private final Logger logger = LoggerFactory.getLogger(FollowService.class);

    public FollowService(FollowRepository followRepository, FollowMapper followMapper) {
        this.followRepository = followRepository;
        this.followMapper = followMapper;
    }

    public FollowDTO followUser(User follower, User followee) {
        if (follower == null || followee == null) {
            logger.error("Follower or followee is null.");
            throw new IllegalArgument("Follower and followee cannot be null.");
        }
        if (follower.equals(followee)) {
            logger.warn("User {} tried to follow themselves.", follower.getUsername());
            throw new IllegalArgument(ErrorMessages.FOLLOW_SELF.getMessage());
        }
        if (followRepository.findByFollowerAndFollowee(follower, followee).isPresent()) {
            logger.info("User {} is already following user {}.", follower.getUsername(), followee.getUsername());
            throw new IllegalArgument(ErrorMessages.FOLLOW_ALREADY_EXISTS.getMessage());
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowee(followee);
        logger.info("User {} successfully followed user {}.", follower.getUsername(), followee.getUsername());
        return followMapper.toDTO(followRepository.save(follow));
    }

    public List<FollowDTO> getFollowers(User user) {
        return followRepository.findByFollowee(user).stream()
                .map(followMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<FollowDTO> getFollowees(User user) {
        return followRepository.findByFollower(user).stream()
                .map(followMapper::toDTO)
                .collect(Collectors.toList());
    }
}
