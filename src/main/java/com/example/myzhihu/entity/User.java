package com.example.myzhihu.entity;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    private String username;

    @Column(unique = true) //一个邮箱仅允许注册一个账号
    private String email;

    @Column(length = 500)
    private  String bio;

    public User(){}

    public User(long Id, String username, String email, String bio)
    {
        this.Id = Id;
        this.username = username;
        this.email = email;
        this.bio = bio;
    }

    public long getId()
    {
        return this.Id;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getBio()
    {
        return this.bio;
    }

    public void setId(long Id)
    {
        this.Id = Id;
    }

    public  void setUsername(String username)
    {
        this.username = username;
    }

    public  void setEmail(String email)
    {
        this.email = email;
    }

    public void setBio(String bio)
    {
        this.bio = bio;
    }

}
