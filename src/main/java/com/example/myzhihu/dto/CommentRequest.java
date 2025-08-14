package com.example.myzhihu.dto;


public class CommentRequest {

    private String content;

    private Long userId;


    private Long answerId;

    private Long parentCommentId;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }
}
