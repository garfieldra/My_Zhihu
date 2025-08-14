package com.example.myzhihu.controller;

import com.example.myzhihu.entity.User;
import com.example.myzhihu.repository.UserRepository;
import com.example.myzhihu.service.UserService;
import com.example.myzhihu.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService=userService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id)
    {
        return userService.getUserById(id);
//                          .map(ResponseEntity::ok)
//                          .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<User> getAllUsers()
    {
        return userService.getAllUsers();
    };

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id)
    {
        userService.deleteUser(id);
    };

    @PostMapping
    public User createUser(@RequestBody User user)
    {
        return userService.saveUser(user);
    };
}
