package com.example.myzhihu.dto;

public class FollowRequest {

    private Long followerId;

    private Long followingId;

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }

    public Long getFollowingId() {
        return followingId;
    }
}
