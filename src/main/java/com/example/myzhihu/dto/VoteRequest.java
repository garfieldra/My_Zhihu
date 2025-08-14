package com.example.myzhihu.dto;

import com.example.myzhihu.entity.VoteType;

public class VoteRequest {

    private Long userId;

    private Long answerId;

    private VoteType voteType;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public VoteType getVoteType() {
        return voteType;
    }
}
