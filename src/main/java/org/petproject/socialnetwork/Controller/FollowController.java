package org.petproject.socialnetwork.Controller;

import org.petproject.socialnetwork.DTO.FollowDTO;
import org.petproject.socialnetwork.Model.User;
import org.petproject.socialnetwork.Service.AuthenticationService;
import org.petproject.socialnetwork.Service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    private final FollowService followService;
    private final AuthenticationService authenticationService;

    public FollowController(FollowService followService, AuthenticationService authenticationService) {
        this.followService = followService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/{followeeUsername}")
    public ResponseEntity<?> followUser(@PathVariable String followeeUsername) {
        User currentUser = authenticationService.getCurrentUser();
        User followee = authenticationService.findUserByUsername(followeeUsername);
        FollowDTO followDTO = followService.followUser(currentUser, followee);
        return ResponseEntity.ok(followDTO);
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<FollowDTO>> getFollowers(@PathVariable String username) {
        User user = authenticationService.findUserByUsername(username);
        List<FollowDTO> followers = followService.getFollowers(user);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{username}/following")
    public ResponseEntity<List<FollowDTO>> getFollowees(@PathVariable String username) {
        User user = authenticationService.findUserByUsername(username);
        List<FollowDTO> followees = followService.getFollowees(user);
        return ResponseEntity.ok(followees);
    }
    @DeleteMapping("/{followeeUsername}")
    public ResponseEntity<?> unfollowUser(@PathVariable String followeeUsername) {
        User currentUser = authenticationService.getCurrentUser();
        User followee = authenticationService.findUserByUsername(followeeUsername);
        followService.unfollowUser(currentUser, followee);
        return ResponseEntity.noContent().build();
    }

}

