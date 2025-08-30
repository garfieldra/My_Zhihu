package com.example.myzhihu.controller;

import com.example.myzhihu.dto.NotificationRequest;
import com.example.myzhihu.entity.Notification;
import com.example.myzhihu.service.NotificationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/subscriptions/{userId}")
    public SseEmitter subscribe(@PathVariable Long userId) {
        return notificationService.subscribe(userId);
    }

    @PostMapping("/send")
    public void sendNotification(@RequestBody NotificationRequest notificationRequest){
        notificationService.sendNotification(notificationRequest);
        return;
    }

    @PostMapping
    public void createNotification(@RequestBody NotificationRequest notificationRequest){
        notificationService.addNotification(notificationRequest);
        return;
    }



    @GetMapping("/{userId}")
    public List<Notification> getUserNotifications(@PathVariable Long userId){
        return notificationService.getUserNotifications(userId);
    }

    @GetMapping("/unread/{userId}")
    public List<Notification> getUserUnreadNotifications(@PathVariable Long userId){
        return notificationService.getUserUnreadNotifications(userId);
    }

    @PostMapping("/read/{notificationId}")
    public void markAsRead(@PathVariable Long notificationId){
        notificationService.markAsRead(notificationId);
    }

    @DeleteMapping("/subscriptions/{userId}")
    public void removeSubscription(@PathVariable Long userId){
        notificationService.removeSubscription(userId);
    }
}
