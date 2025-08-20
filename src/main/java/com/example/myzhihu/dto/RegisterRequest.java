package com.example.myzhihu.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String bio;

    private String email;

    public RegisterRequest(){}

    public RegisterRequest(String username, String password, String bio, String email)
    {
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBio() {
        return bio;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
