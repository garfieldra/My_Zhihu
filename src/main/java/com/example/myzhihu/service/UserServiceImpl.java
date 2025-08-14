package com.example.myzhihu.service;

import com.example.myzhihu.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.myzhihu.entity.User;
import java.util.List;
import java.util.Optional;
import com.example.myzhihu.exception.ResourceNotFoundException;

@Service
public class UserServiceImpl implements UserService{

    private  final UserRepository userRepository;

    public  UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository=userRepository;
    }

    public User saveUser(User user)
    {
        return userRepository.save(user);
        //return user;
    }

    public User getUserById(Long id) //Optional：可能找不到结果。返回值也可使用User，需要加上.orElse(null)
    {
        return userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("未找到id为" + id + "的用户"));
    }

    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    public void deleteUser(Long id)
    {
        if(!userRepository.existsById(id))
        {
            throw new ResourceNotFoundException("未找到id为" + id + "的用户");
        }
        userRepository.deleteById(id);
        return;
    }

}
