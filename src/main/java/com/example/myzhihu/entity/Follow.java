package com.example.myzhihu.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name= "follow", uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"}))
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    private LocalDateTime createdAt = LocalDateTime.now();

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollowing(User following) {
        this.following = following;
    }

    public User getFollowing() {
        return following;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
