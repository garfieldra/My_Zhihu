package com.example.myzhihu.controller;

import com.example.myzhihu.dto.FollowRequest;
import com.example.myzhihu.entity.Follow;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.repository.FollowRepository;
import com.example.myzhihu.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follows")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService)
    {
        this.followService = followService;
    }

    @PostMapping
    public Follow addFollow(@RequestBody FollowRequest followRequest)
    {
        return followService.addFollow(followRequest);
    }

    @DeleteMapping("/{followerId}/{followingId}")
    public ResponseEntity<Void> removeFollow(@PathVariable Long followerId, @PathVariable Long followingId)
    {
        followService.removeFollow(followerId, followingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count/followers/{userId}")
    public int countFollowers(@PathVariable Long userId)
    {
        return followService.countFollowers(userId);
    }

    @GetMapping("/count/followings/{userId}")
    public int countFollowings(@PathVariable Long userId)
    {
        return followService.countFollowings(userId);
    }

    @GetMapping("/followers/{userId}")
    public List<User> findFollowers(@PathVariable Long userId)
    {
        return followService.findFollowers(userId);
    }

    @GetMapping("/followings/{userId}")
    public List<User> findFollowings(@PathVariable Long userId)
    {
        return followService.findFollowings(userId);
    }

    @GetMapping("/exists/{followerId}/{followingId}")
    public boolean hasUserFollowed(@PathVariable Long followerId, @PathVariable Long followingId)
    {
        return followService.hasUserFollowed(followerId, followingId);
    }

}
