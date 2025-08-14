package com.example.myzhihu.dto;

public class QuestionRequest {

    private String title;

    private String content;

    private Long userId;

    public String getTitle()
    {
        return this.title;
    }

    public String getContent()
    {
        return this.content;
    }

    public Long getUserId()
    {
        return this.userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
