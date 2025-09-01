package com.example.myzhihu.service;

import com.example.myzhihu.dto.NotificationRequest;
import com.example.myzhihu.entity.Notification;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.exception.OwnershipMismatchException;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.NotificationRepository;
import com.example.myzhihu.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Long.MAX_VALUE;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private ConcurrentHashMap<Long, SseEmitter>  sseEmitters = new ConcurrentHashMap<>();
    public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Notification addNotification(NotificationRequest notificationRequest) {
        Notification notification = new Notification();
        notification.setUserId(notificationRequest.getUserId());
        notification.setNotificationType(notificationRequest.getNotificationType());
        notification.setMessage(notificationRequest.getMessage());
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public List<Notification> getUserUnreadNotifications(Long userId)
    {
        return notificationRepository.findByUserIdAndReadIsFalse(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = (authentication == null ? null : authentication.getName());
        if (currentUserName == null) {
            throw new OwnershipMismatchException("未登录，无法标记已读");
        }
        User user = userRepository.findByUsername(currentUserName)
                .orElseThrow(() -> new ResourceNotFoundException("当前用户不存在"));

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("未找到该通知"));

        if(!notification.getUserId().equals(user.getId()))
        {
            throw new OwnershipMismatchException("无权为他人的通知标记已读");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public SseEmitter subscribe(Long userId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentUserName = authentication.getName();
        String currentUsername = (authentication == null ? null : authentication.getName());
        if(currentUsername == null){
            throw new OwnershipMismatchException("未登录，无法订阅通知");
        }

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("当前用户不存在"));

        if(!userId.equals(user.getId())){
            throw new OwnershipMismatchException("无权订阅他人通知");
        }

        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        sseEmitters.put(userId, sseEmitter);

        sseEmitter.onCompletion(() -> sseEmitters.remove(userId));
        sseEmitter.onTimeout(() -> sseEmitters.remove(userId));
        sseEmitter.onError((e) -> sseEmitters.remove(userId));

        return sseEmitter;
    }

    @Override
    public void sendNotification(NotificationRequest notificationRequest) {
        Long userId = notificationRequest.getUserId();
        SseEmitter sseEmitter = sseEmitters.get(userId);
        if (sseEmitter != null) {
            try{
                sseEmitter.send(notificationRequest);
            } catch (IOException e) {
                sseEmitters.remove(userId);
            }
        }

        Notification notification = new Notification(
                notificationRequest.getUserId(),
                notificationRequest.getNotificationType(),
                notificationRequest.getMessage()
        );
        notificationRepository.save(notification);
    }

    @Override
    public void removeSubscription(Long userId) {
        SseEmitter sseEmitter = sseEmitters.remove(userId);
        if(sseEmitter != null){
            try{
                sseEmitter.complete();
            } catch (Exception e){}
        }
    }

    @Override
    public int countUnreadNotifications(Long userId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = (authentication == null ? null : authentication.getName());
        if(currentUsername == null){
            throw new OwnershipMismatchException("未登录，无法获取未读通知数");
        }
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("当前用户不存在"));
        if(!userId.equals(user.getId())){
            throw new OwnershipMismatchException("无权获取他人未读通知数");
        }

        return notificationRepository.countByUserIdAndReadIsFalse(userId);
    }

    @Override
    public int countNotifications(Long userId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = (authentication == null ? null : authentication.getName());
        if(currentUsername == null){
            throw new OwnershipMismatchException("未登录，无法获取通知数");
        }
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("当前用户不存在"));
        if(!userId.equals(user.getId())){
            throw new OwnershipMismatchException("无权获取他人通知数");
        }

        return notificationRepository.countByUserId(userId);
    }
}
