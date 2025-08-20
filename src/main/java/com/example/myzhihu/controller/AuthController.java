package com.example.myzhihu.controller;

import com.example.myzhihu.dto.LoginRequest;
import com.example.myzhihu.dto.RegisterRequest;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.exception.BusinessException;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.UserRepository;
import com.example.myzhihu.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 注册账号
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest)
    {
        if(userRepository.existsByUsername(registerRequest.getUsername()))
        {
            throw new BusinessException("该用户名已存在");
        }
//        if(registerRequest.getUsername().isEmpty())
//        {
//            throw new BusinessException("未填写用户名");
//        }
//        if(registerRequest.getPassword().isEmpty())
//        {
//            throw new BusinessException("未填写密码");
//        }
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setBio(registerRequest.getBio());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "注册成功"));
    }

    // 登录账号
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest)
    {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("未找到该用户名的用户"));
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("密码输入错误");
        }

        String token = jwtUtil.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal UserDetails userDetails)
    {
        return ResponseEntity.ok(userDetails.getUsername());
    }
}
