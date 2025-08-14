package com.example.myzhihu.dto;


public class AnswerRequest {

    private String content;

    private Long authorId;

    private Long questionId;

    public String getContent() {
        return content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
}
