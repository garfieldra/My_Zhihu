package com.example.myzhihu.service;

import com.example.myzhihu.dto.FollowRequest;
import com.example.myzhihu.entity.Follow;
import com.example.myzhihu.entity.User;

import java.util.List;

public interface FollowService {

    int countFollowers(Long userId);

    int countFollowings(Long userId);

    Follow addFollow(FollowRequest followRequest); //发起关注

    void removeFollow(Long followerId, Long followingId); //取消关注

    boolean hasUserFollowed(Long followerId, Long followingId);

    List<User> findFollowers(Long userId);

    List<User> findFollowings(Long userId);
}
