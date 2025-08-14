package com.example.myzhihu.service;

import com.example.myzhihu.entity.User;
import java.util.*;

public interface UserService {
    User getUserById(Long id); //通过id查找用户

    User saveUser(User user); //创建或修改用户信息

    List<User> getAllUsers(); //获取所有用户

    void deleteUser(Long id); //根据id删除用户
}
