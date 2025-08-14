package com.example.myzhihu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne //一个用户可以发多个评论
    @JoinColumn(name = "user_id" ,nullable = false)
    private User user;

    @ManyToOne //一个回答下可以有多个评论
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    @ManyToOne //一个父评论可以有多个子评论
    @JoinColumn(name = "parent_comment_id", nullable = true)
    @JsonBackReference
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Comment> children;


    private LocalDateTime createdAt = LocalDateTime.now();


    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setChildren(List<Comment> children) {
        this.children = children;
    }

    public List<Comment> getChildren() {
        return children;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Answer getAnswer() {
        return answer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
