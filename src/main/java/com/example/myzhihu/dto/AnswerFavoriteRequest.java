package com.example.myzhihu.dto;

public class AnswerFavoriteRequest {

    private Long userId;

    private Long answerId;

    public AnswerFavoriteRequest() {}

    public AnswerFavoriteRequest(Long userId, Long answerId) {
        this.userId = userId;
        this.answerId = answerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }
}
