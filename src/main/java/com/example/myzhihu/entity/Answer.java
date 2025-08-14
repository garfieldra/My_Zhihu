package com.example.myzhihu.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne  //一个用户可以撰写多个回答
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne  //一个问题下可以有多个回答
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User getAuthor() {
        return author;
    }

    public Question getQuestion() {
        return question;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
