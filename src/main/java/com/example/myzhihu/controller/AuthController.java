package com.example.myzhihu.controller;

import com.example.myzhihu.dto.LoginRequest;
import com.example.myzhihu.dto.RegisterRequest;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.exception.BusinessException;
import com.example.myzhihu.exception.OwnershipMismatchException;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.UserRepository;
import com.example.myzhihu.search.UserDocument;
import com.example.myzhihu.search.UserSearchService;
import com.example.myzhihu.security.JwtBlacklistService;
import com.example.myzhihu.security.JwtUtil;
import com.example.myzhihu.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private UserSearchService userSearchService;
    private NotificationService notificationService;
    private final JwtBlacklistService jwtBlacklistService;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserSearchService userSearchService, NotificationService notificationService, JwtBlacklistService jwtBlacklistService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userSearchService = userSearchService;
        this.notificationService = notificationService;
        this.jwtBlacklistService = jwtBlacklistService;
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

        UserDocument userDocument = new UserDocument(
                user.getId(),
                user.getUsername()
        );
        userSearchService.saveUserDocument(userDocument);

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

        SseEmitter emitter = notificationService.subscribe(user.getId());

        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal UserDetails userDetails)
    {
        return ResponseEntity.ok(userDetails.getUsername());
    }

    @PostMapping("/logout/{userId}")
    public ResponseEntity<?> logout(@PathVariable Long userId, HttpServletRequest request)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = (auth == null ? null : auth.getName());
        if(currentUsername == null)
        {
            throw new OwnershipMismatchException("未登录账号，无法登出");
        }
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("当前账号不存在"));
        if(!userId.equals(user.getId()))
        {
            throw new OwnershipMismatchException("无权登出他人账号");
        }

        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new BusinessException("未提供有效认证信息");
        }

        String token = authHeader.substring(7);

        if(!jwtUtil.validateToken(token))
        {
            throw new BusinessException("无效的token");
        }

        String tokenUsername = jwtUtil.extractUsername(token);
        if(!tokenUsername.equals(currentUsername))
        {
            throw new BusinessException("token 与当前用户不匹配");
        }

        Date expiration = jwtUtil.extractExpiration(token);
        long expirationTimeMillis = expiration.getTime();
        jwtBlacklistService.addToBlacklist(tokenUsername, expirationTimeMillis);

        SecurityContextHolder.clearContext(); //清除当前安全上下文

        notificationService.removeSubscription(userId);

        return ResponseEntity.ok(Map.of("message", "登出成功"));

    }
}
