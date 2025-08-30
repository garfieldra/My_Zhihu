package com.example.myzhihu.repository;

import com.example.myzhihu.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    int countByUserIdAndReadIsFalse(long userId);

    int countByUserId(long userId);

    List<Notification> findByUserIdAndReadIsFalse(long userId);

    List<Notification> findByUserId(long userId);
}
