package com.example.myzhihu.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public long getId()
    {
        return this.id;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getContent()
    {
        return this.content;
    }

    public LocalDateTime getCreatedAt()
    {
        return this.createdAt;
    }

    public User getUser()
    {
        return this.user;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
