package com.example.myzhihu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.myzhihu.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
