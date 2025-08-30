package com.example.myzhihu.dto;

import com.example.myzhihu.entity.NotificationType;

public class NotificationRequest {

    private Long userId;

    private NotificationType notificationType;

    private String message;

    public NotificationRequest() {}

    public NotificationRequest(Long userId, NotificationType notificationType, String message) {
        this.userId = userId;
        this.notificationType = notificationType;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
