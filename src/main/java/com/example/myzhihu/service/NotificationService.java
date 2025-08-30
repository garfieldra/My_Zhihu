package com.example.myzhihu.service;

import com.example.myzhihu.dto.NotificationRequest;
import com.example.myzhihu.entity.Notification;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface NotificationService {

    Notification  addNotification(NotificationRequest notificationRequest);

    List<Notification> getUserNotifications(Long userId);

    List<Notification> getUserUnreadNotifications(Long userId);

    void markAsRead(Long notificationId);

    SseEmitter subscribe(Long userId);

    void sendNotification(NotificationRequest notificationRequest);

    void removeSubscription(Long userId);

    int countUnreadNotifications(Long userId);

    int countNotifications(Long userId);
}
